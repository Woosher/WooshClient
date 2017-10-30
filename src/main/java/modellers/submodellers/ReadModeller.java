package modellers.submodellers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigReader;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONException;
import org.json.JSONObject;
import modellers.submodellers.interfaces.ReaderInterface;

public class ReadModeller implements ReaderInterface {

    ConfigReader configReader;

    public ReadModeller(CheckerInterface configChecker){
        configReader = new ConfigReader(configChecker);
    }

    public Deployment readConfigFile(String path) throws WooshException {
        String configText = configReader.loadConfig(path);
        JSONObject jsonObject= null;
        try{

            jsonObject = new JSONObject(configText);
        }catch (JSONException e){
            throw new WooshException("Wrong JSON");
        }
        return configReader.parseFromJSON(jsonObject);
    }
}
