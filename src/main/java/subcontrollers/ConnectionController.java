package subcontrollers;

import entities.ConnectionInfo;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import networking.SSHClient;
import subcontrollers.interfaces.ConnectionControllerInterface;

import java.util.ArrayList;
import java.util.List;

public class ConnectionController implements ConnectionControllerInterface {

    @Override
    public List<ConnectionInfo> testConnections(Deployment deployment){
        List<ConnectionInfo> list = new ArrayList<>();
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            String lb = SSHClient.testConnection(loadBalancer);
            list.add(new ConnectionInfo(loadBalancer,lb.substring(lb.lastIndexOf(" ")+1)));
            //Nodes
            for (Node node: loadBalancer.getNodes()) {
                String n = SSHClient.testConnection(node);
                list.add(new ConnectionInfo(node,n.substring(n.lastIndexOf(" ")+1)));
            }

        }
        return list;
    }

    @Override
    public void addKnownHost(Machine machine)throws WooshException{
        SSHClient.addKnownHost(machine);
    }

    @Override
    public void sendPackages(Deployment deployment) throws WooshException{
        //LoadBalancers
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
              //  SSHClient.sendPackage(loadBalancer);
                //Nodes
                for (Node node: loadBalancer.getNodes()) {
                    try {
                        SSHClient.sendPackage(node);
                    }catch (WooshException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

}
