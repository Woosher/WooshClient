package networking.Interfaces;

import entities.DeploymentPackage;
import entities.parsing.Machine;
import exceptions.WooshException;

public interface SSHClientInterface {
    void addKnownHost(Machine machine) throws WooshException;

    boolean testConnection(Machine machine);

    void sendPackage(DeploymentPackage pack) throws WooshException;
}
