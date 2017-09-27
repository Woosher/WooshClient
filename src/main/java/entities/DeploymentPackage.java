package entities;

import entities.parsing.Machine;

public class DeploymentPackage {

    private Machine machine;
    private String pathBash, pathCompressed;

    public DeploymentPackage(Machine machine){
        this.machine = machine;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
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
