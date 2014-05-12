package ca.nigelchan.silenttempest.importer;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ca.nigelchan.silenttempest.data.WorldData;
import ca.nigelchan.silenttempest.data.actors.ActorConfiguration;
import ca.nigelchan.silenttempest.data.actors.EnemyData;
import ca.nigelchan.silenttempest.data.actors.PlayerData;
import ca.nigelchan.silenttempest.data.actors.sequences.MoveData;
import ca.nigelchan.silenttempest.data.actors.sequences.SequenceDataList;
import ca.nigelchan.silenttempest.data.actors.sequences.TurnData;
import ca.nigelchan.silenttempest.data.actors.sequences.WaitData;
import ca.nigelchan.silenttempest.data.actors.traps.LaserData;
import ca.nigelchan.silenttempest.data.actors.traps.SawBladeData;
import ca.nigelchan.silenttempest.data.layers.ActorLayerData;
import ca.nigelchan.silenttempest.data.layers.FieldLayerData;
import ca.nigelchan.silenttempest.data.layers.TileTemplateCollection;
import ca.nigelchan.silenttempest.util.Coordinate;
import ca.nigelchan.silenttempest.util.Direction;
import ca.nigelchan.silenttempest.util.MathHelper;
import ca.nigelchan.silenttempest.util.Vector2;

public class WorldImporter {
	
	private static final Pattern LAYER_REGEX = Pattern.compile("^(?:(?:([a-z0-9]+)>)|())([a-z0-9]*)(?:(?::([a-z0-9]+))|())$");
	private static final Pattern MOVE_REGEX = Pattern.compile("^([nesw])(?:\\(([0-9]+)\\))?$");
	private static final Pattern TURN_REGEX = Pattern.compile("^t([nesw])$");
	private static final Pattern WAIT_REGEX = Pattern.compile("^w\\(([0-9]+)\\)$");
	private static final HashMap<String, Direction> DIRECTION_MAPPER = new HashMap<String, Direction>();
	
	static {
		DIRECTION_MAPPER.put("n", Direction.NORTH);
		DIRECTION_MAPPER.put("e", Direction.EAST);
		DIRECTION_MAPPER.put("s", Direction.SOUTH);
		DIRECTION_MAPPER.put("w", Direction.WEST);
	}
	
	public static WorldData load(
		String file,
		BaseGameActivity activity,
		ActorConfiguration actorConfiguration
	) throws IOException, JSONException {
		JSONObject json = Importer.load(file, activity);
		int rows = json.getInt("rows");
		int columns = json.getInt("columns");
		JSONArray ground = json.getJSONArray("ground");
		JSONArray top = json.getJSONArray("top");
		JSONObject player = json.getJSONObject("player");
		JSONArray enemies = json.optJSONArray("enemies");
		JSONArray sawBlades = json.optJSONArray("saw_blades");
		JSONArray laser = json.optJSONArray("laser");
		TileTemplateCollection tiles = TileTemplateCollection.instance();
		
		WorldData data = new WorldData(columns, rows);
		for (int i = 0; i < ground.length(); ++i)
			data.addLayer(parseLayer(tiles, ground.getJSONObject(i), rows, columns));

		PlayerData playerData = new PlayerData(Coordinate.fromJSONObject(player), 0, actorConfiguration.getPlayerSpeed());
		ActorLayerData actorLayer = new ActorLayerData(columns, rows, playerData);
		if (enemies != null) {
			for (int i = 0; i < enemies.length(); ++i)
				actorLayer.addEnemy(parseEnemy(enemies.getJSONObject(i), actorConfiguration));
		}
		
		if (sawBlades != null) {
			for (int i = 0; i < sawBlades.length(); ++i)
				actorLayer.addTrap(parseSawBlade(sawBlades.getJSONObject(i)));
		}
		
		if (laser != null) {
			for (int i = 0; i < laser.length(); ++i)
				actorLayer.addTrap(parseLaserData(laser.getJSONObject(i)));
		}

		data.addLayer(actorLayer);
		for (int i = 0; i < top.length(); ++i)
			data.addLayer(parseLayer(tiles, top.getJSONObject(i), rows, columns));
		return data;
	}
	
	public static EnemyData parseEnemy(JSONObject json, ActorConfiguration actorConfiguration) throws JSONException {
		EnemyData data = new EnemyData(
			Coordinate.fromJSONObject(json),
			MathHelper.getRotation(
				Enum.valueOf(
					Direction.class,
					json.optString("direction", Direction.NORTH.toString()).toUpperCase()
				)
			),
			actorConfiguration.getEnemySpeed(),
			json.getInt("range"),
			json.optString("id")
		);
		parseSequenceDataList(json, data.getSequenceList());
		return data;
	}
	
	public static SawBladeData parseSawBlade(JSONObject json) throws JSONException {
		SawBladeData data = new SawBladeData(
			Vector2.fromJSONObject(json),
			(float)json.getDouble("speed"),
			SawBladeData.Size.valueOf(json.getString("size"))
		);
		parseSequenceDataList(json, data.getSequenceList());
		return data;
	}
	
	public static LaserData parseLaserData(JSONObject json) throws JSONException {
		LaserData data = new LaserData(
			Vector2.fromJSONObject(json),
			Direction.valueOf(json.getString("direction")),
			(float)json.getDouble("speed"),
			(float)json.getDouble("rotation_speed")
		);
		parseSequenceDataList(json, data.getSequenceList());
		return data;
	}
	
	public static FieldLayerData parseLayer(
		TileTemplateCollection tiles,
		JSONObject json,
		int rows,
		int columns
		) throws JSONException {
		int defaultTile = json.optInt("default", -1);
		FieldLayerData data = new FieldLayerData(columns, rows, defaultTile >= 0 ? tiles.get(defaultTile) : null);
		int total = rows * columns;
		String[] tokens = json.getString("layout").split(";");
		for (int i = 0, j = 0; i < tokens.length && j < total; ++i) {
			Matcher m = LAYER_REGEX.matcher(tokens[i]);
			if (!m.matches())
				throw new JSONException("Unable to decompress layer.");
			int skip = m.group(1) == null ? 0 : Integer.parseInt(m.group(1), 36);
			int index = m.group(3).equals("") ? -1 : Integer.parseInt(m.group(3), 36);
			int repeat = m.group(4) == null ? 0 : Integer.parseInt(m.group(4), 36);
			j += skip;
			for (int k = 0; k <= repeat && j < total; ++j, ++k)
				data.setTile(j / columns, j % columns, tiles.get(index));
		}
		return data;
	}
	
	private static void parseSequenceDataList(JSONObject json, SequenceDataList data) throws JSONException {
		String raw = json.getString("pattern");
		if (raw.isEmpty())
			return;
		String[] tokens = raw.split(";");
		for (String token : tokens) {
			Matcher m = MOVE_REGEX.matcher(token);
			if (m.matches()) {
				Direction direction = DIRECTION_MAPPER.get(m.group(1));
				int total = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
				for (int i = 0; i < total; ++i)
					data.addSequenceItem(new MoveData(direction));
				continue;
			}
			m = TURN_REGEX.matcher(token);
			if (m.matches()) {
				data.addSequenceItem(new TurnData(DIRECTION_MAPPER.get(m.group(1))));
				continue;
			}
			m = WAIT_REGEX.matcher(token);
			if (m.matches()) {
				// Must convert from milliseconds to seconds
				data.addSequenceItem(new WaitData(Float.parseFloat(m.group(1)) / 1000));
				continue;
			}
			
			throw new JSONException("Unable to parse enemy pattern.");
		}
	}

}
