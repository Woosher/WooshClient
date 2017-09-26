package subcontrollers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import iohelpers.ConfigReader;
import subcontrollers.interfaces.ReaderInterface;

public class ReadController implements ReaderInterface {

    ConfigReader configReader;

    public ReadController(ConfigChecker configChecker){
        configReader = new ConfigReader(configChecker);
    }

    public Deployment readConfigFile(String path) throws WooshException {
        return null;
    }
}
