package ca.nigelchan.silenttempest.resources;

import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.silenttempest.data.actors.traps.SawBladeData;
import android.util.DisplayMetrics;

public class GameResource extends Resource {

	public static final int MAX_BEACON_SIZE = 4;
	public static final int FIELD_SPRITE_HEIGHT = 5;
	public static final int FIELD_SPRITE_WIDTH = 5;
	public static final int LARGE_SAW_BLADE = 0;
	public static final int MEDIUM_SAW_BLADE = 1;
	public static final int SMALL_SAW_BLADE = 2;
	
	public GameResource(BaseGameActivity activity) {
		super(activity);
	}
	
	private ITextureRegion alertIndicator;
	private ITextureRegion beacon;
	private BuildableBitmapTextureAtlas fieldAtlas;
	private ITiledTextureRegion fieldTextureRegion;
	private int fieldTileSize;
	private ITextureRegion joystickDisplay;
	private ITextureRegion laserBeam;
	private ITextureRegion laserBeamCap;
	private ITextureRegion laserCannon;
	private ITextureRegion monkeyBaseTextureRegion;
	private ITextureRegion[] sawBlades = new ITextureRegion[SawBladeData.Size.values().length];
	private ITextureRegion star;
	private BuildableBitmapTextureAtlas starAtlas;
	
	public SpriteGroup createFieldSpriteGroup(int capacity) {
		return new SpriteGroup(fieldAtlas, capacity, getVertexBufferObjectManager());
	}
	
	public SpriteGroup createStarAtlas(int capacity) {
		return new SpriteGroup(starAtlas, capacity, getVertexBufferObjectManager());
	}

	@Override
	public void onLoad() {
		computeSize(activity);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

		createBeaconTexture();
		createFieldTexture();
		createIndicatorTexture();
		createJoystickTexture();
		createLaser();
		createMonkeyTexture();
		createSawBlades();
		createStar();
	}
	
	private void computeSize(BaseGameActivity activity) {
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		// Be able to see 5 tiles above/below
		// Therefore, 2 * 5 + 1
		fieldTileSize = metrics.heightPixels / 11;
	}
	
	private void createBeaconTexture() {
		BuildableBitmapTextureAtlas atlas = createBuildableAtlas(MAX_BEACON_SIZE, MAX_BEACON_SIZE);
		beacon = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"beacon.svg",
			fieldTileSize * MAX_BEACON_SIZE,
			fieldTileSize * MAX_BEACON_SIZE
		);
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
		fieldAtlas = createBuildableAtlas(
			FIELD_SPRITE_WIDTH,
			FIELD_SPRITE_HEIGHT
		);
		fieldTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			fieldAtlas,
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
	
	private void createLaser() {
		int laserWidth = fieldTileSize / 2;
		BuildableBitmapTextureAtlas laserBeamAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			laserWidth,
			laserWidth,
			TextureOptions.DEFAULT
		);
		addAtlas(laserBeamAtlas);
		laserBeam = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			laserBeamAtlas,
			activity,
			"laser_beam.svg",
			laserWidth,
			laserWidth
		);

		BuildableBitmapTextureAtlas laserBeamCapAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			laserWidth,
			laserWidth / 5,
			TextureOptions.DEFAULT
		);
		addAtlas(laserBeamCapAtlas);
		laserBeamCap = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			laserBeamCapAtlas,
			activity,
			"laser_beam_cap.svg",
			laserWidth,
			laserWidth / 5
		);
		
		BuildableBitmapTextureAtlas laserCannonAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			laserWidth,
			fieldTileSize,
			TextureOptions.DEFAULT
		);
		addAtlas(laserCannonAtlas);
		laserCannon = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			laserCannonAtlas,
			activity,
			"laser_cannon.svg",
			laserWidth,
			fieldTileSize
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
	
	private void createSawBlades() {
		for (SawBladeData.Size size : SawBladeData.Size.values()) {
			int sizeValue = SawBladeData.diameter(size) * fieldTileSize;
			BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(),
				sizeValue,
				sizeValue,
				TextureOptions.DEFAULT
			);
			addAtlas(atlas);
			sawBlades[size.ordinal()] = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas,
				activity,
				"saw_blade.svg",
				sizeValue,
				sizeValue
			);
		}
	}
	
	private void createStar() {
		int size = fieldTileSize / 2;
		starAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			size,
			size,
			TextureOptions.DEFAULT
		);
		addAtlas(starAtlas);
		star = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			starAtlas,
			activity,
			"star.svg",
			size,
			size
		);
	}

	// Getters
	public ITextureRegion getAlertIndicator() {
		return alertIndicator;
	}
	
	public ITextureRegion getBeacon() {
		return beacon;
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
	
	public ITextureRegion getLaserBeam() {
		return laserBeam;
	}
	
	public ITextureRegion getLaserBeamCap() {
		return laserBeamCap;
	}

	public ITextureRegion getLaserCannon() {
		return laserCannon;
	}

	public ITextureRegion getMonkeyBase() {
		return monkeyBaseTextureRegion;
	}
	
	public ITextureRegion getSawBlade(SawBladeData.Size size) {
		return sawBlades[size.ordinal()];
	}

	public ITextureRegion getStar() {
		return star;
	}

}
