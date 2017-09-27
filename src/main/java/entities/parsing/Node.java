package entities.parsing;

import org.json.JSONObject;

public class Node extends Machine {

    private String environment, operatingSystem;

    public Node(){

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
        node.setEnvironment(jsonObject.getString("software_environment"));
        node.setOperatingSystem(jsonObject.getString("operating_system"));
        node.setPassword(jsonObject.getString("password"));
        return node;
    }

    public JSONObject parseToJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", this.getIp());
        jsonObject.put("port", this.getPort());
        jsonObject.put("name", this.getName());
        jsonObject.put("software_environment", this.getEnvironment());
        jsonObject.put("operating_system", this.getOperatingSystem());
        jsonObject.put("password",this.getPassword());
        return jsonObject;
    }
}
