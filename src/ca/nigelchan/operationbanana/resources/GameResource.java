package ca.nigelchan.operationbanana.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;

public class GameResource extends Resource {

	public static final int FIELD_SPRITE_HEIGHT = 5;
	public static final int FIELD_SPRITE_WIDTH = 5;
	
	public GameResource(BaseGameActivity activity) {
		super(activity);
	}
	
	private int dpi;
	private ITiledTextureRegion fieldTextureRegion;
	private int fieldTileSize;
	private ITextureRegion joystickDisplay;
	private ITextureRegion monkeyBaseTextureRegion;

	@Override
	public void onLoad() {
		computeSize(activity);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		BuildableBitmapTextureAtlas fieldTextureAtlas = createBuildableAtlas(
			FIELD_SPRITE_WIDTH,
			FIELD_SPRITE_HEIGHT
		);
		BuildableBitmapTextureAtlas monkeyBaseTextureAtlas = createBuildableAtlas(1, 1);
		BuildableBitmapTextureAtlas joystickAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			getTextureSize(dpi),
			getTextureSize(dpi),
			TextureOptions.DEFAULT
		);
		addAtlas(joystickAtlas);


		fieldTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			fieldTextureAtlas,
			activity,
			"field.svg",
			fieldTileSize * FIELD_SPRITE_WIDTH,
			fieldTileSize * FIELD_SPRITE_HEIGHT,
			5,
			5
		);
		monkeyBaseTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			monkeyBaseTextureAtlas,
			activity,
			"monkey_base.svg",
			fieldTileSize,
			fieldTileSize
        );
		joystickDisplay = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			joystickAtlas,
			activity,
			"joystick.svg",
			dpi,
			dpi
        );
	}
	
	private void computeSize(BaseGameActivity activity) {
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		// Be able to see 5 tiles above/below
		// Therefore, 2 * 5 + 1
		fieldTileSize = metrics.heightPixels / 11;
		dpi = metrics.densityDpi;
	}
	
	// Create buildable bitmap texture atlas by grid width and height
	private BuildableBitmapTextureAtlas createBuildableAtlas(int width, int height) {
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			getTextureSize(fieldTileSize * width),
			getTextureSize(fieldTileSize * height),
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		return atlas;
	}
	
	private int getTextureSize(int requiredSize) {
		if (requiredSize > 8192)
			throw new IllegalArgumentException("Seriously? You want texture size of " + requiredSize + "?");

		for (int i = 2; ; i *= 2) {
			if (i >= requiredSize)
				return i;
		}
	}

	// Getters
	public ITiledTextureRegion getFieldTextureRegion() {
		return fieldTextureRegion;
	}

	public int getFieldTileSize() {
		return fieldTileSize;
	}
	
	public ITextureRegion getJoystickDisplay() {
		return joystickDisplay;
	}
	
	public ITextureRegion getMonkeyBase() {
		return monkeyBaseTextureRegion;
	}
}
