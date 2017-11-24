package iohelpers;

import com.jcraft.jsch.*;
import entities.parsing.Machine;
import exceptions.WooshException;
import iohelpers.interfaces.SSHClientInterface;
import tools.Utils;
import tools.WooshLogger;

import java.io.*;
import java.util.Scanner;

import static values.Constants.SERVERPATH;

public class SSHClient implements SSHClientInterface {

    private WooshLogger logger;

    public SSHClient(){
        logger = new WooshLogger();
    }

    private void setKnownHostFile(JSch jsch) throws WooshException {
        try {
            File file = Utils.generateFile(System.getProperty("user.home") + "/.ssh/known_hosts");
            jsch.setKnownHosts(file.getAbsolutePath());
        } catch (Exception e) {
            throw new WooshException(e.getMessage());
        }
    }

    @Override
    public void addKnownHost(Machine machine) throws WooshException {
        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            if (machine.isUseSSHKey()) {
                jsch.addIdentity(machine.getSshKeyPath());
            } else {
                session.setPassword(machine.getPassword());
            }
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            session.disconnect();
        } catch (Exception e) {
            throw new WooshException(e.getMessage());
        }
    }

    @Override
    public void testConnection(Machine machine) throws WooshException {

        JSch jsch = new JSch();
        setKnownHostFile(jsch);
        try {
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            if (machine.isUseSSHKey()) {
                jsch.addIdentity(machine.getSshKeyPath());
            } else {
                session.setPassword(machine.getPassword());
            }
            session.connect(8000);
            session.disconnect();
        } catch (JSchException e) {
            if (e.getMessage().contains("UnknownHostKey")) {
                throw new WooshException(" Unknown host, fingerprint: " + e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1));
            } else if (e.getMessage().contains("Connection refused")) {
                throw new WooshException("Connection refused");
            }else if (e.getMessage().contains("Connection timed out")) {
                throw new WooshException("Connection timed out");
            }else if (e.getMessage().contains("No route to host")) {
                throw new WooshException("No route to host: please check your internet connection.");
            } else if (e.getMessage().contains("Connection reset")) {
                throw new WooshException("Connection reset: Is an SSH client installed?");
            }else{
                throw new WooshException(e.getMessage());
            }
        } catch (Exception ex) {
            throw new WooshException(" Error with " + machine.getIp());
        }

    }

    @Override
    public String sendPackage(Machine machine) throws WooshException {

        logger.startNewLog(machine.getName());
        logger.appendLoggingWithOutTime("-----------------------STARTED----------------------------");
        logger.appendLoggingWithOutTime("DEPLOYMENTLOG FOR: " + machine.getName());
        logger.appendLoggingWithOutTime("IP: " + machine.getIp());

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            setKnownHostFile(jsch);
            if (machine.isUseSSHKey()) {
                jsch.addIdentity(machine.getSshKeyPath());
            } else {
                logger.hideCharset(machine.getPassword());
                session.setPassword(machine.getPassword());
            }
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp channelSftp = (ChannelSftp) channel;
            executeRemoteCommandAsSudo(session, true, machine.getPassword(), "sudo mkdir -p " + SERVERPATH);
            channelSftp.cd(SERVERPATH);

            executeRemoteCommandAsSudo(session, true, machine.getPassword(), "sudo chmod -R 777 " + SERVERPATH);

            File f = new File(machine.getPathCompressed());
            channelSftp.put(new FileInputStream(f), f.getName());
            Scanner scanner = new Scanner(machine.getBashScript());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                executeRemoteCommandAsSudo(session, scanner.hasNextLine(), machine.getPassword(), line);
            }
            channel.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            if (e.getMessage().contains("UnknownHostKey")) {
                return "Unknown host, fingerprint: " + e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1);
            } else if (e.getMessage().contains("Connection refused")) {
                return "Connection refused";
            }else if (e.getMessage().contains("Connection timed out")) {
                return "Connection timed out";
            }else if (e.getMessage().contains("No route to host")) {
                return "No route to host: please check your internet connection.";
            }else{
                return e.getMessage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            logger.appendLoggingWithOutTime("-----------------------DONE-------------------------------");
            logger.saveLogsAndClear();
        }
        return "Success";
    }

    private void executeRemoteCommandAsSudo(Session session, boolean hasMore, String password,
                                            String command) throws WooshException {
        Channel channel = null;
        StringBuffer result = new StringBuffer();
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setPty(true);
            channel.connect();
            InputStream stdout = channel.getInputStream();
            InputStream stderr = ((ChannelExec) channel).getErrStream();
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);
            out.write((password + "\n").getBytes());
            out.flush();
            out.close();
            logger.appendLoggingWithTime(command);
            byte[] tmp = new byte[1024];
            if(hasMore) {
                while (true) {
                    while (stdout.available() > 0) {
                        if (!read(stdout, tmp, logger)) break;
                    }
                    while (stderr.available() > 0) {
                        if (!read(stderr, tmp, logger)) break;
                    }
                    if (channel.isClosed()) {
                        logger.appendLoggingWithTime("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    Thread.sleep(400);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WooshException("Failure in executing command " + command);
        } finally {
            channel.disconnect();
        }
    }

    private boolean read(InputStream stdout, byte[] tmp, WooshLogger logger) throws IOException {
        int i = stdout.read(tmp, 0, 1024);
        if (i < 0) return false;
        String log = new String(tmp, 0, i);
        logger.appendLoggingWithTime(log);
        return true;
    }

}