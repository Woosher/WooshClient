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
    public void testConnections(Deployment deployment){

    }

    @Override
    public void addKnownHosts(Deployment deployment){
        //LoadBalancers
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            try {
                SSHClient.addKnownHost(loadBalancer);
                //Nodes
                for (Machine node: loadBalancer.getNodes()) {
                    try {
                        SSHClient.addKnownHost(node);
                    }catch (WooshException e) {
                        e.printStackTrace();
                    }
                }
            } catch (WooshException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void sendPackages(Deployment deployment) throws WooshException{
        //LoadBalancers
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
              //  SSHClient.sendPackage(loadBalancer);
                //Nodes
                for (Machine node: loadBalancer.getNodes()) {
                    try {
                        SSHClient.sendPackage(node);
                    }catch (WooshException e) {
                        e.printStackTrace();
                    }
                }

        }
    }

}
