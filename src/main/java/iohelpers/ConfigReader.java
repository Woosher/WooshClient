package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
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


    public ConfigReader(){
    }

    public Deployment parseFromJSON(JSONObject jsonObject){
        Deployment deployment = new Deployment();
        List<Machine> machines = new ArrayList<>();
        deployment.setName(jsonObject.getString("deployment_name"));
        deployment.setSsl_path(jsonObject.getString("ssl_path"));
        JSONArray machinesJson = jsonObject.getJSONArray("machines");
        for(int i = 0; i<machinesJson.length(); i++){
            JSONObject machineJSON = machinesJson.getJSONObject(i);
            if(machineJSON.getString("type").equals("loadbalancer")){
                LoadBalancer loadBalancer = parseLoadBalancerFromJSON(machineJSON);
                machines.add(loadBalancer);
            }else{
                Node node = parseNodeFromJSON(machineJSON);
                machines.add(node);
            }
        }
        deployment.setMachines(machines);

        return deployment;
    }

    private LoadBalancer parseLoadBalancerFromJSON(JSONObject jsonObject){
        LoadBalancer loadBalancer = new LoadBalancer();
        List<Node> nodes = new ArrayList<Node>();
        if(jsonObject.has("caching_attributes")) loadBalancer.setCachingAttributes(jsonObject.getString("caching_attributes"));
        parseMachineInfo(jsonObject, loadBalancer);
        if(jsonObject.has("nodes")){
            JSONArray JSONnodes = jsonObject.getJSONArray("nodes");
            for(int i = 0; i<JSONnodes.length(); i++){
                JSONObject JSONnode = JSONnodes.getJSONObject(i);
                Node node = parseNodeFromJSON(JSONnode);
                nodes.add(node);
            }
            loadBalancer.setNodes(nodes);
        }

        return loadBalancer;
    }

    private void parseMachineInfo(JSONObject jsonObject, Machine machine){
        if(jsonObject.has("ip"))  machine.setIp(jsonObject.getString("ip"));
        if(jsonObject.has("port"))  machine.setPort(jsonObject.getInt("port"));
        if(jsonObject.has("sshport"))  machine.setSSHPort(jsonObject.getInt("sshport"));
        if(jsonObject.has("name"))  machine.setName(jsonObject.getString("name"));
        if(jsonObject.has("username"))  machine.setUsername(jsonObject.getString("username"));
        if(jsonObject.has("password"))  machine.setPassword(jsonObject.getString("password"));
        if(jsonObject.has("sshkeypath"))  machine.setSshKeyPath(jsonObject.getString("sshkeypath"));
        if(jsonObject.has("useSSHKey")) machine.setUseSSHKey(jsonObject.getBoolean("useSSHKey"));
        if(jsonObject.has("useCustomScript")) machine.setUseCustomScript(jsonObject.getBoolean("useCustomScript"));
        if(jsonObject.has("customScriptPath")) machine.setCustomScriptPath(jsonObject.getString("customScriptPath"));
    }

    private Node parseNodeFromJSON(JSONObject jsonObject){
        Node node = new Node();
        if(jsonObject.has("path"))  node.setProgramPath(jsonObject.getString("path"));
        if(jsonObject.has("software_environment"))  node.setEnvironment(jsonObject.getString("software_environment"));
        if(jsonObject.has("operating_system"))  node.setOperatingSystem(jsonObject.getString("operating_system"));
        parseMachineInfo(jsonObject, node);
        return node;
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
        return everything;
    }

}
