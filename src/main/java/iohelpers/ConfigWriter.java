package iohelpers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.interfaces.WriterInterface;
import org.json.JSONObject;

public class ConfigWriter implements WriterInterface{

    private ConfigChecker configChecker;

    public ConfigWriter(ConfigChecker configChecker){
        this.configChecker = configChecker;
    }

    public JSONObject parseDeployment(Deployment deployment) throws WooshException {
        return null;
    }

    public void saveConfig(String config, String path) throws WooshException {

    }

    public boolean checkDeployment(Deployment deployment) throws WooshException {
        return false;
    }
}
