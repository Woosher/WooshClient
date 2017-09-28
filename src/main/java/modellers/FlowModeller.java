package modellers;

import iohelpers.interfaces.CheckerInterface;
import modellers.interfaces.FlowModelInterface;
import entities.DeploymentPackage;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import org.json.JSONObject;
import subcontrollers.ConnectionController;
import subcontrollers.MemoryMapper;
import subcontrollers.PackagingController;
import subcontrollers.ReadController;
import subcontrollers.interfaces.MapperInterface;
import subcontrollers.interfaces.PackagingInterface;
import subcontrollers.interfaces.ReaderInterface;

import java.io.Reader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class FlowModeller implements FlowModelInterface {

    private ReaderInterface readController;
    private PackagingInterface packagingController;
    private ConnectionController connectionController;
    private MapperInterface memoryMapper;
    private CheckerInterface configChecker;
    private Deployment deployment;

    public FlowModeller(){
        configChecker = new ConfigChecker();
        packagingController = new PackagingController();
        connectionController = new ConnectionController();
        memoryMapper = new MemoryMapper(configChecker);
        readController = new ReadController(configChecker);
        deployment = null;
        //testParsing();
    }



    @Override
    public void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) {





        supplyAsync(()-> {
            try {
                return readController.readConfigFile(path);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAcceptAsync(a -> answerLoad(a, resultsListener))
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    private void answerLoad(Deployment deployment, ResultsListener<Deployment> resultsListener){
        this.deployment = deployment;
        resultsListener.onCompletion(deployment);
    }

    @Override
    public void saveDeployment(Deployment deployment, ResultsListener<Void> resultsListener) {

    }

    @Override
    public void clearDeployment(ResultsListener<Deployment> resultsListener) {

    }

    @Override
    public void addNodeToDeployment(Node node, ResultsListener<String> resultsListener) {

    }

    @Override
    public void removeNodeToDeployment(Node node, ResultsListener<String> resultsListener) {

    }

    @Override
    public void addLoadBalancerToDeployment(LoadBalancer loadBalancer, ResultsListener<String> resultsListener) {

    }

    @Override
    public void removeLoadBalancerToDeployment(LoadBalancer loadBalancer, ResultsListener<String> resultsListener) {

    }

    @Override
    public void sendPackage(DeploymentPackage deploymentPackage, final ResultsListener<String> resultsListener) {
        supplyAsync(this::createId).
                        thenApply(this::convert).
                        thenAccept(a -> store(a, resultsListener));
    }

    @Override
    public void deploy(ResultsListener<String> resultsListener) {
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {

        }
    }

    void store(String message, ResultsListener<String> resultsListener) {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resultsListener.onCompletion("LISTENER COMPLETE");
    }


    String convert(UUID input) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input.toString();
    }

    UUID createId() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return UUID.randomUUID();
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
