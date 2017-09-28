package iohelpers.interfaces;

import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;

public interface ScripterInterface {
    DeploymentPackage packNode(Node node);

    DeploymentPackage packLoadBalancer(LoadBalancer lb);

    String compressPackage(String bashPath, String path, String destinationPath, String archiveName) throws WooshException;
}
