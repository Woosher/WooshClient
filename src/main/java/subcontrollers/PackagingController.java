package subcontrollers;


import entities.DeploymentPackage;
import entities.parsing.System;
import exceptions.WooshException;
import iohelpers.Scripter;
import subcontrollers.interfaces.PackagingInterface;

public class PackagingController implements PackagingInterface {

    private Scripter scripter;

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
        return null;
    }

}
