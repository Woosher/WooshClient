package iohelpers;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.WriterInterface;
import org.json.JSONObject;
import tools.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigWriter implements WriterInterface{

    private CheckerInterface configChecker;

    public ConfigWriter(CheckerInterface configChecker){
        this.configChecker = configChecker;
    }

    public JSONObject parseDeployment(Deployment deployment) throws WooshException {
        return null;
    }

    public void saveConfig(String content, String path) throws WooshException {
        FileWriter fileWriter = null;
        File file = null;

         /* TODO
            Brug ConfigChecker til at checke deployment for fejl
            Hvis alt går godt så save filen, ellers send fejl tilbage.
         */

        try {
            file = Utils.generateFile(path);
            FileWriter fooWriter = new FileWriter(file, true);
            fooWriter.write(content);
            fooWriter.close();
        } catch (IOException e) {
            throw new WooshException("Could not create file" );
        }

    }

}
