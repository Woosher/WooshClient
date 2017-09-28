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
        }).thenAccept(a -> {this.deployment = a; resultsListener.onCompletion(deployment);})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    @Override
    public void saveDeployment(Deployment deployment, String path, ResultsListener<Void> resultsListener) {
        supplyAsync(() -> {
            try {
                return memoryMapper.formatToConfigFile(this.deployment,path);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> { resultsListener.onCompletion(null);}).exceptionally((t) -> {
            resultsListener.onFailure(t); return null;
        });
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


    public void print(String text){
        System.out.println(text);
    }



}
