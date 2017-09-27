package networking;

import com.jcraft.jsch.*;
import entities.DeploymentPackage;
import entities.parsing.Node;
import entities.parsing.Machine;
import exceptions.WooshException;

import java.io.File;
import java.io.FileInputStream;

public final class SSHClient{



    private static void setKnownHostFile(JSch jsch) throws WooshException{
        try {
            File file = new File(System.getProperty("user.home") + "\\.ssh\\known_hosts");
            if (!file.isFile()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                file.createNewFile();
            }
            jsch.setKnownHosts(System.getProperty("user.home") + "\\.ssh\\known_hosts");
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

    public static boolean testConnection(Machine machine){

        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(machine.getName(), machine.getIp(), machine.getPort());
            session.setPassword(machine.getPassword());
            session.connect();
            session.disconnect();
            return true;

        }catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void sendPackage(DeploymentPackage pack) throws WooshException{
        try {
            JSch jsch = new JSch();
            setKnownHostFile(jsch);
            Session session = jsch.getSession(pack.getMachine().getName(), pack.getMachine().getIp(), pack.getMachine().getPort());
            session.setPassword(pack.getMachine().getPassword());

            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd("/etc/woosh/package/wooshserver/");

            File f = new File(pack.getMachine().getPath());
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
    }


}
