package subcontrollers.interfaces;

import entities.parsing.Machine;
import exceptions.WooshException;

public interface PackagingInterface {


    String createBashScripts(Machine machine) throws WooshException;

    String compressPackage(Machine machine) throws WooshException;

}
