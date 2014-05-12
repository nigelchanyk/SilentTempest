package ca.nigelchan.silenttempest.importer;

import java.io.IOException;

import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.nigelchan.silenttempest.data.EventsData;
import ca.nigelchan.silenttempest.data.events.AbductionTutorial;
import ca.nigelchan.silenttempest.data.events.ApproachMission;
import ca.nigelchan.silenttempest.data.events.MoveTutorial;
import ca.nigelchan.silenttempest.util.Coordinate;

public class EventImporter {
	
	public static EventsData load(String file, BaseGameActivity activity) throws IOException, JSONException {
		JSONObject json = Importer.load(file, activity);
		EventsData data = new EventsData();
		
		JSONArray events = json.getJSONArray("events");
		for (int i = 0; i < events.length(); ++i) {
			JSONObject event = events.getJSONObject(i);
			String type = event.getString("type");
			if (type.equals("approach")) {
				data.addEventData(
					new ApproachMission(
						Coordinate.fromJSONObject(event).toCenterVector2(),
						(float)event.getDouble("radius"),
						event.getString("instruction")
					)
				);
			}
			else if (type.equals("move_tutorial")) {
				data.addEventData(new MoveTutorial(Coordinate.fromJSONObject(event)));
			}
			else if (type.equals("abduction_tutorial")) {
				data.addEventData(
					new AbductionTutorial(
						event.getString("enemy"),
						Coordinate.fromJSONObject(event.getJSONObject("approach")),
						Coordinate.fromJSONObject(event.getJSONObject("destination"))
					)
				);
			}
		}
		
		return data;
	}

}
