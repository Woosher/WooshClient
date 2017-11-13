package entities.parsing;

public class Node extends Machine {

    private String environment, operatingSystem, programPath;

    public Node(){

    }

    public String getProgramPath() {
        return programPath;
    }

    public void setProgramPath(String programPath) {
        this.programPath = programPath;
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
