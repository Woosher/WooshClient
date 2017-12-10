package iohelpers.interfaces;

import entities.parsing.Deployment;
import entities.parsing.Machine;
import exceptions.WooshException;
import org.json.JSONObject;

import java.util.List;

public interface CheckerInterface {

    void checkDeploymentJSON(JSONObject jsonObject) throws WooshException;

    void checkDeploymentObject(Deployment deployment) throws WooshException;

     void checkMachineList(List<Machine> machineList) throws WooshException;

}
