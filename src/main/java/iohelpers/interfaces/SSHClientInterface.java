package iohelpers.interfaces;

import entities.parsing.Machine;
import exceptions.WooshException;

public interface SSHClientInterface {
    void addKnownHost(Machine machine) throws WooshException;

    void testConnection(Machine machine) throws WooshException;

    String sendPackage(Machine machine) throws WooshException;
}
