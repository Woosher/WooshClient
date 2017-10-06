package networking;

import com.jcraft.jsch.*;
import entities.parsing.Machine;
import exceptions.WooshException;
import tools.Utils;

import java.io.*;
import java.util.Vector;

import static values.Constants.SERVERPATH;

public final class SSHClient{

    private static void setKnownHostFile(JSch jsch) throws WooshException{
        try {
            File file = Utils.generateFile(System.getProperty("user.home") + "\\.ssh\\known_hosts");
            jsch.setKnownHosts(file.getAbsolutePath());
        }catch(Exception e){
            throw new WooshException(e.getMessage());
        }
    }

    public static void addKnownHost(Machine machine) throws WooshException{
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String testConnection(Machine machine){

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
        }catch (JSchException e) {
            System.out.println(e.getMessage());
            if(e.getMessage().contains("UnknownHostKey")){
                return " Unknown host, fingerprint: " + e.getMessage().substring(e.getMessage().lastIndexOf(" ")+1);
            }else if(e.getMessage().contains("Connection refused")){
                return " Connection refused!";
            }
            return " Failed";
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return " Error with " + machine.getIp();
        }

    }

    public static void sendPackage(Machine machine) throws WooshException{
        System.out.println(machine.getName());
        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getUsername(), machine.getIp(), machine.getSSHPort());
            session.setPassword(machine.getPassword());

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            mkdirs(session, SERVERPATH, machine.getPassword());
            channelSftp.cd(SERVERPATH);
            giveUserRights(session, channelSftp.pwd(), machine.getPassword());
            System.out.println("user rights done");
            File f = new File(machine.getPathCompressed());
            channelSftp.put(new FileInputStream(f), f.getName());
            channel.disconnect();
            session.disconnect();
        }catch (JSchException e) {
            e.printStackTrace();
            throw new WooshException(e.getMessage());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new WooshException(ex.getMessage());
        }
        System.out.println(machine.getName() + " DONE");

    }

    public static void giveUserRights(Session session, String path, String sudo_pass) throws JSchException, IOException {
        String command="sudo chmod -R 777 " + path;
        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        InputStream in=channel.getInputStream();
        OutputStream out=channel.getOutputStream();
        ((ChannelExec)channel).setErrStream(System.err);

        channel.connect();

        out.write((sudo_pass+"\n").getBytes());
        out.flush();
        channel.disconnect();
    }



    public static void mkdirs(Session session, String path, String sudo_pass) throws JSchException, IOException {
        String command="sudo mkdir -p " + path;
        Channel channel=session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);

        InputStream in=channel.getInputStream();
        OutputStream out=channel.getOutputStream();
        ((ChannelExec)channel).setErrStream(System.err);

        channel.connect();

        out.write((sudo_pass+"\n").getBytes());
        out.flush();
        channel.disconnect();
    }


}
