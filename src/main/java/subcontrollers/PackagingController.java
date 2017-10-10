package subcontrollers;


import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigWriter;
import iohelpers.Scripter;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ScripterInterface;
import iohelpers.interfaces.WriterInterface;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import subcontrollers.interfaces.PackagingInterface;
import tools.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static values.Constants.*;

public class PackagingController implements PackagingInterface {

    private WriterInterface configWriter;
    private ScripterInterface scripter;

    public PackagingController(CheckerInterface configChecker){
        configWriter = new ConfigWriter(configChecker);
        scripter = new Scripter();
    }

    @Override
    public String createNginxScript(LoadBalancer loadBalancer) throws WooshException {
        return scripter.createLoadBalancerScript(loadBalancer);
    }

    @Override
    public String readyDeployment(Deployment deployment) throws WooshException {
        try {
            String path = FULLTEMPPATH + deployment.getName() + "/";
            Utils.generateSimpleFolder(path);
            for(LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
                updateAndSaveLb(loadBalancer, path);
                for(Node node : loadBalancer.getNodes()){
                    updateAndSaveNode(node,path);
                }
            }
        } catch (IOException e) {
            throw new WooshException(e.getMessage());
        }
        return null;
    }

    private void updateAndSaveNode(Node node, String path) throws WooshException {
        String nodePath = path + node.getName() + "/";
        File source = new File(node.getPath());
        File targetDir = null;
        try {
            targetDir = Utils.generateSimpleFolder(nodePath);
            File[] listOfFiles = source.listFiles();
            if(listOfFiles != null){
                for(int i = 0; i<listOfFiles.length; i++){
                    File tempFile = listOfFiles[i];
                    FileUtils.copyFileToDirectory(tempFile,targetDir);
                }
                createNodeBashScript(targetDir.getPath(), node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String compressedPath = scripter.compressPackage(nodePath, path, node.getName());

        Utils.delete(nodePath);
        node.setPathCompressed(compressedPath);

    }

    private void updateAndSaveLb(LoadBalancer loadBalancer, String path) throws WooshException{
        String loadBalancerPath = path  + loadBalancer.getName() + "/";
        String loadBalancerConf = loadBalancerPath + NGINXCONF;
        String content = createNginxScript(loadBalancer);
        String nginxPath = configWriter.saveFile(content,loadBalancerConf);
        String bashScript = createLoadBalancerBashScript(nginxPath);
        loadBalancer.setBashScript(bashScript);
        loadBalancer.setPathCompressed(nginxPath);
    }

    private String createLoadBalancerBashScript(String nginxPath) throws WooshException {
        StringBuilder sb = new StringBuilder();
        sb.append(INSTALLNGINX);
        sb.append("\n");
        sb.append("sudo ").append("mkdir -p ").append(SERVERPATH);
        sb.append("\n");
        sb.append("sudo ").append("cp ").append(SERVERPATH).append("nginx.conf").append(" ").append(NGINXPATH);
        sb.append("\n");
        sb.append(RESTARTNGINX);
        return sb.toString();
    }

    private void createNodeBashScript(String path, Node node) throws WooshException {
        StringBuilder sb = new StringBuilder();
        String content = getBashForEnvironment(path, node);
        sb.append("sudo ").append("mkdir -p ").append(SERVERPATH).append(node.getName()).append("\n");
        sb.append("sudo ").append("tar -xvzf ").append(SERVERPATH).append(node.getName()).append(".tar.gz ").append("-C ").append(SERVERPATH).append(node.getName()).append("\n");
        sb.append(content);
        node.setBashScript(sb.toString());
    }

    private String getBashForEnvironment(String path, Node node){
        switch (node.getEnvironment()){
            case ENVIRONMENT_JAVA:
                return doJavaBashScript(path,node);
            case ENVIRONMENT_CSHARP:
                return null;
            case ENVIRONMENT_PYTHON:
                return null;
            case ENVIRONMENT_RUBY:
                return null;
        }
        return null;
    }

    private String doJavaBashScript(String path, Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(INSTALLJAVA);
        sb.append("\n");
        File[] jarFiles = filterForExtention(path, "jar");
        for(int i = 0; i<jarFiles.length; i++){
            File jarFile = jarFiles[i];
            sb.append("sudo java -jar ").append(SERVERPATH).append(node.getName()).append("/").append(jarFile.getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private File[] filterForExtention(String folderName, String extension){
        File dir = new File(folderName);
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith("." + extension); }
        } );
    }

    public void formatToConfigFile(Deployment deployment, String path) throws WooshException {
        configWriter.saveDeployment(deployment,path);
    }

}
