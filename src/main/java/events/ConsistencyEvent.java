package events;

import consistencyComputingMethods.ConsistencyComputeMethod;
import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

public class ConsistencyEvent extends Event {
	Goal criterium;
	ConsistencyComputeMethod ccm;

	public static final EventType<ConsistencyEvent> CHANGED_SINGLE_CONSISTENCY = new EventType<ConsistencyEvent>(
			ANY, "CHANGED SINGLE CONSISTENCY");
	public static final EventType<ConsistencyEvent> CHANGED_MAX_CONSISTENCY = new EventType<ConsistencyEvent>(
			ANY, "CHANGED MAX CONSISTENCY");
	public static final EventType<ConsistencyEvent> CHANGED_NUBER_OF_ALTERNATIVES = new EventType<ConsistencyEvent>(
			ANY, "CHANGED NUBER OF CRITERIA");
	public static final EventType<ConsistencyEvent> CHANGED_CONSISTENCY_METHOD = new EventType<ConsistencyEvent>(
			ANY, "CHANGED CONSISTENCY METHOD");
	public static final EventType<ConsistencyEvent> CHECK_CONSISTENCY = new EventType<ConsistencyEvent>(
			ANY, "CHECK CONSISTENCY");

	public ConsistencyEvent(EventType<ConsistencyEvent> eventType, Goal criterium) {
		super(eventType);
		this.criterium = criterium;
	}

	public ConsistencyEvent(EventType<ConsistencyEvent> eventType, ConsistencyComputeMethod ccm) {
		super(eventType);
		this.ccm = ccm;
	}

	public ConsistencyEvent(EventType<ConsistencyEvent> eventType) {
		super(eventType);
	}

	public ConsistencyComputeMethod getCcm() {
		return ccm;
	}

}