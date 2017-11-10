package iohelpers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;
import org.json.JSONObject;

public interface ConfigReaderInterface {

    Deployment parseFromJSON(JSONObject jsonObject) throws WooshException;

    String loadConfig(String path) throws WooshException;

}
