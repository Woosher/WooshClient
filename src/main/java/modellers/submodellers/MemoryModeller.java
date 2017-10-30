package modellers.submodellers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import modellers.submodellers.interfaces.MapperInterface;

import java.util.List;

public class MemoryModeller implements MapperInterface{


    public MemoryModeller(){

    }

    public Deployment addNodes(List<Node> nodes) throws WooshException {
        return null;
    }

    public void addDeployment(Deployment deployment) throws WooshException {

    }

    public Deployment addLoadBalancers(List<LoadBalancer> loadBalancers) throws WooshException {
        return null;
    }

    public Deployment clearDeployment(Deployment deployment) throws WooshException {
        return null;
    }
}
