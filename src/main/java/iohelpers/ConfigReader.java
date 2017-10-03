package iohelpers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ConfigReaderInterface;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader implements ConfigReaderInterface {

    private CheckerInterface configChecker;

    public ConfigReader(CheckerInterface configChecker){
        this.configChecker = configChecker;
    }


    public Deployment parseConfig(JSONObject jsonObject) throws WooshException {
        return null;
    }

    public String loadConfig(String path) throws WooshException {
        String everything = null;
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            throw new WooshException("Could not find file: " + path);
        } catch (IOException e) {
            throw new WooshException("Could not open file: " + path);
        }

           /*TODO
            Ryk JSON Parsing ud fra entitetsklasserne
            --- CONFIGCHECKER STUFF --
            1. Smid JSON ind i configchecker
            2. hvis configchecker ikke returnere nogle fejl så parse alt json til et deployment og returner dette
         */

        return everything;
    }

    public boolean checkDeployment(Deployment deployment) throws WooshException {
        return false;
    }
}
