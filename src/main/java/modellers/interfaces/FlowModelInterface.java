package modellers.interfaces;

import entities.DeploymentPackage;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;

public interface FlowModelInterface {

    Deployment loadDeployment(String path) throws WooshException;

    void saveDeployment(Deployment deployment) throws WooshException;

    Deployment clearDeployment() throws WooshException;

    void addNodeToDeployment(Node node) throws WooshException;

    void removeNodeToDeployment(Node node) throws WooshException;

    void addLoadBalancerToDeployment(LoadBalancer loadBalancer) throws WooshException;

    void removeLoadBalancerToDeployment(LoadBalancer loadBalancer) throws WooshException;

    void sendPackage(DeploymentPackage deploymentPackage,  ResultsListener<String> resultsListener) ;


}
