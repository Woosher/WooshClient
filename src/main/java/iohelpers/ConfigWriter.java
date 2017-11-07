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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ConfigWriter implements WriterInterface {

    private CheckerInterface configChecker;

    public ConfigWriter() {
        this.configChecker = new ConfigChecker();
    }


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
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("type", "loadbalancer");
        jsonObject.put("name", loadBalancer.getName());
        jsonObject.put("username", loadBalancer.getUsername());
        jsonObject.put("ip", loadBalancer.getIp());
        jsonObject.put("port", loadBalancer.getPort());
        jsonObject.put("sshport", loadBalancer.getSSHPort());
        jsonObject.put("caching_attributes", loadBalancer.getCachingAttributes());
        jsonObject.put("password", loadBalancer.getPassword());
        jsonObject.put("sshkeypath", loadBalancer.getSshKeyPath());
        jsonObject.put("useSSHKey", loadBalancer.isUseSSHKey());

        for (Node node : loadBalancer.getNodes()) {
            JSONObject jsonNodeInfo = parseNode(node);
            jsonArray.put(jsonNodeInfo);
        }

        jsonObject.put("nodes", jsonArray);
        return jsonObject;
    }

    private JSONObject parseNode(Node node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "node");
        jsonObject.put("ip", node.getIp());
        jsonObject.put("port", node.getPort());
        jsonObject.put("sshport", node.getSSHPort());
        jsonObject.put("name", node.getName());
        jsonObject.put("username", node.getUsername());
        jsonObject.put("software_environment", node.getEnvironment());
        jsonObject.put("operating_system", node.getOperatingSystem());
        jsonObject.put("password", node.getPassword());
        jsonObject.put("path", node.getPath());
        jsonObject.put("sshkeypath", node.getSshKeyPath());
        jsonObject.put("useSSHKey", node.isUseSSHKey());
        return jsonObject;
    }


}
