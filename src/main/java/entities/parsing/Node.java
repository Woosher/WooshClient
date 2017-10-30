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
        notifyListeners();
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
        notifyListeners();
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        notifyListeners();
    }

}
