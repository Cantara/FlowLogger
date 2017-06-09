package no.cantara.flow.flowlogger.event;

public class Reporter {

    final String deployment;
    final String name;
    final String version;
    final String containerId;
    final String instance;

    public Reporter(String deployment, String name, String version, String containerId, String instance) {
        this.deployment = deployment;
        this.name = name;
        this.version = version;
        this.containerId = containerId;
        this.instance = instance;
    }

    public String getDeployment() {
        return deployment;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getInstance() {
        return instance;
    }
}
