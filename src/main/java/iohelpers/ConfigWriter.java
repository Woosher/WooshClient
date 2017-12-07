package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.WriterInterface;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.Crypto;
import tools.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriter implements WriterInterface {


    public ConfigWriter() {

    }


    @Override
    public void saveDeployment(Deployment deployment, String path, String password) throws WooshException {
        JSONObject jsonObject = parseDeployment(deployment);
        String content = jsonObject.toString();
        try {
            String encrypt = Crypto.encrypt(password, content);
            saveFile(encrypt, path);
        } catch (Exception e) {
            throw new WooshException(e.getMessage());
        }
    }

    @Override
    public String saveFile(String content, String path) throws WooshException {
        FileWriter fileWriter = null;
        File file = null;
        String newPath = null;
        try {
            file = Utils.generateFile(path);
            FileUtils.writeStringToFile(file, content, "UTF-8");
            newPath = file.getPath();
        } catch (IOException e) {
            throw new WooshException("Could not create file");
        }
        return newPath;
    }

    private JSONObject parseDeployment(Deployment deployment) throws WooshException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("deployment_name", deployment.getName());
        jsonObject.put("ssl_path", deployment.getSsl_path());
        for (Machine machine : deployment.getMachines()) {
            if (machine instanceof LoadBalancer) {
                JSONObject loadBalancerJson = parseLoadBalancer((LoadBalancer) machine);
                jsonArray.put(loadBalancerJson);
            } else {
                JSONObject nodeJson = parseNode((Node) machine);
                jsonArray.put(nodeJson);
            }

        }
        jsonObject.put("machines", jsonArray);
        return jsonObject;
    }

    private JSONObject parseLoadBalancer(LoadBalancer loadBalancer) {
        JSONObject jsonObject = parseMachineJson(loadBalancer);
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("type", "loadbalancer");
        jsonObject.put("caching_attributes", loadBalancer.getCachingAttributes());

        for (Node node : loadBalancer.getNodes()) {
            JSONObject jsonNodeInfo = parseNode(node);
            jsonArray.put(jsonNodeInfo);
        }

        jsonObject.put("nodes", jsonArray);
        return jsonObject;
    }

    private JSONObject parseMachineJson(Machine machine){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", machine.getIp());
        jsonObject.put("port", machine.getPort());
        jsonObject.put("sshport", machine.getSSHPort());
        jsonObject.put("name", machine.getName());
        jsonObject.put("username", machine.getUsername());
        jsonObject.put("password", machine.getPassword());
        jsonObject.put("sshkeypath", machine.getSshKeyPath());
        jsonObject.put("useSSHKey", machine.isUseSSHKey());
        jsonObject.put("useCustomScript", machine.isUseCustomScript());
        jsonObject.put("customScriptPath", machine.getCustomScriptPath());
        return jsonObject;


    }

    private JSONObject parseNode(Node node) {
        JSONObject jsonObject = parseMachineJson(node);
        jsonObject.put("type", "node");
        jsonObject.put("software_environment", node.getEnvironment());
        jsonObject.put("operating_system", node.getOperatingSystem());
        jsonObject.put("path", node.getProgramPath());

        return jsonObject;
    }


}
