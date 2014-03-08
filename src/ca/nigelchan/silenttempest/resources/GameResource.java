package ca.nigelchan.silenttempest.resources;

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
	
	private ITextureRegion alertIndicator;
	private ITiledTextureRegion fieldTextureRegion;
	private int fieldTileSize;
	private ITextureRegion joystickDisplay;
	private ITextureRegion monkeyBaseTextureRegion;

	@Override
	public void onLoad() {
		computeSize(activity);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		createFieldTexture();
		createMonkeyTexture();
		createJoystickTexture();
		createIndicatorTexture();
	}
	
	private void computeSize(BaseGameActivity activity) {
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		// Be able to see 5 tiles above/below
		// Therefore, 2 * 5 + 1
		fieldTileSize = metrics.heightPixels / 11;
	}
	
	// Create buildable bitmap texture atlas by grid width and height
	private BuildableBitmapTextureAtlas createBuildableAtlas(int width, int height) {
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			fieldTileSize * width,
			fieldTileSize * height,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		return atlas;
	}
	
	private void createFieldTexture() {
		BuildableBitmapTextureAtlas fieldTextureAtlas = createBuildableAtlas(
			FIELD_SPRITE_WIDTH,
			FIELD_SPRITE_HEIGHT
		);
		fieldTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			fieldTextureAtlas,
			activity,
			"field.svg",
			fieldTileSize * FIELD_SPRITE_WIDTH,
			fieldTileSize * FIELD_SPRITE_HEIGHT,
			5,
			5
		);
	}
	
	private void createJoystickTexture() {
		BuildableBitmapTextureAtlas joystickAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			getDPI(),
			getDPI(),
			TextureOptions.DEFAULT
		);
		addAtlas(joystickAtlas);
		joystickDisplay = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			joystickAtlas,
			activity,
			"joystick.svg",
			getDPI(),
			getDPI()
        );
	}
	
	private void createIndicatorTexture() {
		BuildableBitmapTextureAtlas indicatorAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			fieldTileSize * 4,
			fieldTileSize * 2,
			TextureOptions.DEFAULT
		);
		addAtlas(indicatorAtlas);
		alertIndicator = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			indicatorAtlas,
			activity,
			"alert_indicator.svg",
			fieldTileSize * 4,
			fieldTileSize * 2
		);
	}
	
	private void createMonkeyTexture() {
		BuildableBitmapTextureAtlas monkeyBaseTextureAtlas = createBuildableAtlas(1, 1);
		monkeyBaseTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			monkeyBaseTextureAtlas,
			activity,
			"monkey_base.svg",
			fieldTileSize,
			fieldTileSize
        );
	}

	// Getters
	public ITextureRegion getAlertIndicator() {
		return alertIndicator;
	}

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
