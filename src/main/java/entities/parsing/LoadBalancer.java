package entities.parsing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancer extends Machine {

    private String cachingAttributes;
    private ObservableList<Node> nodes;

    public LoadBalancer(){
        nodes = FXCollections.observableArrayList();
    }


    public String getCachingAttributes() {
        return cachingAttributes;
    }

    public void setCachingAttributes(String cachingAttributes) {
        this.cachingAttributes = cachingAttributes;
    }

    public ObservableList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
        notifyListeners();
    }
}
