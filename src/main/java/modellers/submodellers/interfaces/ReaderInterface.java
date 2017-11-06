package modellers.submodellers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;

public interface ReaderInterface {

    Deployment readConfigFile(String path, String password) throws WooshException;



}
