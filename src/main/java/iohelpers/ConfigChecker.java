package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigChecker implements CheckerInterface {


    //Outdated and unused
    @Override
    public void checkDeploymentJSON(JSONObject jsonObject) throws WooshException {
        if(!jsonObject.has("deployment_name")){
            throw new WooshException("Deployment name is missing");
        }
        JSONArray loadBalancersJson = jsonObject.getJSONArray("loadbalancers");
        for(int i = 0; i<loadBalancersJson.length(); i++){
            JSONObject loadBalancerJSON = loadBalancersJson.getJSONObject(i);
            if(!loadBalancerJSON.has("name")){
                throw new WooshException("Name is missing");
            }
            if(!loadBalancerJSON.has("ip")){
                throw new WooshException("IP is missing");
            }
            if(!loadBalancerJSON.has("sshport")){
                throw new WooshException("SSHPort is missing");
            }
            if(!loadBalancerJSON.has("port")){
                throw new WooshException("Port is missing");
            }
            if(!loadBalancerJSON.has("password")){
                throw new WooshException("Password is missing");
            }
            if(!loadBalancerJSON.has("caching_attributes")){
                throw new WooshException("Caching attributes are missing");
            }
            if(!loadBalancerJSON.has("nodes")){
                throw new WooshException("Nodes are missing");
            }
            JSONArray nodesJson = jsonObject.getJSONArray("nodes");
            for(int j = 0; i<nodesJson.length(); j++) {
                JSONObject nodeJson = loadBalancersJson.getJSONObject(i);
                if(!nodeJson.has("name")){
                    throw new WooshException("Name is missing");
                }
                if(!nodeJson.has("ip")){
                    throw new WooshException("IP is missing");
                }
                if(!nodeJson.has("sshport")){
                    throw new WooshException("SSHPort is missing");
                }
                if(!nodeJson.has("port")){
                    throw new WooshException("Port is missing");
                }
                if(!nodeJson.has("password")){
                    throw new WooshException("Password is missing");
                }
                if(!nodeJson.has("software_environment")){
                    throw new WooshException("Software environment is missing");
                }
                if(!nodeJson.has("path")){
                    throw new WooshException("Path is missing");
                }
                if(!nodeJson.has("operating_system")){
                    throw new WooshException("Operating system is missing");
                }
            }
        }
    }

    @Override
    public void checkDeploymentObject(Deployment deployment) throws WooshException {
        checkDeploymentAttributes(deployment);
        checkDeploymentDuplicates(deployment);
    }

    private void checkDeploymentDuplicates(Deployment deployment) throws WooshException{
        /*TODO
            Udbyg så den kan tjekke en node stack også
         */
        Stack<Machine> machines = new Stack<>();
        machines.addAll(deployment.getMachines());
        for(Machine machine: deployment.getMachines()){
            if(machine instanceof LoadBalancer) {
                machines.addAll(((LoadBalancer)machine).getNodes());
            }

        }
        Machine firstMachine = machines.pop();
        checkMachineStack(firstMachine, machines);

    }

    private void checkMachineStack(Machine machine, Stack<Machine> machines) throws WooshException{
        for(Machine tempMachine : machines){
            if(tempMachine.getName().equals(machine.getName())){
                throw new WooshException("You can't have the same name twice in a deployment");
            }
        }
        if(!machines.isEmpty()){
            Machine nextMachine = machines.pop();
            checkMachineStack(nextMachine, machines);
        }

    }

    private void checkDeploymentAttributes(Deployment deployment)throws WooshException{
        String errorMsg = "";
        if(deployment.getName() == null){
            errorMsg += "You have no deployment name \n";
        }
        if(deployment.getSsl_path() == null){
            errorMsg += "You have no SSL path \n";
        }
        if(deployment.getMachines() != null){
            if(deployment.getMachines().isEmpty()){
                errorMsg += "You have no machines \n";
            }else {
                for(Machine machine : deployment.getMachines()){
                    if(machine instanceof LoadBalancer){
                        errorMsg += checkLoadBalancerAttributes((LoadBalancer)machine);
                    }else{
                        errorMsg += checkNodeAttributes((Node)machine);
                    }

                }
            }
        }
        if(!errorMsg.equals("")){
            throw new WooshException(errorMsg);
        }
    }

    private String checkLoadBalancerAttributes(LoadBalancer loadBalancer) throws WooshException{
        String errorMsg = "";
        if(loadBalancer.getCachingAttributes() == null){
            errorMsg += "You have no caching attributes \n";
        }

        if(loadBalancer.getName() != null){
            if(!isVaildName(loadBalancer.getName())){
                errorMsg += "You can only choose a combination of letters and numbers as name for a node\n";
            }
        }else {
            errorMsg += "You are missing a name in a node \n";
        }

        if(loadBalancer.isUseSSHKey()){
            if(loadBalancer.getSshKeyPath() != null){
                if(!isVaildPath(loadBalancer.getSshKeyPath())){
                    errorMsg += "You have a wrong SSH Key path specified in a load balancer \n";
                }
            }
        }



        if(loadBalancer.getUsername() == null || loadBalancer.getUsername().isEmpty()){
            errorMsg += "You have no username in a loadbalancer \n";
        }
        if(loadBalancer.getIp() == null ) {
            errorMsg += "You are missing an ip in a loadbalancer \n";
        }
        if(loadBalancer.getSSHPort() == 0){
            errorMsg += "You are missing a port in a loadbalancer \n";
        }
        if(loadBalancer.getPassword() == null && !loadBalancer.isUseSSHKey()){
            errorMsg += "You are missing a password in a loadbalancer \n";
        }
        if(loadBalancer.getNodes() != null){
            if(loadBalancer.getNodes().isEmpty()){
//                errorMsg += " You are missing nodes behind your loadbalancer \n";
//            }else {
                for(Node node : loadBalancer.getNodes()){
                    errorMsg += checkNodeAttributes(node);
                }
            }
        }
        return errorMsg;
    }

    private String checkNodeAttributes(Node node){
        String errorMsg = "";
        if(node.getEnvironment() == null){
            errorMsg += "You are missing environment in a node  \n";
        }

        if(node.getName() != null){
            if(!isVaildName(node.getName())){
                errorMsg += "You can only choose a combination of letters and numbers as name for a node.\n";
            }
        }else {
            errorMsg += "You are missing a name in a node \n";
        }

        if(node.isUseSSHKey()){
            if(node.getSshKeyPath() != null){
                if(!isVaildPath(node.getSshKeyPath())){
                    errorMsg += "You have a wrong SSH Key path specified in a node \n";
                }
            }
        }

        if(node.getProgramPath() != null){
            if(!isVaildPath(node.getProgramPath())){
                errorMsg += "You have a wrong path specified in a node \n";
            }
        }else {
            errorMsg += "You are missing a path in a node \n";
        }
        if(node.getOperatingSystem() == null){
            errorMsg += "You are missing OS in a node \n";
        }
        if(node.getIp() == null){
            errorMsg += "You are missing an IP in a node \n";
        }
        if(node.getSSHPort() == 0){
            errorMsg += "You are missing a port in a node \n";
        }
        if(node.getPassword() == null && !node.isUseSSHKey()){
            errorMsg += "You are missing a password in a node\n";
        }
        return errorMsg;
    }

    private  boolean isVaildPath(String file) {
        return new File(file).exists();
    }

    private boolean isVaildName(String name){
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(name);
        return !m.find();
    }

}
