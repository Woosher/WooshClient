package subcontrollers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import networking.SSHClient;
import subcontrollers.interfaces.ConnectionControllerInterface;

public class ConnectionController implements ConnectionControllerInterface {

    @Override
    public String testConnections(Deployment deployment){
        
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
