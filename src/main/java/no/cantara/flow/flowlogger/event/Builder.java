package no.cantara.flow.flowlogger.event;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

public class Builder {
    String correlationId;
    String[] history;

    String deployment;
    String name;
    String version;
    String containerId;
    String instance;

    String sourceType;
    String sourceAddress;
    String sourcePayloadType;
    String sourcePayloadId;

    String destinationType;
    String destinationAddress;
    String destinationPayloadType;
    String destinationPayloadId;

    String edgeId;
    String retryGroupId;
    ZonedDateTime timestamp;
    String milestone;
    String status = "OK";
    String errorType;
    String errorReason;
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

        Reporter reporter;
        if (deployment == null && name == null && version == null && containerId == null && instance == null) {
            reporter = null;
        } else {
            reporter = new Reporter(deployment, name, version, containerId, instance);
        }
        Node source;
        if (sourceType == null && sourceAddress == null) {
            source = null;
        } else {
            source = new Node(sourceType, sourceAddress, sourcePayloadType, sourcePayloadId);
        }
        Node destination;
        if (destinationType == null && destinationAddress == null) {
            destination = null;
        } else {
            destination = new Node(destinationType, destinationAddress, destinationPayloadType, destinationPayloadId);
        }
        Edge edge = new Edge(edgeId, retryGroupId, timestamp.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), milestone, status, errorType, errorReason, comment);

        return new FlowEvent(
                correlationId,
                history,
                reporter,
                source,
                destination,
                edge
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

        public SourceBuilder payloadType(String payloadType) {
            Builder.this.sourcePayloadType = payloadType;
            return this;
        }

        public SourceBuilder payloadId(String payloadId) {
            Builder.this.sourcePayloadId = payloadId;
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

        public DestinationBuilder payloadType(String payloadType) {
            Builder.this.destinationPayloadType = payloadType;
            return this;
        }

        public DestinationBuilder payloadId(String payloadId) {
            Builder.this.destinationPayloadId = payloadId;
            return this;
        }
    }

    private static final Pattern COMMA_SEPARATED_EDGE_IDS_PATTERN = Pattern.compile("[a-zA-Z0-9+/=]+?(,[a-zA-Z0-9+/=]+)*");

    public class EdgeBuilder extends AbstractBuilder {

        public EdgeBuilder generateCorrelationId() {
            return correlationId(UUID.randomUUID().toString());
        }

        public EdgeBuilder correlationId(String correlationId) {
            Builder.this.correlationId = correlationId;
            return this;
        }

        /**
         * Expects a list of edge ids separated by space. Each edge-id must only have characters taken from the Base64
         * basic alphabet, an IllegalArgumentException is thrown if this requirement is not met.
         *
         * @param history
         * @return this builder.
         */
        public EdgeBuilder history(String history) {
            if (history == null || history.isEmpty()) {
                Builder.this.history = null;
            } else {
                if (!COMMA_SEPARATED_EDGE_IDS_PATTERN.matcher(history).matches()) {
                    throw new IllegalArgumentException("History must match a pattern of a comma-separated list of IDs with alphabet taken from the Base64 basic type. Bad history: \"" + history + "\"");
                }
                Builder.this.history = history.split("[, ]");
            }
            return this;
        }

        /**
         * Generate a random (with characters from the basic Base64 alphabet) 5-character long string. Assuming a true
         * random function, this gives approximately 1 to a billion chance of collision. This is useful when a flow is
         * split, so that we never (in practice) get the same history for separate paths.
         *
         * @return this builder.
         */
        public EdgeBuilder generateId() {
            String shorterRandomId = generateShortRandomId();
            return id(shorterRandomId);
        }

        public EdgeBuilder id(String edgeId) {
            Builder.this.edgeId = edgeId;
            return this;
        }

        public EdgeBuilder retryGroupId(String retryGroupId) {
            Builder.this.retryGroupId = retryGroupId;
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

        public EdgeBuilder milestone(String milestone) {
            Builder.this.milestone = milestone;
            return this;
        }

        public EdgeBuilder comment(String comment) {
            Builder.this.comment = comment;
            return this;
        }
    }

    /**
     * Generate a random (with characters from the basic Base64 alphabet) 5-character long string. Assuming a true
     * random function, this gives approximately 1 to a billion chance of collision. This is useful when a flow is
     * split, so that we never (in practice) get the same history for separate paths.
     *
     * @return the generated id.
     */
    public static String generateShortRandomId() {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String randomUuid = UUID.randomUUID().toString();
            return Base64.getEncoder().encodeToString(md5.digest(randomUuid.getBytes(Charset.forName("ASCII")))).substring(0, 5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
