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


    public JSONObject parseToJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ip", this.getIp());
        jsonObject.put("port", this.getPort());
        jsonObject.put("name", this.getName());
        jsonObject.put("username",this.getUsername());
        jsonObject.put("software_environment", this.getEnvironment());
        jsonObject.put("operating_system", this.getOperatingSystem());
        jsonObject.put("password",this.getPassword());
        jsonObject.put("path", this.getPath());
        return jsonObject;
    }
}
