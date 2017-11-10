package iohelpers.interfaces;

import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;

import java.util.List;

public interface ScripterInterface {
    String compressPackage(String path, String destinationPath, String archiveName) throws WooshException;

    String createLoadBalancerScript(LoadBalancer lb) throws WooshException;
}
