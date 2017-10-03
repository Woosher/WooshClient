package iohelpers.interfaces;

import entities.parsing.Deployment;
import exceptions.WooshException;
import org.json.JSONObject;

public interface CheckerInterface {

    void checkDeploymentJSON(JSONObject jsonObject) throws WooshException;

    void checkDeploymentObject(Deployment deployment) throws WooshException;

}
