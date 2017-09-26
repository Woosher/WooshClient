package iohelpers.interfaces;

import entities.DeploymentPackage;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;

public interface ScripterInterface {
    DeploymentPackage packNode(Node node);

    DeploymentPackage packLoadBalancer(LoadBalancer lb);

    String compressPackage(String path, String destinationPath, String archiveName) throws WooshException;
}
