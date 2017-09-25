package iohelpers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;
import org.json.JSONObject;

public interface CheckerInterface {

    boolean isValidConfig(JSONObject jsonObject);

    boolean checkConfigAttributes(JSONObject jsonObject) throws WooshException;


}
