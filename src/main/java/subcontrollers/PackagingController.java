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
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import subcontrollers.interfaces.PackagingInterface;
import tools.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static values.Constants.*;

public class PackagingController implements PackagingInterface {

    private ConfigWriter configWriter;
    private ScripterInterface scripter;

    public PackagingController(CheckerInterface configChecker){
        configWriter = new ConfigWriter(configChecker);
        scripter = new Scripter();
    }

    public String createBashScripts(Machine machine) throws WooshException {
        return null;
    }

    @Override
    public String compressPackage(Machine node) throws WooshException {
        return null; //scripter.compressPackage(node.getPathBash(), node.getPathCompressed(),"/home/toby/packages/" ,node.getName());
    }

    @Override
    public String compressPackage(LoadBalancer loadBalancer) throws WooshException {
        return null; //scripter.compressPackage(loadBalancer.getPathBash(), loadBalancer.getPathBash(),"/home/toby/packages/" ,loadBalancer.getName());

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
            }
            createNodeBashScript(nodePath, node);
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
        configWriter.saveConfig(content,loadBalancerConf);
        createLoadBalancerBashScript(loadBalancerPath, loadBalancerConf, loadBalancer);
        String compressedPath = scripter.compressPackage(loadBalancerPath, path, loadBalancer.getName());
        Utils.delete(loadBalancerPath);
        loadBalancer.setPathCompressed(compressedPath);
    }

    private void createLoadBalancerBashScript(String path, String confPath, LoadBalancer loadBalancer) throws WooshException {
        StringBuilder sb = new StringBuilder();
        File file = new File(confPath);
        String bashScript = path + EXESCRIPT;
        sb.append(BASHSTART);
        sb.append("\n");
        sb.append(INSTALLNGINX);
        sb.append("\n");
        sb.append("sudo ").append("cp ").append(SERVERPATH).append(loadBalancer.getName()).append("/nginx.conf").append(" ").append(NGINXPATH);
        sb.append("\n");
        sb.append(RESTARTNGINX);
        System.out.println(sb.toString());
        configWriter.saveConfig(sb.toString(),bashScript);
    }

    private void createNodeBashScript(String path, Node node) throws WooshException {
        String bashScript = path + EXESCRIPT;
        StringBuilder sb = new StringBuilder();
        sb.append(BASHSTART);
        String content = getBashForEnvironment(path, node);
        configWriter.saveConfig(content,bashScript);
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
            sb.append("sudo java -jar ").append(" ").append(SERVERPATH).append(node.getName()).append("/").append(jarFile.getName());
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
        JSONObject jsonObject = deployment.parseToJSON();
        String plainText = jsonObject.toString();
        configWriter.saveConfig(plainText,path);
    }

}
