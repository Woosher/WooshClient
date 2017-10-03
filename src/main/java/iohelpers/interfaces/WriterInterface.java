package iohelpers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;
import org.json.JSONObject;

public interface WriterInterface {


    void saveDeployment(Deployment deployment, String path) throws WooshException;

    void saveFile(String content, String path) throws WooshException;

}
