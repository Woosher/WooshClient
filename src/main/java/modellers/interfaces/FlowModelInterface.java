package modellers.interfaces;

import entities.ConnectionInfo;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;

import java.util.List;
import java.util.Map;

public interface FlowModelInterface {

    void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) ;

    void saveDeployment(String path, final ResultsListener<Void> resultsListener) ;

    void clearDeployment(final ResultsListener<Deployment> resultsListener) ;

    void addNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void removeNodeToDeployment(Node node,final ResultsListener<String> resultsListener) ;

    void addLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void removeLoadBalancerToDeployment(LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void addKnownHosts(List<Machine> macs, final ResultsListener<Boolean> resultsListener);

    void sendPackages(final ResultsListener<Map<String,String>> resultsListener) ;

    void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener);

    void createNewDeployment(String name, final ResultsListener<Deployment> resultsListener);

}
