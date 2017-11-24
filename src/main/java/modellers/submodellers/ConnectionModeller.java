package modellers.submodellers;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.SSHClient;
import iohelpers.interfaces.SSHClientInterface;
import modellers.submodellers.interfaces.ConnectionInterface;

import java.util.ArrayList;
import java.util.List;

import static values.Constants.TRUSTED;

public class ConnectionModeller implements ConnectionInterface {

    SSHClientInterface sshClient;

    public ConnectionModeller(){
        sshClient = new SSHClient();
    }

    @Override
    public List<ConnectionInfo> testConnections(Deployment deployment){
        List<ConnectionInfo> list = new ArrayList<>();
        String status;
        for (Machine machine: deployment.getMachines()) {
            status = TRUSTED;
            try {
                sshClient.testConnection(machine);
            } catch (WooshException e) {
                status = e.getMessage();
            }
            list.add(new ConnectionInfo(machine,status));
            //Nodes
            if(machine instanceof LoadBalancer) {
                for (Node node : ((LoadBalancer)machine).getNodes()) {
                    status = TRUSTED;
                    try {
                        sshClient.testConnection(node);
                    } catch (WooshException e) {
                        status = e.getMessage();
                    }
                    list.add(new ConnectionInfo(node, status));
                }
            }
        }
        return list;
    }

    @Override
    public void addKnownHosts(List<Machine> macs)throws WooshException{
        for(Machine machine : macs){
            sshClient.addKnownHost(machine);
        }

    }

    @Override
    public ConnectionInfo sendPackage(Machine machine){
        try {
            return new ConnectionInfo(machine,new SSHClient().sendPackage(machine));
        } catch (WooshException e) {
            return new ConnectionInfo(machine,e.getMessage());
        }

    }


}
