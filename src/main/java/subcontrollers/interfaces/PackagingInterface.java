package subcontrollers.interfaces;

import entities.DeploymentPackage;
import entities.parsing.System;
import exceptions.WooshException;

public interface PackagingInterface {

    DeploymentPackage createPackage(System system) throws WooshException;

    String createBashScripts(System system) throws WooshException;

    String compressPackage(System system) throws WooshException;



}
