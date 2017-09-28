package modellers;

import iohelpers.interfaces.CheckerInterface;
import modellers.interfaces.FlowModelInterface;
import entities.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import subcontrollers.ConnectionController;
import subcontrollers.MemoryMapper;
import subcontrollers.PackagingController;
import subcontrollers.ReadController;
import subcontrollers.interfaces.ConnectionControllerInterface;
import subcontrollers.interfaces.MapperInterface;
import subcontrollers.interfaces.PackagingInterface;
import subcontrollers.interfaces.ReaderInterface;

import java.util.UUID;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class FlowModeller implements FlowModelInterface {

    private ReaderInterface readController;
    private PackagingInterface packagingController;
    private ConnectionControllerInterface connectionController;
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
    public void sendPackages(final ResultsListener<String> resultsListener) {
        supplyAsync(()->{deploy(resultsListener); return null;}).
                        thenAccept(() -> {store(resultsListener);});
    }

    void deploy(ResultsListener<String> resultsListener) {
        try {
            connectionController.addKnownHosts(deployment);
        } catch (WooshException e) {
            e.printStackTrace();
        }


        //LoadBalancers
        for (LoadBalancer loadBalancer: deployment.getLoadBalancers()) {
            try {
                loadBalancer.setPathBash(packagingController.createBashScripts(loadBalancer));
                loadBalancer.setPathBash(packagingController.compressPackage(loadBalancer));
                //Nodes
                for (Node node: loadBalancer.getNodes()) {
                    try {
                        node.setPathBash(packagingController.createBashScripts(node));
                        node.setPathBash(packagingController.compressPackage(node));
                    }catch (WooshException e) {
                        e.printStackTrace();
                    }
                }
            } catch (WooshException e) {
                e.printStackTrace();
            }

        }

        try {
            connectionController.sendPackages(deployment);
        } catch (WooshException e) {
            e.printStackTrace();
        }

    }

    void store(ResultsListener<String> resultsListener) {
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
