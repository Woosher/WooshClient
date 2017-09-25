package subcontrollers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import iohelpers.ConfigWriter;
import subcontrollers.interfacaes.MapperInterface;

import java.util.List;

public class MemoryMapper implements MapperInterface{

    ConfigWriter configWriter;

    public MemoryMapper(ConfigChecker configChecker){
        configWriter = new ConfigWriter(configChecker);

    }

    public Deployment addNodes(List<Node> nodes) throws WooshException {
        return null;
    }

    public void addDeployment(Deployment deployment) throws WooshException {

    }

    public Deployment addLoadBalancers(List<LoadBalancer> loadBalancers) throws WooshException {
        return null;
    }

    public String formatToConfigFile(Deployment deployment) throws WooshException {
        return null;
    }

    public Deployment clearDeployment(Deployment deployment) throws WooshException {
        return null;
    }
}
