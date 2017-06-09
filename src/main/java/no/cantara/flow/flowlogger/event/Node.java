package no.cantara.flow.flowlogger.event;

public class Node {

    final String type;
    final String address;

    public Node(String type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }
}
