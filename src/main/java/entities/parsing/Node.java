package entities.parsing;

import org.json.JSONObject;

public class Node extends Machine {

    private String environment, operatingSystem, path;

    public Node(){

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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


    public static Node parseFromJSON(JSONObject jsonObject){
        Node node = new Node();
        node.setIp(jsonObject.getString("ip"));
        node.setPort(jsonObject.getInt("port"));
        node.setName(jsonObject.getString("name"));
        node.setUsername(jsonObject.getString("username"));
        node.setEnvironment(jsonObject.getString("software_environment"));
        node.setOperatingSystem(jsonObject.getString("operating_system"));
        node.setPassword(jsonObject.getString("password"));
        node.setPath(jsonObject.getString("path"));
        return node;
    }


}
