package no.cantara.flow.flowlogger.event;

public class Node {

    final String type;
    final String address;
    final String payloadType;
    final String payloadId;

    public Node(String type, String address, String payloadType, String payloadId) {
        this.type = type;
        this.address = address;
        this.payloadType = payloadType;
        this.payloadId = payloadId;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public String getPayloadId() {
        return payloadId;
    }
}
