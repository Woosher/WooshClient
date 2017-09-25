package networking.Interfaces;

import entities.parsing.Node;
import networking.SSHClient;

import java.util.ArrayList;

public interface SSHManagerInterface {
    ArrayList<SSHClient> getSSHClients();

    ArrayList<SSHClient> getSSHClients(Node[] nodes);
}
