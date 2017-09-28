package subcontrollers;


import entities.parsing.LoadBalancer;
import entities.parsing.Machine;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.Scripter;
import iohelpers.interfaces.ScripterInterface;
import subcontrollers.interfaces.PackagingInterface;

public class PackagingController implements PackagingInterface {

    private ScripterInterface scripter;

    public PackagingController(){
        scripter = new Scripter();
    }

    public String createBashScripts(Machine machine) throws WooshException {
        return null;
    }

    @Override
    public String compressPackage(Node node) throws WooshException {
        scripter.compressPackage(node.getPathBash(), node.getPath(),"/home/Toby/packages/" ,node.getName());
    }

    @Override
    public String compressPackage(LoadBalancer loadBalancer) throws WooshException {
        scripter.compressPackage(loadBalancer.getPathBash(), null,"/home/Toby/packages/" ,loadBalancer.getName());
    }

}
