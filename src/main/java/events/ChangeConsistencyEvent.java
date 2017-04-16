package events;

import consistencyComputingMethods.ConsistencyComputeMethod;
import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

public class ChangeConsistencyEvent extends Event {
	Goal criterium;
	ConsistencyComputeMethod ccm;

	public static final EventType<ChangeConsistencyEvent> CHANGED_SINGLE_CONSISTENCY = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED SINGLE CONSISTENCY");
	public static final EventType<ChangeConsistencyEvent> CHANGED_MAX_CONSISTENCY = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED MAX CONSISTENCY");
	public static final EventType<ChangeConsistencyEvent> CHANGED_NUBER_OF_ALTERNATIVES = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED NUBER OF CRITERIA");
	public static final EventType<ChangeConsistencyEvent> CHANGED_CONSISTENCY_METHOD = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED CONSISTENCY METHOD");

	public ChangeConsistencyEvent(EventType<ChangeConsistencyEvent> eventType, Goal criterium) {
		super(eventType);
		this.criterium = criterium;
	}

	public ChangeConsistencyEvent(EventType<ChangeConsistencyEvent> eventType, ConsistencyComputeMethod ccm) {
		super(eventType);
		this.ccm = ccm;
	}

	public ChangeConsistencyEvent(EventType<ChangeConsistencyEvent> eventType) {
		super(eventType);
	}

	public ConsistencyComputeMethod getCcm() {
		return ccm;
	}

}