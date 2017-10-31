package modellers.submodellers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import javafx.collections.ObservableList;
import tools.WooshLogger;

import java.util.List;

public interface MapperInterface {

    void clearDeployment(Deployment deployment) throws WooshException;

    void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName) throws WooshException;

    void addLoadbalancer(Deployment deployment, String loadBalancer) throws WooshException;

    void deleteMachine(ObservableList<Machine> machines, Machine machine) throws WooshException;

    void addNode(Deployment deployment, String loadBalancer) throws WooshException;

    void deleteNodeFromLoadBalancer(ObservableList<Node> nodes, Node node) throws WooshException;

    void copyInfo(Node source, Node destinationNode) throws WooshException;

}
