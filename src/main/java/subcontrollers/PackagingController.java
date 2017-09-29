package subcontrollers;


import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.Scripter;
import iohelpers.interfaces.ScripterInterface;
import subcontrollers.interfaces.PackagingInterface;

import java.util.List;

public class PackagingController implements PackagingInterface {

    private ScripterInterface scripter;

    public PackagingController(){
        scripter = new Scripter();
    }

    public String createBashScripts(Machine machine) throws WooshException {
        return null;
    }

    @Override
    public String compressPackage(Machine node) throws WooshException {
        return scripter.compressPackage(node.getPathBash(), node.getPathCompressed(),"/home/toby/packages/" ,node.getName());
    }

    @Override
    public String compressPackage(LoadBalancer loadBalancer) throws WooshException {
        return scripter.compressPackage(loadBalancer.getPathBash(), loadBalancer.getPathBash(),"/home/toby/packages/" ,loadBalancer.getName());

    }

    @Override
    public String createNginxScript(List<LoadBalancer> loadBalancer) throws WooshException {
        System.out.println(scripter.createLoadBalancerScript(loadBalancer));
        return null;
    }

}
