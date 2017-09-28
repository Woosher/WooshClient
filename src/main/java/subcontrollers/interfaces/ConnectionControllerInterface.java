package subcontrollers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;

public interface ConnectionControllerInterface {
    void testConnections(Deployment deployment) throws WooshException;

    void addKnownHosts(Deployment deployment) throws WooshException;

    void sendPackages(Deployment deployment) throws WooshException;
}
