package modellers.interfaces;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import javafx.collections.ObservableList;

import java.util.List;

public interface FlowModelInterface {

    void loadDeployment(String path, String load, final ResultsListener<Deployment> resultsListener) ;

    void saveDeployment(String path, String password, final ResultsListener<Void> resultsListener) ;

    void clearDeployment(final ResultsListener<Deployment> resultsListener) ;

    void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName,final ResultsListener<String> resultsListener) ;

    void removeNodeFromLoadBalancer(ObservableList<Node> nodes, Node node, final ResultsListener<String> resultsListener) ;

    void addLoadBalancerToDeployment(String loadBalancerName,final ResultsListener<String> resultsListener) ;

    void addNodeToDeployment(String loadBalancerName,final ResultsListener<String> resultsListener);

    void removeMachineFromDeployment(ObservableList<Machine> machines, Machine machine, final ResultsListener<String> resultsListener) ;

    void addKnownHosts(List<Machine> macs, final ResultsListener<Boolean> resultsListener);

    void sendAllPackages(final ResultsListener<List<ConnectionInfo>> resultsListener) ;

    void sendPackages(List<Machine> macs, final ResultsListener<List<ConnectionInfo>> resultsListener) ;

    void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener);

    void createNewDeployment(String name, final ResultsListener<Deployment> resultsListener);

}
