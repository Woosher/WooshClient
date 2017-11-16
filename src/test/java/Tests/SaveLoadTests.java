package Tests;

import entities.parsing.Deployment;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import iohelpers.ConfigReader;
import iohelpers.ConfigWriter;
import iohelpers.interfaces.CheckerInterface;
import iohelpers.interfaces.ConfigReaderInterface;
import iohelpers.interfaces.WriterInterface;
import modellers.submodellers.ReadModeller;
import modellers.submodellers.interfaces.ReaderInterface;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

import static Tests.TestConstants.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveLoadTests {

    @Test
    public void testValidJSON(){
        Boolean result = true;
        ConfigReaderInterface cr = new ConfigReader();
        JSONObject jsonObject = new JSONObject(VALID_JSON);
        try {
            cr.parseFromJSON(jsonObject);
        } catch (Exception e) {
            result = false;
        }
        assertTrue(result);
    }

    @Test
    public void testInvalidJSON(){
        Boolean result = true;
        ConfigReaderInterface cr = new ConfigReader();
        JSONObject jsonObject = new JSONObject(INVALID_JSON);
        try {
            cr.parseFromJSON(jsonObject);
        } catch (Exception e) {
            result = false;
        }
        assertFalse(result);
    }


    @Test
    public void testValidDeployment(){
        Boolean result = true;
        ConfigReaderInterface cr = new ConfigReader();
        CheckerInterface cc = new ConfigChecker();

        Deployment d;
        JSONObject jsonObject = new JSONObject(VALID_JSON);

        try {
            d = cr.parseFromJSON(jsonObject);
            cc.checkDeploymentObject(d);
        } catch (WooshException e) {
            result = false;
        }

        assertTrue(result);
    }

    @Test
    public void testInvalidDeployment(){
        Boolean result = true;
        ConfigReaderInterface cr = new ConfigReader();
        CheckerInterface cc = new ConfigChecker();

        Deployment d;

        JSONObject jsonObject = new JSONObject(INVALID_DEPLOYMENT_JSON);

        try {
            d = cr.parseFromJSON(jsonObject);
            cc.checkDeploymentObject(d);
        } catch (WooshException e) {
            result = false;
        }
        assertFalse(result);
    }

    @Test
    public void testASave(){
        Boolean result = true;
        String path = System.getProperty("user.home")+"/"+DEPLOYMENT_NAME+".txt";
        WriterInterface cw = new ConfigWriter();
        ConfigReaderInterface cr = new ConfigReader();
        JSONObject jsonObject = new JSONObject(INVALID_DEPLOYMENT_JSON);

        Deployment d = null;
        try {
            d = cr.parseFromJSON(jsonObject);
        } catch (WooshException e) {
        }



        try{
            cw.saveDeployment(d,path,PASSWORD);
        }catch (WooshException e){
            result = false;
        }
        assertTrue(result);
    }

    @Test
    public void testBLoad(){
        Boolean result = true;
        String path = System.getProperty("user.home")+"/"+DEPLOYMENT_NAME+".txt";
        ReaderInterface r = new ReadModeller();
        try {
            r.readConfigFile(path,PASSWORD);
        }catch (WooshException e){
            e.printStackTrace();
            result = false;
        }
        assertTrue(result);
    }


    @AfterClass
    public static void clealUp(){
        String path = System.getProperty("user.home")+"/"+DEPLOYMENT_NAME+".txt";
        try{new File(path).delete();}catch(Exception e){}
    }



}
