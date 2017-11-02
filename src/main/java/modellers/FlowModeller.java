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

    private ReaderInterface readController;
    private PackagingInterface packagingController;
    private ConnectionInterface connectionController;
    private MapperInterface memoryMapper;

    public FlowModeller(){
        packagingController = new PackagingModeller();
        connectionController = new ConnectionModeller();
        memoryMapper = new MemoryModeller();
        readController = new ReadModeller();
    }

    @Override
    public void loadDeployment(String path,final ResultsListener<Deployment> resultsListener) {
        supplyAsync(()-> {
            try {
                return readController.readConfigFile(path);
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {memoryMapper.setDeployment(a);  resultsListener.onCompletion(memoryMapper.getDeployment());})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    @Override
    public void saveDeployment(String path, ResultsListener<Void> resultsListener) {
        supplyAsync(() -> {
            try {
                packagingController.formatToConfigFile(memoryMapper.getDeployment(),path);
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
            memoryMapper.clearDeployment();
            resultsListener.onCompletion(null);
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void addNodeToLoadBalancer(LoadBalancer loadBalancer, String nodeName, ResultsListener<String> resultsListener) {
        try {
            memoryMapper.addNodeToLoadBalancer(loadBalancer,nodeName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }


    @Override
    public void removeNodeFromLoadBalancer(ObservableList<Node> nodes, Node node, ResultsListener<String> resultsListener) {
        try{
            memoryMapper.deleteNodeFromLoadBalancer(nodes,node);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }

    }

    @Override
    public void addNodeToDeployment(String machineName, ResultsListener<String> resultsListener){
        try {
            memoryMapper.addNode(machineName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void addLoadBalancerToDeployment(String machineName, ResultsListener<String> resultsListener) {
        try {
            memoryMapper.addLoadbalancer(machineName);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void removeMachineFromDeployment(ObservableList<Machine> machines, Machine machine, ResultsListener<String> resultsListener) {
        try{
            memoryMapper.deleteMachine(machines,machine);
            resultsListener.onCompletion("Complete");
        } catch (WooshException e) {
            resultsListener.onFailure(e);
        }
    }

    @Override
    public void testConnections(final ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                return connectionController.testConnections(memoryMapper.getDeployment());
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
            Deployment deployment = memoryMapper.createNewDeployment(name);
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
    public void sendPackages(final ResultsListener<List<ConnectionInfo>> resultsListener) {
        supplyAsync(()-> {
            try {
                return deploy();
            } catch (WooshException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).thenAccept(a -> {
            resultsListener.onCompletion(Arrays.asList(a));})
                .exceptionally((t) -> {
                    resultsListener.onFailure(t); return null;});

    }

    private ConnectionInfo[] deploy() throws WooshException{
        try {
            packagingController.readyDeployment(memoryMapper.getDeployment());
        } catch (WooshException e) {
            e.printStackTrace();
        }

        Stack<Machine> machines = new Stack<>();
        for(Machine machine: memoryMapper.getDeployment().getMachines()){
            if(machine instanceof LoadBalancer){
                machines.addAll(((LoadBalancer)machine).getNodes());
            }
        }
        machines.addAll(memoryMapper.getDeployment().getMachines());

        int numOfMachines = machines.size();

        CompletableFuture<?>[] allFutures = new CompletableFuture<?>[numOfMachines];
        for (int i=0; i<numOfMachines; ++i) {
            allFutures[i] = CompletableFuture.supplyAsync(() -> {return connectionController.sendPackage(machines.pop());});
        }

        CompletableFuture.allOf(allFutures).join();
        return Arrays.stream(allFutures).map(CompletableFuture::join).toArray(ConnectionInfo[]::new);
    }

}
