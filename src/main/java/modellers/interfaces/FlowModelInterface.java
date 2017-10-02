package modellers.interfaces;

import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;

public interface FlowModelInterface {

    void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) ;

    void saveDeployment(String path, final ResultsListener<Void> resultsListener) ;

    void clearDeployment(final ResultsListener<Deployment> resultsListener) ;

    void addNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void removeNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void addLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void removeLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void sendPackages(final ResultsListener<String> resultsListener) ;

    void testConnections(final ResultsListener<String> resultsListener) ;

}
