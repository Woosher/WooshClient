package modellers.submodellers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;

import java.util.List;

public interface MapperInterface {

    Deployment addNodes(List<Node> nodes) throws WooshException;

    void addDeployment(Deployment deployment) throws WooshException;

    Deployment addLoadBalancers(List<LoadBalancer> loadBalancers) throws WooshException;


    Deployment clearDeployment(Deployment deployment) throws WooshException;

}
