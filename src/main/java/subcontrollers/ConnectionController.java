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

import static values.Constants.TRUSTED;

public class ConnectionController implements ConnectionControllerInterface {

    @Override
    public List<ConnectionInfo> testConnections(Deployment deployment){
        List<ConnectionInfo> list = new ArrayList<>();
        String status = TRUSTED;
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            try {
                SSHClient.testConnection(loadBalancer);
            } catch (WooshException e) {
                status = e.getMessage();
            }
            list.add(new ConnectionInfo(loadBalancer,status));
            //Nodes
            for (Node node: loadBalancer.getNodes()) {
                status = TRUSTED;
                try {
                    SSHClient.testConnection(node);
                } catch (WooshException e) {
                    status = e.getMessage();
                }
                list.add(new ConnectionInfo(node,status));
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
    public String sendPackage(Machine machine){
        try {
            return SSHClient.sendPackage(machine);
        } catch (WooshException e) {
            return e.getMessage();
        }

    }


}
