package controllers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import iohelpers.ConfigChecker;
import org.json.JSONObject;
import subcontrollers.ConnectionController;
import subcontrollers.MemoryMapper;
import subcontrollers.PackagingController;
import subcontrollers.ReadController;

public class FlowController {

    private ReadController readController;
    private PackagingController packagingController;
    private ConnectionController connectionController;
    private MemoryMapper memoryMapper;
    private ConfigChecker configChecker;

    public FlowController(){
        configChecker = new ConfigChecker();
        packagingController = new PackagingController();
        connectionController = new ConnectionController();
        memoryMapper = new MemoryMapper(configChecker);
        readController = new ReadController(configChecker);
        testParsing();


    }

    public void testParsing(){
        String scriptTest = "{ \"deployment_name\": \"demoname\", \"ssl_path\": \"/testpath/stuff\", \"loadbalancers\": [{ \"name\": \"lb1\", \"ip\": \"127.0.0.1\", \"port\": 8000, \"caching_attributes\": \"/testpath/stuff\", \"nodes\": [{ \"node\": { \"ip\": \"127.0.1.1\", \"port\": 8000, \"name\": \"NAME1\", \"software_environment\": \"JAVA\", \"operating_system\": \"ubuntu_xenial_16\" } }, { \"node\": { \"ip\": \"127.0.1.2\", \"port\": 8000, \"name\": \"NAME2\", \"software_environment\": \"JAVA\", \"operating_system\": \"ubuntu_xenial_16\" } }] }, { \"name\": \"lb2\", \"ip\": \"127.0.1.4\", \"port\": 8000, \"caching_attributes\": \"/testpath/stuff\", \"nodes\": [{ \"node\": { \"ip\": \"127.0.1.3\", \"port\": 8000, \"name\": \"NAME\", \"software_environment\": \"JAVA\", \"operating_system\": \"ubuntu_xenial_16\" } }] }] }";
        JSONObject jsonObject = new JSONObject(scriptTest);

        //PARSE JSON TO DEPLOYMENT CLASS
        Deployment deployment = Deployment.parseFromJSON(jsonObject);
        printDeployMent(deployment);

        //PARSE DEPLOYMENT CLASS TO JSON
        String deploymentJson = deployment.parseToJSON().toString();
        print("");
        print(deploymentJson);
        print("");
        //PARSE BACK TO DEPLOYMENT CLASS
        JSONObject loadSavedJson = new JSONObject(deploymentJson);
        Deployment deploymentLoad = Deployment.parseFromJSON(loadSavedJson);
        printDeployMent(deploymentLoad);

    }

    public void printDeployMent(Deployment deployment){
        print("NAME OF DEPLOYMENT: " + deployment.getName());
        print("SSL PATH: " + deployment.getSsl_path());
        print("LOADBALANCERS: ");
        for(LoadBalancer loadBalancer : deployment.getLoadBalancers()){
            print("");
            print("\tNAME OF LB: " + loadBalancer.getName());
            print("\tIP OF LB: " + loadBalancer.getPort());
            print("\tCATCHE OF LB: " + loadBalancer.getCachingAttributes());
            print("\tNODES");
            for(Node node : loadBalancer.getNodes()){
                print("");
                print("\t\tNAME OF NODE: " + node.getName());
                print("\t\tIP OF NODE: " + node.getIp());
                print("\t\tPORT OF NODE: " + node.getPort());
                print("\t\tSE OF NODE: " + node.getEnvironment());
                print("\t\tOS OF NODE: " + node.getOperatingSystem());
            }
        }
    }

    public void print(String text){
        System.out.println(text);
    }

}
