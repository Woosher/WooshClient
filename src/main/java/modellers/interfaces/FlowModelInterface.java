package modellers.interfaces;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.collections.ObservableList;

import java.util.List;

public interface FlowModelInterface {

    void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) ;

    void saveDeployment(String path, final ResultsListener<Void> resultsListener) ;

    void clearDeployment(Deployment deployment,final ResultsListener<Deployment> resultsListener) ;

    void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName,final ResultsListener<String> resultsListener) ;

    void removeNodeFromLoadBalancer(ObservableList<Node> nodes, Node node, final ResultsListener<String> resultsListener) ;

    void addLoadBalancerToDeployment(String loadBalancerName,final ResultsListener<String> resultsListener) ;

    void removeLoadBalancerFromDeployment(ObservableList<LoadBalancer> loadBalancers, LoadBalancer loadBalancer,final ResultsListener<String> resultsListener) ;

    void addKnownHosts(List<Machine> macs, final ResultsListener<Boolean> resultsListener);

    void sendPackages(final ResultsListener<List<ConnectionInfo>> resultsListener) ;

    void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener);

    void createNewDeployment(String name, final ResultsListener<Deployment> resultsListener);

}
