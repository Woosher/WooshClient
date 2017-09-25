package entities;

import org.json.JSONObject;

public class Node{

    private String name, ip, environment, operatingSystem;
    private int port;

    public Node(){

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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Node parseFromJSON(JSONObject jsonObject){
        Node node = new Node();
        node.setIp(jsonObject.getString("ip"));
        node.setPort(jsonObject.getInt("port"));
        node.setName(jsonObject.getString("name"));
        node.setEnvironment(jsonObject.getString("software_environment"));
        node.setOperatingSystem(jsonObject.getString("operating_system"));
        return node;
    }

    public JSONObject parseToJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", this.getIp());
        jsonObject.put("port", this.getPort());
        jsonObject.put("name", this.getName());
        jsonObject.put("software_environment", this.getEnvironment());
        jsonObject.put("operating_system", this.getOperatingSystem());
        return jsonObject;
    }
}
