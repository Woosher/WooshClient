package entities.parsing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {

    private String name, ip, cachingAttributes;
    private int port;
    private List<Node> nodes;

    public LoadBalancer(){
        nodes = new ArrayList<Node>();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public static LoadBalancer parseFromJSON(JSONObject jsonObject){
        LoadBalancer loadBalancer = new LoadBalancer();
        List<Node> nodes = new ArrayList<Node>();
        loadBalancer.setName(jsonObject.getString("name"));
        loadBalancer.setIp(jsonObject.getString("ip"));
        loadBalancer.setPort(jsonObject.getInt("port"));
        loadBalancer.setCachingAttributes(jsonObject.getString("caching_attributes"));

        JSONArray JSONnodes = jsonObject.getJSONArray("nodes");
        for(int i = 0; i<JSONnodes.length(); i++){
            JSONObject JSONnode = JSONnodes.getJSONObject(i);
            Node node = Node.parseFromJSON(JSONnode.getJSONObject("node"));
            nodes.add(node);
        }
        loadBalancer.setNodes(nodes);

        return loadBalancer;
    }

    public JSONObject parseToJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("name",this.getName());
        jsonObject.put("ip", this.getIp());
        jsonObject.put("port", this.getPort());
        jsonObject.put("caching_attributes", this.getCachingAttributes());

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
