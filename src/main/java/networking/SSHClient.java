package networking;

import com.jcraft.jsch.*;
import entities.parsing.Machine;
import exceptions.WooshException;
import tools.Utils;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import static values.Constants.SERVERPATH;

public final class SSHClient {

    private static boolean shouldPrintLog = true;

    private static void setKnownHostFile(JSch jsch) throws WooshException {
        try {
            File file = Utils.generateFile(System.getProperty("user.home") + "\\.ssh\\known_hosts");
            jsch.setKnownHosts(file.getAbsolutePath());
        } catch (Exception e) {
            throw new WooshException(e.getMessage());
        }
    }

    public static void addKnownHost(Machine machine) throws WooshException {
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
            e.printStackTrace();
        }
    }

    public static String testConnection(Machine machine) {

        try {
            JSch jsch = new JSch();
            try {
                setKnownHostFile(jsch);
            } catch (WooshException e) {
                e.printStackTrace();
            }
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            session.setPassword(machine.getPassword());
            session.connect();
            session.disconnect();
            return " Succes!";
        } catch (JSchException e) {
            System.out.println(e.getMessage());
            if (e.getMessage().contains("UnknownHostKey")) {
                return " Unknown host, fingerprint: " + e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1);
            } else if (e.getMessage().contains("Connection refused")) {
                return " Connection refused!";
            }
            return " Failed";
        } catch (Exception ex) {
            ex.printStackTrace();
            return " Error with " + machine.getIp();
        }

    }

    public static void sendPackage(Machine machine) throws WooshException {

        printLogs(machine.getName() + " ---- STARTED \t  ---------------------------------------------------");
        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            session.setPassword(machine.getPassword());
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            mkdirs(channelSftp, SERVERPATH);
            channelSftp.cd(SERVERPATH);
            giveUserRights(session, channelSftp.pwd(), machine.getPassword());
            File f = new File(machine.getPathCompressed());
            channelSftp.put(new FileInputStream(f), f.getName());
            Scanner scanner = new Scanner(machine.getBashScript());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                executeRemoteCommandAsSudo(machine, machine.getPassword(), line);
            }
            channel.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
            throw new WooshException(e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new WooshException(ex.getMessage());
        }
        printLogs(machine.getName() + " ---- DONE \t  ---------------------------------------------------");

    }

    public static void giveUserRights(Session session, String path, String sudo_pass) throws JSchException, IOException {
        String command = "sudo chmod -R 777 " + path;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        InputStream in = channel.getInputStream();
        OutputStream out = channel.getOutputStream();
        ((ChannelExec) channel).setErrStream(System.err);

        channel.connect();

        out.write((sudo_pass + "\n").getBytes());
        out.flush();
        channel.disconnect();
    }


    public static void mkdirs(ChannelSftp ch, String path) {
        try {
            String[] folders = path.split("/");
            if (folders[0].isEmpty()) folders[0] = "/";
            String fullPath = folders[0];
            for (int i = 1; i < folders.length; i++) {
                Vector ls = ch.ls(fullPath);
                boolean isExist = false;
                for (Object o : ls) {
                    if (o instanceof ChannelSftp.LsEntry) {
                        ChannelSftp.LsEntry e = (ChannelSftp.LsEntry) o;
                        if (e.getAttrs().isDir() && e.getFilename().equals(folders[i])) {
                            isExist = true;
                        }
                    }
                }
                if (!isExist && !folders[i].isEmpty()) {
                    ch.mkdir(fullPath + folders[i]);
                }
                fullPath = fullPath + folders[i] + "/";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeRemoteCommandAsSudo(Machine machine, String password,
                                                   String command) throws WooshException {
        Session session = null;
        Channel channel = null;
        StringBuffer result = new StringBuffer();
        try {
            session = getSession(machine);
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
                    printLogs(new String(tmp, 0, i));
                }
                while(stderr.available()>0){
                    int i=stdout.read(tmp, 0, 1024);
                    if(i<0)break;
                    printLogs(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    printLogs("exit-status: "+channel.getExitStatus());
                    break;
                }
               Thread.sleep(500);
            }
        } catch (Exception ex) {
            throw new WooshException("Failure in exeuctin command " + command);
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    private static void printLogs(String text){
        if(shouldPrintLog)
        System.out.println(text);
    }


    private static Session getSession(Machine machine) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
        session.setPassword(machine.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        return session;
    }


}
