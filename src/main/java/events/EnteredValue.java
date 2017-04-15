package events;

import javafx.event.Event;
import javafx.event.EventType;

public class EnteredValue extends Event {
	
	public static final EventType<EnteredValue> COMPARATION_VALUE_CHANGED = new EventType<EnteredValue>(ANY,
			"COMPARATION VALUE CHANGED");
	public EnteredValue(EventType<? extends Event> eventType) {
		super(eventType);
	}

}