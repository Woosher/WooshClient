package entities.parsing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Deployment extends Observable {

    private String name, ssl_path;
    private ObservableList<LoadBalancer> loadBalancers;

    public Deployment(){
        loadBalancers = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
        notifyObservers();
    }

    public String getSsl_path() {
        return ssl_path;
    }

    public void setSsl_path(String ssl_path) {
        this.ssl_path = ssl_path;
    }

    public ObservableList<LoadBalancer> getLoadBalancers() {
        return loadBalancers;
    }

    public void setLoadBalancers(List<LoadBalancer> loadBalancers) {
        this.loadBalancers.clear();
        this.loadBalancers.addAll(loadBalancers);
        setChanged();
        notifyObservers();
    }

}
