package subcontrollers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;

public interface ReaderInterface {

    Deployment readConfigFile(String path) throws WooshException;



}
