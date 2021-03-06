package modellers.submodellers.interfaces;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.Machine;
import exceptions.WooshException;

import java.util.List;

public interface ConnectionInterface {
    List<ConnectionInfo> testConnections(Deployment deployment) throws WooshException;

    void addKnownHosts(List<Machine> macs) throws WooshException;

    ConnectionInfo sendPackage(Machine machine);
}
