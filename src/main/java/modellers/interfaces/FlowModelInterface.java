package modellers.interfaces;

import entities.DeploymentPackage;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;

public interface FlowModelInterface {

    void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) ;

    void saveDeployment(Deployment deployment, String path, final ResultsListener<Void> resultsListener) ;

    void clearDeployment(final ResultsListener<Deployment> resultsListener) ;

    void addNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void removeNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void addLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void removeLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void sendPackage(DeploymentPackage deploymentPackage,final ResultsListener<String> resultsListener) ;

    void deploy(final ResultsListener<String> resultsListener);

}
