package networking;

import com.jcraft.jsch.*;
import entities.parsing.Machine;
import exceptions.WooshException;
import tools.Utils;

import java.io.File;
import java.io.FileInputStream;

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
            Session session = jsch.getSession(machine.getName(), machine.getIp(), machine.getPort());
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
            Session session = jsch.getSession(machine.getName(), machine.getIp(), machine.getPort());
            session.setPassword(machine.getPassword());
            session.connect();
            session.disconnect();
            return "Succes!";
        }catch (JSchException e) {
            return e.getMessage();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "Error with " + machine.getIp();
        }

    }

    public static void sendPackage(Machine machine) throws WooshException{
        System.out.println(machine.getName());
        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getName(), machine.getIp(), machine.getPort());
            session.setPassword(machine.getPassword());

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SERVERPATH);

            File f = new File(machine.getPathCompressed());
            channelSftp.put(new FileInputStream(f), f.getName());
            System.out.println(machine.getName());
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


}
