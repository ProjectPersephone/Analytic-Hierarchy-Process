package view;

import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

class ChangeConsistencyEvent extends Event {

	public static final EventType<ChangeConsistencyEvent> CHANGED_CONSISTENCY = new EventType<ChangeConsistencyEvent>(ANY, "CHANGED CONSISTENCY");

	public ChangeConsistencyEvent() {
		super(ChangeConsistencyEvent.CHANGED_CONSISTENCY);
	}
}