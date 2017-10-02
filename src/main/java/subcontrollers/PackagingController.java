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
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static values.Constants.FULLTEMPPATH;

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

    private void updateAndSaveNode(Node node, String path) throws IOException {
        String nodePath = path + node.getName() + "/";
        File source = new File(node.getPath());
        File targetDir = Utils.generateSimpleFolder(nodePath);
        File[] listOfFiles = source.listFiles();
        for(int i = 0; i<listOfFiles.length; i++){
            File tempFile = listOfFiles[i];
            FileUtils.copyFileToDirectory(tempFile,targetDir);
        }

    }

    private void updateAndSaveLb(LoadBalancer loadBalancer, String path) throws WooshException{
        String loadBalancerPath = path  + loadBalancer.getName() + "/" ;
        String loadBalancerConf = path  + loadBalancer.getName() + "/" +  "nginx.conf";
        String content = createNginxScript(loadBalancer);
        configWriter.saveConfig(content,loadBalancerConf);
        String compressedPath = scripter.compressPackage(loadBalancerPath, path, loadBalancer.getName());
        Utils.delete(loadBalancerPath);
        loadBalancer.setPathCompressed(compressedPath);
    }

    public void formatToConfigFile(Deployment deployment, String path) throws WooshException {
        JSONObject jsonObject = deployment.parseToJSON();
        String plainText = jsonObject.toString();
        configWriter.saveConfig(plainText,path);
    }

}
