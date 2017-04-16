package events;

import javafx.event.Event;
import javafx.event.EventType;
import model.Goal;

public class EnteredValue extends Event {
	
	private Goal criterium;
	
	public static final EventType<EnteredValue> COMPARATION_VALUE_CHANGED = new EventType<EnteredValue>(ANY,
			"COMPARATION VALUE CHANGED");

	public EnteredValue(EventType<? extends Event> eventType, Goal criterium) {
		super(eventType);
		this.criterium = criterium;
	}
	public Goal getCriterium() {
		return criterium;
	}
}