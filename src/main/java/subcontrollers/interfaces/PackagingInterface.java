package subcontrollers.interfaces;

import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;

public interface PackagingInterface {


    String createBashScripts(Machine machine) throws WooshException;

    String compressPackage(Node node) throws WooshException;

    String compressPackage(LoadBalancer loadBalancer) throws WooshException;

}
