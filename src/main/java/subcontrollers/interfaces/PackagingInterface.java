package subcontrollers.interfaces;

import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;

import java.util.List;

public interface PackagingInterface {


    String createBashScripts(Machine machine) throws WooshException;

    String compressPackage(Machine node) throws WooshException;

    String compressPackage(LoadBalancer loadBalancer) throws WooshException;

    String createNginxScript(List<LoadBalancer> loadBalancer) throws WooshException;

}
