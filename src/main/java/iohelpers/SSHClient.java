package iohelpers;

import com.jcraft.jsch.*;
import entities.parsing.Machine;
import exceptions.WooshException;
import tools.Utils;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import static values.Constants.SERVERPATH;

public class SSHClient implements iohelpers.interfaces.SSHClientInterface {


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
            session.setPassword(machine.getPassword());
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
        try{
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            session.setPassword(machine.getPassword());
            session.connect();
            session.disconnect();
        } catch (JSchException e) {
            if (e.getMessage().contains("UnknownHostKey")) {
                throw new WooshException(" Unknown host, fingerprint: " + e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1));
            } else if (e.getMessage().contains("Connection refused")) {
                throw new WooshException("Connection refused");
            }
        } catch (Exception ex) {
            throw new WooshException(" Error with " + machine.getIp());
        }

    }

    @Override
    public String sendPackage(Machine machine) throws WooshException {

        Utils.printLogs("-----------------------STARTED----------------------------");
        Utils.printLogs("DEPLOYMENTLOG FOR: " +machine.getName());
        Utils.printLogs("IP: " + machine.getIp());
        Utils.printLogs("\n\n");
        Utils.printLogs("BASH SCRIPT " + machine.getBashScript() );
        Utils.printLogs("MACHINE COMPRESSED " + machine.getPathCompressed());

        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            session.setPassword(machine.getPassword());
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            // mkdirs(channelSftp, SERVERPATH);
            executeRemoteCommandAsSudo(session, machine, machine.getPassword(), "sudo mkdir -p " + SERVERPATH);
            channelSftp.cd(SERVERPATH);
            executeRemoteCommandAsSudo(session, machine, machine.getPassword(), "sudo chmod -R 777 " + SERVERPATH);
            //giveUserRights(session, channelSftp.pwd(), machine.getPassword());
            File f = new File(machine.getPathCompressed());
            channelSftp.put(new FileInputStream(f), f.getName());
            Scanner scanner = new Scanner(machine.getBashScript());

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Utils.printLogs(line);
                executeRemoteCommandAsSudo(session, machine, machine.getPassword(), line);
            }
            channel.disconnect();
            session.disconnect();



        } catch (JSchException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
        Utils.printLogs("-----------------------DONE-------------------------------");
        Utils.printLogs("\n\n");
        return "Succes";
    }

    private void executeRemoteCommandAsSudo(Session session, Machine machine, String password,
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
            out.write((command + "\n").getBytes());
            out.flush();
            out.write((password + "\n").getBytes());
            out.flush();
            out.close();

            byte[] tmp=new byte[1024];
            while(true){
                while(stdout.available()>0){
                    int i=stdout.read(tmp, 0, 1024);
                    if(i<0)break;
                    Utils.printLogs(new String(tmp, 0, i));
                }
                while(stderr.available()>0){
                    int i=stdout.read(tmp, 0, 1024);
                    if(i<0)break;
                    Utils.printLogs(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    Utils.printLogs("exit-status: "+channel.getExitStatus());
                    break;
                }
                Thread.sleep(500);
            }
        } catch (Exception ex) {
            throw new WooshException("Failure in execting command " + command);
        } finally {
            channel.disconnect();
        }
    }

}
