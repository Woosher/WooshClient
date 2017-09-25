package networking.Interfaces;

import entities.Package;
import entities.parsing.Node;

public interface SSHClientInterface {
    void addKnownHost(Node node);

    void testConnection(Node node);

    void sendPackage(Package pack);
}
