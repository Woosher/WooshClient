package subcontrollers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import networking.SSHClient;
import subcontrollers.interfaces.ConnectionControllerInterface;

import java.util.List;

public class ConnectionController implements ConnectionControllerInterface {

    @Override
    public List<String> testConnections(Deployment deployment){
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            System.out.println(SSHClient.testConnection(loadBalancer));
            //Nodes
            for (Node node: loadBalancer.getNodes()) {
                System.out.println(SSHClient.testConnection(node));
            }

        }
        return null;
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
