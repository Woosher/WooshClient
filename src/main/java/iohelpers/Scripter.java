package iohelpers;

import entities.DeploymentPackage;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import iohelpers.interfaces.ScripterInterface;

public class Scripter implements ScripterInterface {

    public DeploymentPackage packNode(Node node){
        return null;
    }

    public DeploymentPackage packLoadBalancer(LoadBalancer lb){
        return null;
    }
}
