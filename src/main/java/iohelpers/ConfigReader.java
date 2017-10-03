package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ConfigReaderInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader implements ConfigReaderInterface {

    private CheckerInterface configChecker;

    public ConfigReader(CheckerInterface configChecker){
        this.configChecker = configChecker;
    }

    public Deployment parseFromJSON(JSONObject jsonObject){
        Deployment deployment = new Deployment();
        List<LoadBalancer> loadBalancers = new ArrayList<LoadBalancer>();
        deployment.setName(jsonObject.getString("deployment_name"));
        deployment.setSsl_path(jsonObject.getString("ssl_path"));
        JSONArray loadBalancersJson = jsonObject.getJSONArray("loadbalancers");
        for(int i = 0; i<loadBalancersJson.length(); i++){
            JSONObject loadBalancerJSON = loadBalancersJson.getJSONObject(i);
            LoadBalancer loadBalancer = parseLoadBalancerFromJSON(loadBalancerJSON);
            loadBalancers.add(loadBalancer);
        }
        deployment.setLoadBalancers(loadBalancers);

        return deployment;
    }

    private LoadBalancer parseLoadBalancerFromJSON(JSONObject jsonObject){
        LoadBalancer loadBalancer = new LoadBalancer();
        List<Node> nodes = new ArrayList<Node>();
        loadBalancer.setName(jsonObject.getString("name"));
        loadBalancer.setUsername(jsonObject.getString("username"));
        loadBalancer.setIp(jsonObject.getString("ip"));
        loadBalancer.setPort(jsonObject.getInt("port"));
        loadBalancer.setCachingAttributes(jsonObject.getString("caching_attributes"));
        loadBalancer.setPassword(jsonObject.getString("password"));
        JSONArray JSONnodes = jsonObject.getJSONArray("nodes");
        for(int i = 0; i<JSONnodes.length(); i++){
            JSONObject JSONnode = JSONnodes.getJSONObject(i);
            Node node = parseNodeFromJSON(JSONnode.getJSONObject("node"));
            nodes.add(node);
        }
        loadBalancer.setNodes(nodes);
        return loadBalancer;
    }

    private Node parseNodeFromJSON(JSONObject jsonObject){
        Node node = new Node();
        node.setIp(jsonObject.getString("ip"));
        node.setPort(jsonObject.getInt("port"));
        node.setName(jsonObject.getString("name"));
        node.setUsername(jsonObject.getString("username"));
        node.setEnvironment(jsonObject.getString("software_environment"));
        node.setOperatingSystem(jsonObject.getString("operating_system"));
        node.setPassword(jsonObject.getString("password"));
        node.setPath(jsonObject.getString("path"));
        return node;
    }



    public Deployment parseConfig(JSONObject jsonObject) throws WooshException {
        return null;
    }

    public String loadConfig(String path) throws WooshException {
        String everything = null;
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            throw new WooshException("Could not find file: " + path);
        } catch (IOException e) {
            throw new WooshException("Could not open file: " + path);
        }

           /*TODO
            Ryk JSON Parsing ud fra entitetsklasserne
            --- CONFIGCHECKER STUFF --
            1. Smid JSON ind i configchecker
            2. hvis configchecker ikke returnere nogle fejl så parse alt json til et deployment og returner dette
         */

        return everything;
    }

    public boolean checkDeployment(Deployment deployment) throws WooshException {
        return false;
    }
}
