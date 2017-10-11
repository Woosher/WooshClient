package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.WriterInterface;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriter implements WriterInterface{

    private CheckerInterface configChecker;

    public ConfigWriter(CheckerInterface configChecker){
        this.configChecker = configChecker;
    }


    public void saveDeployment(Deployment deployment, String path) throws WooshException {
        configChecker.checkDeploymentObject(deployment);
        JSONObject jsonObject = parseDeployment(deployment);
        String content = jsonObject.toString();
        saveFile(content, path);
    }

    @Override
    public String saveFile(String content, String path) throws WooshException {
        FileWriter fileWriter = null;
        File file = null;
        String newPath = null;
        try {
            file = Utils.generateFile(path);
            FileWriter fooWriter = new FileWriter(file, false);
            fooWriter.write(content);
            fooWriter.close();
            newPath = file.getPath();
        } catch (IOException e) {
            throw new WooshException("Could not create file" );
        }
        return newPath;
    }

    private JSONObject parseDeployment(Deployment deployment) throws WooshException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("deployment_name", deployment.getName());
        jsonObject.put("ssl_path", deployment.getSsl_path());
        for(LoadBalancer loadBalancer : deployment.getLoadBalancers()){
            JSONObject loadBalancerJson = parseLoadBalancer(loadBalancer);
            jsonArray.put(loadBalancerJson);
        }
        jsonObject.put("loadbalancers", jsonArray);
        return jsonObject;
    }

    private JSONObject parseLoadBalancer(LoadBalancer loadBalancer){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("name",loadBalancer.getName());
        jsonObject.put("username",loadBalancer.getUsername());
        jsonObject.put("ip", loadBalancer.getIp());
        jsonObject.put("port", loadBalancer.getPort());
        jsonObject.put("sshport", loadBalancer.getSSHPort());
        jsonObject.put("caching_attributes", loadBalancer.getCachingAttributes());
        jsonObject.put("password", loadBalancer.getPassword());

        for(Node node : loadBalancer.getNodes()){
            JSONObject jsonNodeInfo = parseNode(node);
            JSONObject jsonNode = new JSONObject();
            jsonNode.put("node", jsonNodeInfo);
            jsonArray.put(jsonNode);
        }

        jsonObject.put("nodes", jsonArray);
        return jsonObject;
    }

    private JSONObject parseNode(Node node){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", node.getIp());
        jsonObject.put("port", node.getPort());
        jsonObject.put("sshport", node.getSSHPort());
        jsonObject.put("name", node.getName());
        jsonObject.put("username",node.getUsername());
        jsonObject.put("software_environment", node.getEnvironment());
        jsonObject.put("operating_system", node.getOperatingSystem());
        jsonObject.put("password",node.getPassword());
        jsonObject.put("path", node.getPath());
        return jsonObject;
    }


}
