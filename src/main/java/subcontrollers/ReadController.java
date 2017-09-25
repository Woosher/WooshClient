package subcontrollers;

import iohelpers.ConfigChecker;
import iohelpers.ConfigReader;

public class ReadController {

    ConfigReader configReader;

    public ReadController(ConfigChecker configChecker){
        configReader = new ConfigReader(configChecker);
    }
}
