package networking;

import com.jcraft.jsch.*;
import entities.DeploymentPackage;
import entities.parsing.Node;
import exceptions.WooshException;

import java.io.File;
import java.io.FileInputStream;

public class SSHClient implements networking.Interfaces.SSHClientInterface {

    private JSch jsch = null;

    public SSHClient(){
        jsch = new JSch();
    }

    private void setKnownHostFile(JSch jsch) throws WooshException{
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
    public void addKnownHost(Node node) throws WooshException{
        try {
            setKnownHostFile(jsch);
            Session session = jsch.getSession(node.getName(), node.getIp(), node.getPort());
            session.setPassword(node.getPassword());
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            session.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean testConnection(Node node){

        try {
            setKnownHostFile(jsch);
            Session session = jsch.getSession(node.getName(), node.getIp(), node.getPort());
            session.setPassword(node.getPassword());
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

    public void sendPackage(DeploymentPackage pack) throws WooshException{
        try {
            setKnownHostFile(jsch);
            Session session = jsch.getSession(pack.getSystem().getName(), pack.getSystem().getIp(), pack.getSystem().getPort());
            session.setPassword(pack.getSystem().getPassword());

            session.connect();
            System.out.println(session.getHostKey().getKey());

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.cd("/etc/woosh/package/wooshserver/");

            File f = new File(pack.getSystem().getPath());
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