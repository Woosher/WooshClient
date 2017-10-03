package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigChecker implements CheckerInterface {


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

    }

    private void checkLoadBalancerObject(LoadBalancer loadBalancer) throws WooshException{

    }

    private void checkNodeObject(Node node){

    }

}
