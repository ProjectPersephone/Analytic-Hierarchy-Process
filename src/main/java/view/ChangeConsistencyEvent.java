package view;

import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

class ChangeConsistencyEvent extends Event {
	Goal criterium;

	public static final EventType<ChangeConsistencyEvent> CHANGED_SINGLE_CONSISTENCY = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED SINGLE CONSISTENCY");
	public static final EventType<ChangeConsistencyEvent> CHANGED_MAX_CONSISTENCY = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED MAX CONSISTENCY");
	public static final EventType<ChangeConsistencyEvent> CHANGED_NUBER_OF_CRITERIA = new EventType<ChangeConsistencyEvent>(
			ANY, "CHANGED NUBER OF CRITERIA");

	public ChangeConsistencyEvent(EventType<ChangeConsistencyEvent> eventType, Goal criterium) {
		super(eventType);
		this.criterium = criterium;
	}
	
	public ChangeConsistencyEvent(EventType<ChangeConsistencyEvent> eventType) {
		super(eventType);
	}
}