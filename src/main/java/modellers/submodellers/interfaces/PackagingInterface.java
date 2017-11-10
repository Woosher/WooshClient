package modellers.submodellers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import exceptions.WooshException;

public interface PackagingInterface {

    String createNginxScript(LoadBalancer loadBalancer) throws WooshException;

    String readyDeployment(Deployment deployment) throws WooshException;

    void formatToConfigFile(Deployment deployment, String path, String password) throws WooshException;

}
