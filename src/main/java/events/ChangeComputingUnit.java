package events;

import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

public class ChangeComputingUnit extends Event {

	public static final EventType<ChangeComputingUnit> CHANGED_COMPUTING_BRANCH = new EventType<ChangeComputingUnit>(
			ANY, "CHANGED COMPUTING BRANCH");

	public ChangeComputingUnit(EventType<ChangeComputingUnit> eventType) {
		super(eventType);
	}
}