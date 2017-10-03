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


    public JSONObject parseToJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("name",this.getName());
        jsonObject.put("username",this.getUsername());
        jsonObject.put("ip", this.getIp());
        jsonObject.put("port", this.getPort());
        jsonObject.put("caching_attributes", this.getCachingAttributes());
        jsonObject.put("password", this.getPassword());

        for(Node node : this.getNodes()){
            JSONObject jsonNodeInfo = node.parseToJSON();
            JSONObject jsonNode = new JSONObject();
            jsonNode.put("node", jsonNodeInfo);
            jsonArray.put(jsonNode);
        }

        jsonObject.put("nodes", jsonArray);
        return jsonObject;
    }
}
