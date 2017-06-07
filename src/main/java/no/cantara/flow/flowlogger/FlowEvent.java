package no.cantara.flow.flowlogger;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.UUID;

public class FlowEvent {

    public static class Reporter {

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

    public static class Node {

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

    public static class Edge {

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

    final String correlationId;
    final String[] history;
    final Reporter reporter;
    final Node source;
    final Node destination;
    final Edge edge;

    public FlowEvent(
            String correlationId,
            String history,
            String deployment,
            String name,
            String version,
            String containerId,
            String instance,
            String sourceType,
            String sourceAddress,
            String destinationType,
            String destinationAddress,
            String id,
            String timestamp,
            String milestone,
            String status,
            String errorType,
            String errorReason,
            String payloadType,
            String payloadId,
            String comment
    ) {
        this.correlationId = correlationId;
        if (history == null) {
            this.history = null;
        } else {
            this.history = history.split("[, ]");
        }
        if (deployment == null && name == null && version == null && containerId == null && instance == null) {
            this.reporter = null;
        } else {
            this.reporter = new Reporter(deployment, name, version, containerId, instance);
        }
        if (sourceType == null && sourceAddress == null) {
            this.source = null;
        } else {
            this.source = new Node(sourceType, sourceAddress);
        }
        if (destinationType == null && destinationAddress == null) {
            this.destination = null;
        } else {
            this.destination = new Node(destinationType, destinationAddress);
        }
        this.edge = new Edge(id, timestamp, milestone, status, errorType, errorReason, payloadType, payloadId, comment);
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String[] getHistory() {
        return history;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public Edge getEdge() {
        return edge;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String nextHistory() {
        StringJoiner sj = new StringJoiner(",");
        if (history != null) {
            for (String p : history) {
                sj.add(p);
            }
        }
        sj.add(edge.id);
        return sj.toString();
    }

    public static class Builder {
        String correlationId;
        String history;

        String deployment;
        String name;
        String version;
        String milestone;
        String containerId;
        String instance;

        String sourceType;
        String sourceAddress;

        String destinationType;
        String destinationAddress;

        String edgeId;
        ZonedDateTime timestamp;
        String status = "OK";
        String errorType;
        String errorReason;
        String payloadType;
        String payloadId;
        String comment;

        public FlowEvent build() {
            if (correlationId == null) {
                throw new IllegalArgumentException("event.correlationId is missing.");
            }
            if (edgeId == null) {
                throw new IllegalArgumentException("edge.id is missing.");
            }
            if (timestamp == null) {
                timestamp = ZonedDateTime.now();
            }
            return new FlowEvent(
                    correlationId,
                    history,
                    deployment,
                    name,
                    version,
                    containerId,
                    instance,
                    sourceType,
                    sourceAddress,
                    destinationType,
                    destinationAddress,
                    edgeId,
                    timestamp.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    milestone,
                    status,
                    errorType,
                    errorReason,
                    payloadType,
                    payloadId,
                    comment
            );
        }

        public EdgeBuilder event() {
            return new EdgeBuilder();
        }

        public abstract class AbstractBuilder<T> {

            public ReporterBuilder reporter() {
                return new ReporterBuilder();
            }

            public SourceBuilder source() {
                return new SourceBuilder();
            }

            public DestinationBuilder destination() {
                return new DestinationBuilder();
            }

            public EdgeBuilder edge() {
                return new EdgeBuilder();
            }

            public FlowEvent build() {
                return Builder.this.build();
            }
        }

        public class ReporterBuilder extends AbstractBuilder {

            private ReporterBuilder() {
            }

            public ReporterBuilder deployment(String deployment) {
                Builder.this.deployment = deployment;
                return this;
            }

            public ReporterBuilder name(String name) {
                Builder.this.name = name;
                return this;
            }

            public ReporterBuilder version(String version) {
                Builder.this.version = version;
                return this;
            }

            public ReporterBuilder milestone(String milestone) {
                Builder.this.milestone = milestone;
                return this;
            }

            public ReporterBuilder containerId(String containerId) {
                Builder.this.containerId = containerId;
                return this;
            }

            public ReporterBuilder instance(String instance) {
                Builder.this.instance = instance;
                return this;
            }
        }

        public class SourceBuilder extends AbstractBuilder {

            private SourceBuilder() {
            }

            public SourceBuilder type(String sourceType) {
                Builder.this.sourceType = sourceType;
                return this;
            }

            public SourceBuilder address(String sourceAddress) {
                Builder.this.sourceAddress = sourceAddress;
                return this;
            }
        }

        public class DestinationBuilder extends AbstractBuilder {

            private DestinationBuilder() {
            }

            public DestinationBuilder type(String destinationType) {
                Builder.this.destinationType = destinationType;
                return this;
            }

            public DestinationBuilder address(String destinationAddress) {
                Builder.this.destinationAddress = destinationAddress;
                return this;
            }
        }

        public class EdgeBuilder extends AbstractBuilder {

            public EdgeBuilder generateCorrelationId() {
                return correlationId(UUID.randomUUID().toString());
            }

            public EdgeBuilder correlationId(String correlationId) {
                Builder.this.correlationId = correlationId;
                return this;
            }

            public EdgeBuilder history(String history) {
                Builder.this.history = history;
                return this;
            }

            public EdgeBuilder generateId() {
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    String randomUuid = UUID.randomUUID().toString();
                    String shorterRandomId = new String(md5.digest(randomUuid.getBytes(Charset.forName("ASCII"))), "ASCII").substring(0, 4);
                    return id(shorterRandomId);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

            public EdgeBuilder id(String edgeId) {
                Builder.this.edgeId = edgeId;
                return this;
            }

            public EdgeBuilder timestamp(ZonedDateTime timestamp) {
                Builder.this.timestamp = timestamp;
                return this;
            }

            public EdgeBuilder status(String status) {
                Builder.this.status = status;
                return this;
            }

            public EdgeBuilder errorType(String errorType) {
                Builder.this.errorType = errorType;
                return this;
            }

            public EdgeBuilder errorReason(String errorReason) {
                Builder.this.errorReason = errorReason;
                return this;
            }

            public EdgeBuilder payloadType(String payloadType) {
                Builder.this.payloadType = payloadType;
                return this;
            }

            public EdgeBuilder payloadId(String payloadId) {
                Builder.this.payloadId = payloadId;
                return this;
            }

            public EdgeBuilder comment(String comment) {
                Builder.this.comment = comment;
                return this;
            }
        }
    }
}
