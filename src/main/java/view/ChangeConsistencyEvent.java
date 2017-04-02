package view;

import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

class ChangeConsistencyEvent extends Event {

	private Goal goal;

	public static final EventType<ChangeConsistencyEvent> CHANGED_CONSISTENCY = new EventType<ChangeConsistencyEvent>(ANY, "CHANGED CONSISTENCY");

	public ChangeConsistencyEvent(Goal goal) {
		super(ChangeConsistencyEvent.CHANGED_CONSISTENCY);
		this.goal = goal;
	}

	public Goal getGoal() {
		return goal;
	}

}