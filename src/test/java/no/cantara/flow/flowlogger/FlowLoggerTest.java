package no.cantara.flow.flowlogger;

import com.google.gson.Gson;
import no.cantara.flow.flowlogger.event.FlowEvent;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static no.cantara.flow.flowlogger.FlowLogger.event;

public class FlowLoggerTest {

    @Test
    public void thatMinimalFlowEventWorks() {
        FlowEvent event = event("m1").id("A").build();

        Assert.assertEquals(event.getCorrelationId(), "m1");
        Assert.assertEquals(event.getEdge().getId(), "A");
        Assert.assertNotNull(event.getEdge().getTimestamp());
        Assert.assertEquals(event.getEdge().getStatus(), "OK"); // default
    }

    @Test
    public void thatPathWorksWithoutHistory() {
        FlowEvent event = event("h1").id("A").build();

        Assert.assertEquals(event.nextHistory(), "A");
    }

    @Test
    public void thatPathWorksWithOneElementInHistory() {
        FlowEvent event = event("h1", "A").id("B").build();

        Assert.assertEquals(event.nextHistory(), "A,B");
    }

    @Test
    public void thatPathWorksWithMultipleElementsInHistory() {
        FlowEvent event = event("h1", "A,B,C").id("D").build();

        Assert.assertEquals(event.nextHistory(), "A,B,C,D");
    }

    @Test
    public void thatFlowEventIsAssignedAllFieldsWithCorrectValues() {
        FlowEvent event = event("abc123", "A,B")
                .reporter()
                .deployment("test")
                .name("unit-test")
                .version("1.0")
                .containerId("no container")
                .instance("the only instance")
                .source()
                .type("ether")
                .address("void")
                .payloadType("fubarsource")
                .payloadId("pid2source")
                .destination()
                .type("internal")
                .address("testng-value-test")
                .payloadType("fubar")
                .payloadId("pid2")
                .edge()
                .id("C")
                .timestamp(ZonedDateTime.of(2017, 6, 7, 10, 44, 31, 0, ZoneId.of("Europe/Oslo")))
                .milestone("event-values-test")
                .status("test-ok")
                .errorType("no-error")
                .errorReason("none")
                .comment("some comment")
                .build();

        Assert.assertEquals(event.getCorrelationId(), "abc123");
        Assert.assertEquals(event.getHistory(), new String[]{"A", "B"});
        Assert.assertEquals(event.getReporter().getDeployment(), "test");
        Assert.assertEquals(event.getReporter().getName(), "unit-test");
        Assert.assertEquals(event.getReporter().getVersion(), "1.0");
        Assert.assertEquals(event.getReporter().getContainerId(), "no container");
        Assert.assertEquals(event.getReporter().getInstance(), "the only instance");
        Assert.assertEquals(event.getSource().getType(), "ether");
        Assert.assertEquals(event.getSource().getAddress(), "void");
        Assert.assertEquals(event.getSource().getPayloadType(), "fubarsource");
        Assert.assertEquals(event.getSource().getPayloadId(), "pid2source");
        Assert.assertEquals(event.getDestination().getType(), "internal");
        Assert.assertEquals(event.getDestination().getAddress(), "testng-value-test");
        Assert.assertEquals(event.getDestination().getPayloadType(), "fubar");
        Assert.assertEquals(event.getDestination().getPayloadId(), "pid2");
        Assert.assertEquals(event.getEdge().getId(), "C");
        Assert.assertEquals(event.getEdge().getTimestamp(), "2017-06-07T10:44:31+02:00");
        Assert.assertEquals(event.getEdge().getMilestone(), "event-values-test");
        Assert.assertEquals(event.getEdge().getStatus(), "test-ok");
        Assert.assertEquals(event.getEdge().getErrorType(), "no-error");
        Assert.assertEquals(event.getEdge().getErrorReason(), "none");
        Assert.assertEquals(event.getEdge().getComment(), "some comment");
    }

    @Test
    public void thatFlowEventSerializationWorks() throws JSONException {
        FlowEvent event = event("abc123", "A,B")
                .reporter()
                .deployment("test")
                .name("unit-test")
                .version("1.0")
                .containerId("no container")
                .instance("the only instance")
                .source().type("ether").address("void")
                .destination().type("internal").address("testng-value-test").payloadType("fubar").payloadId("pid2")
                .edge()
                .id("C")
                .timestamp(ZonedDateTime.of(2017, 6, 7, 10, 44, 31, 0, ZoneId.of("Europe/Oslo")))
                .status("test-ok")
                .milestone("event-values-test")
                .errorType("no-error")
                .errorReason("none")
                .comment("some comment")
                .build();

        String json1 = event.toJson();
        FlowEvent copyEvent = new Gson().fromJson(json1, FlowEvent.class);
        String json2 = copyEvent.toJson();
        JSONAssert.assertEquals(json1, json2, true);
    }

    @Test
    public void thatGenerateEventIdProduceFourCharacters() {
        String id = event("c67").generateId().build().getEdge().getId();
        Assert.assertTrue(id.length() == 5);
    }

    @Test
    public void thatGenerateCorrelationIdProduceUuids() {
        String correlationId = event().generateCorrelationId().id("i42").build().getCorrelationId();
        UUID.fromString(correlationId);
    }

    @Test
    public void thatFlowLoggingHasAnEasyToUseAPI() {
        String correlationId = "corrId382"; // given correlationId
        String flowHistory = "A,B,C"; // given graph history

        FlowEvent event = event(correlationId, flowHistory).id("D").build(); // generate event

        System.out.println(String.format("%s", event.toJson())); // log event

        flowHistory = event.nextHistory(); // update history

        flowHistory.toString(); // pass history onto next processing step.
    }
}
