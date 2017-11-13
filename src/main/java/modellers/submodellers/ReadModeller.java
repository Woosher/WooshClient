package modellers.submodellers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigReader;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ConfigReaderInterface;
import org.json.JSONException;
import org.json.JSONObject;
import modellers.submodellers.interfaces.ReaderInterface;
import tools.Crypto;


public class ReadModeller implements ReaderInterface {

    ConfigReaderInterface configReader;

    public ReadModeller(){
        configReader = new ConfigReader();
    }

    public Deployment readConfigFile(String path, String password) throws WooshException {
        String configText = configReader.loadConfig(path);
        JSONObject jsonObject= null;
        String decryptedText = null;
        try{
            decryptedText = Crypto.decrypt(password,configText);
        }catch (Exception e){
            throw new WooshException("Wrong password for configuration");
        }
        jsonObject = new JSONObject(decryptedText);
        return configReader.parseFromJSON(jsonObject);
    }
}
