package modellers.submodellers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigReader;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONException;
import org.json.JSONObject;
import modellers.submodellers.interfaces.ReaderInterface;
import tools.Crypto;


public class ReadModeller implements ReaderInterface {

    ConfigReader configReader;

    public ReadModeller(){
        configReader = new ConfigReader();
    }

    public Deployment readConfigFile(String path, String password) throws WooshException {
        String configText = configReader.loadConfig(path);
        JSONObject jsonObject= null;
        String decryptedText = null;
        try{
            System.out.println("DECRYPT WITH " + password);
            System.out.println("DECRYPTING" + configText);
            decryptedText = Crypto.decrypt(password,configText);
            System.out.println("PLAIN TEXT IS" + decryptedText);
        }catch (Exception e){
            e.printStackTrace();
            throw new WooshException("Wrong password for configuration");
        }
        jsonObject = new JSONObject(decryptedText);
        return configReader.parseFromJSON(jsonObject);
    }
}
