package entities.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancer extends Machine {

    private String cachingAttributes;
    private List<Node> nodes;

    public LoadBalancer(){
        nodes = new ArrayList<Node>();
    }


    public String getCachingAttributes() {
        return cachingAttributes;
    }

    public void setCachingAttributes(String cachingAttributes) {
        this.cachingAttributes = cachingAttributes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }



}
