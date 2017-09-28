package subcontrollers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigReader;
import iohelpers.interfaces.CheckerInterface;
import subcontrollers.interfaces.ReaderInterface;

public class ReadController implements ReaderInterface {

    ConfigReader configReader;

    public ReadController(CheckerInterface configChecker){
        configReader = new ConfigReader(configChecker);
    }

    public Deployment readConfigFile(String path) throws WooshException {
        String configText = configReader.loadConfig(path);

        return null;
    }
}
