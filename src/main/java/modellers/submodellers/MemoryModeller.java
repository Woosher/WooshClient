package modellers.submodellers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import javafx.collections.ObservableList;
import modellers.submodellers.interfaces.MapperInterface;

import java.util.List;

public class MemoryModeller implements MapperInterface{

    public MemoryModeller(){

    }


    @Override
    public void clearDeployment(Deployment deployment) throws WooshException {
        deployment = null;
    }

    @Override
    public void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName) throws WooshException {
        Node node = new Node();
        node.setName(nodeName);
        loadBalancer.getNodes().add(node);
    }

    @Override
    public void deleteNodeFromLoadBalancer(ObservableList<Node> nodes, Node node) throws WooshException {
        nodes.remove(node);
        node = null;
    }

    @Override
    public void addLoadbalancer(Deployment deployment,  String loadBalancerName) throws WooshException {
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.setName(loadBalancerName);
        loadBalancer.setCachingAttributes("/temp/attributes/");
        deployment.getLoadBalancers().add(loadBalancer);
    }

    @Override
    public void deleteLoadBalancer(ObservableList<LoadBalancer> loadBalancers, LoadBalancer loadBalancer) throws WooshException {
        List<Node> nodes = loadBalancer.getNodes();
        nodes.removeAll(nodes);
        loadBalancers.remove(loadBalancer);
        loadBalancer = null;
    }

    @Override
    public void copyInfo(Node source, Node destinationNode) throws WooshException {

    }
}
