package iohelpers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;
import org.json.JSONObject;

public interface WriterInterface {

    JSONObject parseDeployment(Deployment deployment) throws WooshException;

    void saveConfig(String config, String path) throws WooshException;

}
