package modellers;

import entities.ConnectionInfo;
import entities.parsing.Machine;
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

import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.*;

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
        packagingController = new PackagingController(configChecker);
        connectionController = new ConnectionController();
        memoryMapper = new MemoryMapper();
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
        }).thenAccept(a -> {this.deployment = a;  resultsListener.onCompletion(deployment);})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    @Override
    public void saveDeployment(String path, ResultsListener<Void> resultsListener) {
        supplyAsync(() -> {
            try {
                packagingController.formatToConfigFile(this.deployment,path);
               // packagingController.readyDeployment(this.deployment);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
            return  null;
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
    public void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                return connectionController.testConnections(deployment);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {resultsListener.onCompletion(a);})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});
    }

    @Override
    public void createNewDeployment(String name, ResultsListener<Deployment> resultsListener) {
        try{
            this.deployment = new Deployment();
            this.deployment.setName(name);
        /*TODO
            SSLPATH REMOVE
         */
            this.deployment.setSsl_path("oaskdo");
            resultsListener.onCompletion(deployment);

        }catch (Exception e){
            resultsListener.onFailure(new WooshException("Could not create deployment"));
        }
    }


    @Override
    public void addKnownHosts(List<Machine> macs,ResultsListener<Boolean> resultsListener){

        supplyAsync(()-> {
            try {
                connectionController.addKnownHosts(macs);
                return true;
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {resultsListener.onCompletion(a);})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});
    }




    @Override
    public void sendPackages(final ResultsListener<String> resultsListener) {
        try {
            packagingController.readyDeployment(deployment);
        } catch (WooshException e) {
            e.printStackTrace();
        }
        Stack<Machine> machines = new Stack<>();
        for(LoadBalancer loadBalancer: deployment.getLoadBalancers()){
            machines.addAll(loadBalancer.getNodes());
        }
        machines.addAll(deployment.getLoadBalancers());
        for(int i = 0; i<machines.size(); i++){
            deploy(machines.get(i));
        }
//
//        int numOfMachines = machines.size();
//        ExecutorService executor = new ThreadPoolExecutor(numOfMachines, numOfMachines,
//                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
//
//        CompletableFuture<?>[] allFutures = new CompletableFuture<?>[numOfMachines];
//        for (int i=0; i<numOfMachines; ++i) {
//            allFutures[i] = CompletableFuture.supplyAsync(() -> {
//                Future future = executor.submit(() -> deploy(machines.pop()));
//                //executor.schedule(() -> future.cancel(true), 100, TimeUnit.MILLISECONDS);
//                try {
//                    return future.get();
//                } catch (InterruptedException | ExecutionException | CancellationException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            });
//        }
//
//        System.out.println(CompletableFuture.allOf(allFutures).join());
        resultsListener.onCompletion("Succes");
    }

    private void deploy(Machine machine){
        try {
            connectionController.sendPackage(machine);
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
