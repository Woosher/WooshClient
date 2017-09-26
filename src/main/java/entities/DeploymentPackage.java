package entities;

public class DeploymentPackage {

    private System system;
    private String pathBash, pathCompressed;

    public DeploymentPackage(System system){
        this.system = system;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public String getPathBash() {
        return pathBash;
    }

    public void setPathBash(String pathBash) {
        this.pathBash = pathBash;
    }

    public String getPathCompressed() {
        return pathCompressed;
    }

    public void setPathCompressed(String pathCompressed) {
        this.pathCompressed = pathCompressed;
    }
}
