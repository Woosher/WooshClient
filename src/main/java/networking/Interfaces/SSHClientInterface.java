package networking.Interfaces;

import entities.Package;
import entities.parsing.Node;

public interface SSHClientInterface {
    void addKnownHost(Node node);

    boolean testConnection(Node node);

    void sendPackage(Package pack);
}
