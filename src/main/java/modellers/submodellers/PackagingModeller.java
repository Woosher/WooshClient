package modellers.submodellers;


import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import iohelpers.ConfigWriter;
import iohelpers.Scripter;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ScripterInterface;
import iohelpers.interfaces.WriterInterface;
import org.apache.commons.io.FileUtils;
import modellers.submodellers.interfaces.PackagingInterface;
import tools.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import static values.Constants.*;

public class PackagingModeller implements PackagingInterface {

    private WriterInterface configWriter;
    private ScripterInterface scripter;
    private CheckerInterface configChecker;

    public PackagingModeller(){
        configWriter = new ConfigWriter();
        scripter = new Scripter();
        configChecker = new ConfigChecker();
    }

    @Override
    public String createNginxScript(LoadBalancer loadBalancer) throws WooshException {
        return scripter.createLoadBalancerScript(loadBalancer);
    }

    @Override
    public String readyDeployment(Deployment deployment) throws WooshException {
        try {
            configChecker.checkDeploymentObject(deployment);
            String path = FULLTEMPPATH + deployment.getName() + "/";
            Utils.generateSimpleFolder(path);
            for(Machine machine: deployment.getMachines()) {
                if(machine instanceof LoadBalancer) {
                    updateAndSaveLb((LoadBalancer)machine, path);
                    for (Node node : ((LoadBalancer)machine).getNodes()) {
                        updateAndSaveNode(node, path);
                    }
                }else{
                    updateAndSaveNode((Node)machine,path);
                }
            }
        } catch (Exception e) {
            throw new WooshException(e.getMessage());
        }
        return null;
    }

    private void updateAndSaveNode(Node node, String path) throws WooshException {
        String nodePath = path + node.getName() + "/";
        File source = new File(node.getProgramPath());
        File targetDir = null;
        try {
            targetDir = Utils.generateSimpleFolder(nodePath);
            File[] listOfFiles = source.listFiles();
            if(listOfFiles != null){
                for(int i = 0; i<listOfFiles.length; i++){
                    File tempFile = listOfFiles[i];
                    if(tempFile.isDirectory()){
                        FileUtils.copyDirectoryToDirectory(tempFile,targetDir);
                    }else {
                        FileUtils.copyFileToDirectory(tempFile,targetDir);
                    }
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
        String bashScript = createLoadBalancerBashScript();
        loadBalancer.setBashScript(bashScript);
        loadBalancer.setPathCompressed(nginxPath);
    }

    private String createLoadBalancerBashScript() throws WooshException {
        StringBuilder sb = new StringBuilder();
        sb.append(INSTALLNGINX).append("\n");
        sb.append("sudo ").append("mkdir -p ").append(SERVERPATH).append("\n");
        sb.append("sudo ").append("cp ").append(SERVERPATH).append("nginx.conf").append(" ").append(NGINXPATH).append("\n");
        sb.append(RESTARTNGINX).append("\n");
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
                return doCSharpBashScript(path,node);
            case ENVIRONMENT_PYTHON:
                return doPythonScript(path, node);
            case ENVIRONMENT_RUBY:
                return doRubyBashScript(path,node);
        }
        return null;
    }

    private String doPythonScript(String path, Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(ADD_DEADSNAKES);
        sb.append("\n");
        sb.append(UPDATE);
        sb.append("\n");
        sb.append(INSTALL_PYTHON);
        sb.append("\n");
        File[] rbFiles = filterForExtention(path, "py");
        for(int i = 0; i<rbFiles.length; i++){
            File rbFile = rbFiles[i];
            sb.append(PYTHON).append(SERVERPATH).append(node.getName()).append("/").append(rbFile.getName());
            sb.append("\n");
        }
        return sb.toString();
    }


    private String doRubyBashScript(String path, Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(INSTALL_RUBY);
        sb.append("\n");
        File[] rbFiles = filterForExtention(path, "rb");
        for(int i = 0; i<rbFiles.length; i++){
            File rbFile = rbFiles[i];
            sb.append(RUN).append(SERVERPATH).append(node.getName()).append("/").append(rbFile.getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String doCSharpBashScript(String path, Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(ADD_KEY_SERVER);
        sb.append("\n");
        sb.append(ECHO_MONO_PROJECT);
        sb.append("\n");
        sb.append(UPDATE);
        sb.append("\n");
        sb.append(INSTALL_MONO);
        sb.append("\n");
        File[] exeFiles = filterForExtention(path, "exe");
        for(int i = 0; i<exeFiles.length; i++){
            File exeFile = exeFiles[i];
            sb.append(MONO).append(SERVERPATH).append(node.getName()).append("/").append(exeFile.getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String doJavaBashScript(String path, Node node){
        StringBuilder sb = new StringBuilder();
        sb.append(UPDATE);
        sb.append("\n");
        sb.append(INSTALLJAVA);
        sb.append("\n");
        File[] jarFiles = filterForExtention(path, "jar");
        for(int i = 0; i<jarFiles.length; i++){
            File jarFile = jarFiles[i];
            sb.append("sudo nohup java -jar ").append(SERVERPATH).append(node.getName()).append("/").append(jarFile.getName());
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

    public void formatToConfigFile(Deployment deployment, String path, String password) throws WooshException {
        configWriter.saveDeployment(deployment,path, password);
    }

}
