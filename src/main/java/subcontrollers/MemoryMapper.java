package subcontrollers;

import entities.parsing.Deployment;
import entities.parsing.LoadBalancer;
import entities.parsing.Node;
import exceptions.WooshException;
import iohelpers.ConfigChecker;
import iohelpers.ConfigWriter;
import iohelpers.interfaces.CheckerInterface;
import org.json.JSONObject;
import subcontrollers.interfaces.MapperInterface;

import java.util.List;

public class MemoryMapper implements MapperInterface{

    ConfigWriter configWriter;

    public MemoryMapper(CheckerInterface configChecker){
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

    @Override
    public void formatToConfigFile(Deployment deployment, String path) throws WooshException {
        JSONObject jsonObject = deployment.parseToJSON();
        String plainText = jsonObject.toString();
        configWriter.saveConfig(plainText,path);
    }


    public Deployment clearDeployment(Deployment deployment) throws WooshException {
        return null;
    }
}
