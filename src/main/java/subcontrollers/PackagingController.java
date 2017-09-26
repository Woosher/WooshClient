package subcontrollers;


import entities.DeploymentPackage;
import entities.parsing.System;
import exceptions.WooshException;
import iohelpers.Scripter;
import iohelpers.interfaces.ScripterInterface;
import subcontrollers.interfaces.PackagingInterface;

public class PackagingController implements PackagingInterface {

    private ScripterInterface scripter;

    public PackagingController(){
        scripter = new Scripter();
    }

    public DeploymentPackage createPackage(System system) throws WooshException {
        return null;
    }

    public String createBashScripts(System system) throws WooshException {
        return null;
    }

    public String compressPackage(System system) throws WooshException {
        scripter.compressPackage("po");
        return null;
    }

}
