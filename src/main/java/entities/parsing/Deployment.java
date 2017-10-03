package entities.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Deployment {

    private String name, ssl_path;
    private List<LoadBalancer> loadBalancers;

    public Deployment(){
        loadBalancers = new ArrayList<LoadBalancer>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsl_path() {
        return ssl_path;
    }

    public void setSsl_path(String ssl_path) {
        this.ssl_path = ssl_path;
    }

    public List<LoadBalancer> getLoadBalancers() {
        return loadBalancers;
    }

    public void setLoadBalancers(List<LoadBalancer> loadBalancers) {
        this.loadBalancers.clear();
        this.loadBalancers.addAll(loadBalancers);
    }




}
