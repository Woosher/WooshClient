package networking.Interfaces;

import entities.DeploymentPackage;
import entities.parsing.Node;

public interface SSHClientInterface {
    void addKnownHost(Node node);

    void testConnection(Node node);

    void sendPackage(DeploymentPackage pack);
}
