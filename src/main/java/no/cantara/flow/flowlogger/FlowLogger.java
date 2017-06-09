package no.cantara.flow.flowlogger;

import no.cantara.flow.flowlogger.event.Builder;

public class FlowLogger {

    public static Builder.EdgeBuilder event() {
        return new Builder().event();
    }

    public static Builder.EdgeBuilder event(String correlationId) {
        return new Builder().event().correlationId(correlationId);
    }

    public static Builder.EdgeBuilder event(String correlationId, String history) {
        return new Builder().event().correlationId(correlationId).history(history);
    }
}
