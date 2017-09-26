package networking.Interfaces;

import entities.DeploymentPackage;
import entities.parsing.Node;
import exceptions.WooshException;

public interface SSHClientInterface {
    void addKnownHost(Node node) throws WooshException;

    boolean testConnection(Node node);

    void sendPackage(DeploymentPackage pack) throws WooshException;
}
