package modellers.submodellers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import exceptions.WooshException;

import java.util.List;

public interface PackagingInterface {

    String createNginxScript(LoadBalancer loadBalancer) throws WooshException;

    void readyDeployment(Deployment deployment) throws WooshException;

    void readyMachines(String name, List<Machine> machines) throws WooshException;

    void formatToConfigFile(Deployment deployment, String path, String password) throws WooshException;

}
