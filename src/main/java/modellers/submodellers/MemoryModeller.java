package modellers.submodellers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import javafx.collections.ObservableList;
import modellers.submodellers.interfaces.MapperInterface;

import java.util.List;

public class MemoryModeller implements MapperInterface{

    Deployment deployment;

    public MemoryModeller(){

    }

    @Override
    public Deployment getDeployment() {
        return deployment;
    }

    @Override
    public void setDeployment(Deployment deployment) {
        this.deployment = deployment;
    }

    @Override
    public void clearDeployment() throws WooshException {
        this.deployment = null;
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
    }

    @Override
    public void addLoadbalancer( String loadBalancerName) throws WooshException {
        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.setName(loadBalancerName);
        loadBalancer.setCachingAttributes("/temp/attributes/");
        deployment.getMachines().add(loadBalancer);
    }

    @Override
    public void addNode(String nodeName) throws WooshException {
        Node node = new Node();
        node.setName(nodeName);
        this.deployment.getMachines().add(node);
    }

    @Override
    public void deleteMachine(ObservableList<Machine> machines, Machine machine) throws WooshException {
        if(machine instanceof LoadBalancer){
            List<Node> nodes = ((LoadBalancer)machine).getNodes();
            nodes.removeAll(nodes);
        }
        machines.remove(machine);
    }

    @Override
    public void copyInfo(Node source, Node destinationNode) throws WooshException {

    }

    @Override
    public Deployment createNewDeployment(String name) throws WooshException {
        this.deployment = new Deployment();
        this.deployment.setName(name);
        this.deployment.setSsl_path("/test/path/");
        return this.deployment;
    }
}
