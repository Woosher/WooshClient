package iohelpers.interfaces;

import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;

import java.util.List;

public interface ScripterInterface {
    Machine packNode(Node node);

    Machine packLoadBalancer(LoadBalancer lb);

    String compressPackage(String bashPath, String path, String destinationPath, String archiveName) throws WooshException;

    String createLoadBalancerScript(List<LoadBalancer> lb) throws WooshException;


}
