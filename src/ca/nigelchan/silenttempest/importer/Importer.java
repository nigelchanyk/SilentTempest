package ca.nigelchan.silenttempest.importer;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class Importer {
	
	public static JSONObject load(String file, BaseGameActivity activity) throws IOException, JSONException {
		InputStream input = activity.getAssets().open(file);
		byte[] buffer = new byte[input.available()];
		input.read(buffer);
		input.close();
		return new JSONObject(new String(buffer, "UTF-8"));
	}

}
