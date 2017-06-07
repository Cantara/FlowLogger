package no.cantara.flow.flowlogger;

public class FlowLogger {

    public static FlowEvent.Builder.EdgeBuilder event() {
        return new FlowEvent.Builder().event();
    }

    public static FlowEvent.Builder.EdgeBuilder event(String correlationId) {
        return new FlowEvent.Builder().event().correlationId(correlationId);
    }

    public static FlowEvent.Builder.EdgeBuilder event(String correlationId, String history) {
        return new FlowEvent.Builder().event().correlationId(correlationId).history(history);
    }
}
