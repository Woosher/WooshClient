package subcontrollers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigReader;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONException;
import org.json.JSONObject;
import subcontrollers.interfaces.ReaderInterface;

public class ReadController implements ReaderInterface {

    ConfigReader configReader;

    public ReadController(CheckerInterface configChecker){
        configReader = new ConfigReader(configChecker);
    }

    public Deployment readConfigFile(String path) throws WooshException {
        String configText = configReader.loadConfig(path);
        JSONObject jsonObject= null;
        try{
            /*TODO
            Ryk JSON Parsing ud fra entitetsklasserne
            --- CONFIGCHECKER STUFF --
            1. Smid JSON ind i configchecker
            2. hvis configchecker ikke returnere nogle fejl så parse alt json til et deployment og returner dette
         */
            jsonObject = new JSONObject(configText);
        }catch (JSONException e){
            throw new WooshException("Wrong JSON");
        }
        return Deployment.parseFromJSON(jsonObject);
    }
}
