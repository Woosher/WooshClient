package subcontrollers;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import networking.SSHClient;
import subcontrollers.interfaces.ConnectionControllerInterface;
import tools.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConnectionController implements ConnectionControllerInterface {

    @Override
    public List<ConnectionInfo> testConnections(Deployment deployment){
        List<ConnectionInfo> list = new ArrayList<>();
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            String lb = SSHClient.testConnection(loadBalancer);
            list.add(new ConnectionInfo(loadBalancer,lb));
            //Nodes
            for (Node node: loadBalancer.getNodes()) {
                String n = SSHClient.testConnection(node);
                list.add(new ConnectionInfo(node,n));
            }

        }
        return list;
    }

    @Override
    public void addKnownHosts(List<Machine> macs)throws WooshException{
        for(Machine machine : macs){
            SSHClient.addKnownHost(machine);
        }

    }

    @Override
    public ConnectionInfo sendPackage(Machine machine){
        try {
            return new ConnectionInfo(machine,SSHClient.sendPackage(machine));
        } catch (WooshException e) {
            return new ConnectionInfo(machine,e.getMessage());
        }

    }


}
