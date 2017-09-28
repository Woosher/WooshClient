package iohelpers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.WriterInterface;
import org.json.JSONObject;

public class ConfigWriter implements WriterInterface{

    private CheckerInterface configChecker;

    public ConfigWriter(CheckerInterface configChecker){
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
