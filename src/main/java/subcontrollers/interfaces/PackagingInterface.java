package subcontrollers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;

import java.util.List;

public interface PackagingInterface {


    String createNginxScript(LoadBalancer loadBalancer) throws WooshException;

    String readyDeployment(Deployment deployment) throws WooshException;

    void formatToConfigFile(Deployment deployment, String path) throws WooshException;

}
