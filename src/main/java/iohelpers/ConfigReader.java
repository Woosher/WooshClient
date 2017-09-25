package iohelpers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.interfaces.ReaderInterface;
import org.json.JSONObject;

public class ConfigReader implements ReaderInterface {

    private ConfigChecker configChecker;

    public ConfigReader(ConfigChecker configChecker){
        this.configChecker = configChecker;
    }


    public Deployment parseConfig(JSONObject jsonObject) throws WooshException {
        return null;
    }

    public String loadConfig(String path) throws WooshException {
        return null;
    }

    public boolean checkDeployment(Deployment deployment) throws WooshException {
        return false;
    }
}
