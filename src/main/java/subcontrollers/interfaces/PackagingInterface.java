package subcontrollers.interfaces;

import entities.DeploymentPackage;
import entities.parsing.Machine;
import exceptions.WooshException;

public interface PackagingInterface {

    DeploymentPackage createPackage(Machine machine) throws WooshException;

    String createBashScripts(Machine machine) throws WooshException;

    String compressPackage(Machine machine) throws WooshException;



}
