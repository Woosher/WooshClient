package iohelpers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONObject;

public class ConfigChecker implements CheckerInterface {


    @Override
    public void checkDeploymentJSON(JSONObject jsonObject) throws WooshException {

    }

    @Override
    public void checkDeploymentObject(Deployment deployment) throws WooshException {

    }

    private void checkLoadBalancerObject(LoadBalancer loadBalancer) throws WooshException{

    }

    private void checkNodeObject(Node node){

    }

}
