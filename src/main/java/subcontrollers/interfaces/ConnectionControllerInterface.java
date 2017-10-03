package subcontrollers.interfaces;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.Machine;
import exceptions.WooshException;

import java.util.List;

public interface ConnectionControllerInterface {
    List<ConnectionInfo> testConnections(Deployment deployment) throws WooshException;

    void addKnownHost(Machine machine) throws WooshException;

    void sendPackages(Deployment deployment) throws WooshException;
}
