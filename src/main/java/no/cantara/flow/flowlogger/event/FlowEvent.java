package no.cantara.flow.flowlogger.event;

import com.google.gson.Gson;

import java.util.StringJoiner;

public class FlowEvent {

    final String flowEventVersion = "0.1";
    final String correlationId;
    final String[] history;
    final Reporter reporter;
    final Node source;
    final Node destination;
    final Edge edge;

    public FlowEvent(String correlationId, String[] history, Reporter reporter, Node source, Node destination, Edge edge) {
        this.correlationId = correlationId;
        this.history = history;
        this.reporter = reporter;
        this.source = source;
        this.destination = destination;
        this.edge = edge;
    }

    /**
     * Will add the edge.id to the current history and format the history as edge ids in a comma separated string.
     *
     * @return the history to be used with the next FlowEvent.
     */
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

    public String toJson() {
        return new Gson().toJson(this);
    }

    public String getFlowEventVersion() {
        return flowEventVersion;
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
}
