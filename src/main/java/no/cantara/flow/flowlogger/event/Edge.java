package no.cantara.flow.flowlogger.event;

public class Edge {

    final String id;
    final String retryGroupId;
    final String timestamp;
    final String milestone;
    final String status;
    final String errorType;
    final String errorReason;
    final String comment;

    public Edge(String id, String retryGroupId, String timestamp, String milestone, String status, String errorType, String errorReason, String comment) {
        this.id = id;
        this.retryGroupId = retryGroupId;
        this.timestamp = timestamp;
        this.milestone = milestone;
        this.status = status;
        this.errorType = errorType;
        this.errorReason = errorReason;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getRetryGroupId() {
        return retryGroupId;
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

    public String getComment() {
        return comment;
    }
}
