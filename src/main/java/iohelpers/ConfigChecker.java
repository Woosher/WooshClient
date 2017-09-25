package iohelpers;

import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONObject;

public class ConfigChecker implements CheckerInterface {


    public boolean isValidConfig(JSONObject jsonObject) {
        return false;
    }

    public boolean checkConfigAttributes(JSONObject jsonObject) throws WooshException {
        return false;
    }
}
