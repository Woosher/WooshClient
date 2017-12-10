package modellers;

import entities.ConnectionInfo;
import entities.parsing.Machine;
import javafx.collections.ObservableList;
import modellers.interfaces.FlowModelInterface;
import modellers.interfaces.ResultsListener;
import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import modellers.submodellers.ConnectionModeller;
import modellers.submodellers.MemoryModeller;
import modellers.submodellers.PackagingModeller;
import modellers.submodellers.ReadModeller;
import modellers.submodellers.interfaces.ConnectionInterface;
import modellers.submodellers.interfaces.MapperInterface;
import modellers.submodellers.interfaces.PackagingInterface;
import modellers.submodellers.interfaces.ReaderInterface;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class FlowModeller implements FlowModelInterface {

    private ReaderInterface readingModeller;
    private PackagingInterface packagingModeller;
    private ConnectionInterface connectionModeller;
    private MapperInterface memoryModeller;

    public FlowModeller(){
        packagingModeller = new PackagingModeller();
        connectionModeller = new ConnectionModeller();
        memoryModeller = new MemoryModeller();
        readingModeller = new ReadModeller();
    }

    @Override
    public void loadDeployment(String path, String password, final ResultsListener<Deployment> resultsListener) {
        supplyAsync(()-> {
            try {
                return readingModeller.readConfigFile(path, password);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {
            memoryModeller.setDeployment(a);  resultsListener.onCompletion(memoryModeller.getDeployment());})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    @Override
    public void saveDeployment(String path, String password, ResultsListener<Void> resultsListener) {
        supplyAsync(() -> {
            try {
                packagingModeller.formatToConfigFile(memoryModeller.getDeployment(),path, password);
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
        try {
            memoryModeller.clearDeployment();
            resultsListener.onCompletion(null);
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName, ResultsListener<String> resultsListener) {
        try {
            memoryModeller.addNodeToLoadBalancer(loadBalancer,nodeName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }


    @Override
    public void removeNodeFromLoadBalancer(ObservableList<Node> nodes, Node node, ResultsListener<String> resultsListener) {
        try{
            memoryModeller.deleteNodeFromLoadBalancer(nodes,node);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }

    }

    @Override
    public void addNodeToDeployment(String machineName, ResultsListener<String> resultsListener){
        try {
            memoryModeller.addNode(machineName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void addLoadBalancerToDeployment(String machineName, ResultsListener<String> resultsListener) {
        try {
            memoryModeller.addLoadbalancer(machineName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void removeMachineFromDeployment(ObservableList<Machine> machines, Machine machine, ResultsListener<String> resultsListener) {
        try{
            memoryModeller.deleteMachine(machines,machine);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                return connectionModeller.testConnections(memoryModeller.getDeployment());
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
            Deployment deployment = memoryModeller.createNewDeployment(name);
            resultsListener.onCompletion(deployment);

        }catch (Exception e){
            resultsListener.onFailure(new WooshException("Could not create deployment"));
        }
    }


    @Override
    public void addKnownHosts(List<Machine> macs,ResultsListener<Boolean> resultsListener){
        supplyAsync(()-> {
            try {
                connectionModeller.addKnownHosts(macs);
                return true;
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {resultsListener.onCompletion(a);})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});
    }

    @Override
    public void sendAllPackages(final ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                packagingModeller.readyDeployment(memoryModeller.getDeployment());
                return deploy(memoryModeller.getDeployment().getMachinesAsList());
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {
            resultsListener.onCompletion(Arrays.asList(a));})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    @Override
    public void sendPackages(String name, List<Machine> macs, ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                packagingModeller.readyMachines(name, macs);
                return deploy(macs);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {
            resultsListener.onCompletion(Arrays.asList(a));})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});
    }


    private ConnectionInfo[] deploy(List<Machine> machines) throws WooshException {
        int numOfMachines = machines.size();
        CompletableFuture<?>[] allFutures = new CompletableFuture<?>[numOfMachines];
        for (int i=0; i<numOfMachines; ++i) {
            allFutures[i] = CompletableFuture.supplyAsync(() -> {return connectionModeller.sendPackage(machines.remove(0));});
        }
        CompletableFuture.allOf(allFutures).join();
        return Arrays.stream(allFutures).map(CompletableFuture::join).toArray(ConnectionInfo[]::new);
    }

}
