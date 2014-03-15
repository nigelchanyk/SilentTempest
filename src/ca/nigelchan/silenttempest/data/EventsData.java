package ca.nigelchan.silenttempest.data;

import java.util.ArrayList;

import ca.nigelchan.silenttempest.data.events.IEventData;

public class EventsData {
	
	private ArrayList<IEventData> eventDataList = new ArrayList<IEventData>();
	
	public void addEventData(IEventData eventData) {
		eventDataList.add(eventData);
	}
	
	public Iterable<IEventData> getAllEventData() {
		return eventDataList;
	}

}
