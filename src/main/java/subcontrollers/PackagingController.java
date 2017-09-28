package subcontrollers;


import entities.parsing.Machine;
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

    public String compressPackage(Machine machine) throws WooshException {
        scripter.compressPackage(null, null,null,null);
        return null;
    }

}
