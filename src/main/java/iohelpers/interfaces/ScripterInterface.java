package iohelpers.interfaces;

import entities.DeploymentPackage;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;

public interface ScripterInterface {
    DeploymentPackage packNode(Node node);

    DeploymentPackage packLoadBalancer(LoadBalancer lb);
}
