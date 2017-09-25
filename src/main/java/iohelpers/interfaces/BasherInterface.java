package iohelpers.interfaces;

import entities.Package;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;

public interface BasherInterface {
    Package packNode(Node node);

    Package packLoadBalancer(LoadBalancer lb);
}
