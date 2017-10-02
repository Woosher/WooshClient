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
        System.out.println(content);
        File file = null;
        try {
            file = Utils.generateFile(path);
            FileWriter fooWriter = new FileWriter(file, true);
            fooWriter.write(content);
            fooWriter.close();
        } catch (IOException e) {
            throw new WooshException("Could not create file");
        }

    }

    public boolean checkDeployment(Deployment deployment) throws WooshException {
        return false;
    }
}
