package no.cantara.flow.flowlogger.event;

public class Edge {

    final String id;
    final String timestamp;
    final String milestone;
    final String status;
    final String errorType;
    final String errorReason;
    final String payloadType;
    final String payloadId;
    final String comment;

    public Edge(String id, String timestamp, String milestone, String status, String errorType, String errorReason, String payloadType, String payloadId, String comment) {
        this.id = id;
        this.timestamp = timestamp;
        this.milestone = milestone;
        this.status = status;
        this.errorType = errorType;
        this.errorReason = errorReason;
        this.payloadType = payloadType;
        this.payloadId = payloadId;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMilestone() {
        return milestone;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public String getPayloadId() {
        return payloadId;
    }

    public String getComment() {
        return comment;
    }
}
