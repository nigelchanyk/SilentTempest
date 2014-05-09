package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.silenttempest.util.Vector2;

public class MainMenuResource extends Resource {
	
	public static final Vector2 LEFT_NIGHT_VISION_UV = new Vector2(0.317935f, 0.224717f);
	public static final Vector2 RIGHT_NIGHT_VISION_UV = new Vector2(0.535327f, 0.224717f);

	private static final float NIGHT_VISION_HEIGHT_RATIO = 0.115826f;
	private static final float PENGUIN_RELATIVE_HEIGHT = 0.7f;
	
	private ITiledTextureRegion actButtons;
	private ITextureRegion nightVisionGlow;
	private ITextureRegion penguin;
	private ITextureRegion penguinShine;
	private ITiledTextureRegion sceneButtons;

	public MainMenuResource(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	public void onLoad() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/mainmenu/");
		createActButtons();
		createPenguin();
		createNightVisionGlow();
		createSceneButtons();
	}
	
	private void createActButtons() {
		int size = getDPI() * 2 / 3;
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			size * 2,
			size * 4,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		actButtons = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			atlas,
			activity,
			"act_buttons.svg",
			size * 2,
			size * 4,
			2,
			4
		);
	}
	
	private void createPenguin() {
		int height = (int)(getScreenHeight() * PENGUIN_RELATIVE_HEIGHT);
		int width = (int)(getScreenHeight() * PENGUIN_RELATIVE_HEIGHT * PENGUIN_ASPECT_RATIO);
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		penguin = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"penguin.svg",
			width,
			height
		);
		addAtlas(atlas);

		atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		penguinShine = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"penguin_shine.svg",
			width,
			height
		);
		addAtlas(atlas);
	}
	
	private void createNightVisionGlow() {
		int dimension = (int)(getScreenHeight() * PENGUIN_RELATIVE_HEIGHT * NIGHT_VISION_HEIGHT_RATIO);
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			dimension,
			dimension,
			TextureOptions.DEFAULT
		);
		nightVisionGlow = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"night_vision_glow.svg",
			dimension,
			dimension
		);
		addAtlas(atlas);
	}
	
	private void createSceneButtons() {
		int size = getDPI() * 2 / 3;
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			size * 2,
			size * 9,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		sceneButtons = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			atlas,
			activity,
			"scene_buttons.svg",
			size * 2,
			size * 9,
			2,
			9
		);
	}

	// Getters
	public ITiledTextureRegion getActButtons() {
		return actButtons;
	}

	public ITextureRegion getNightVisionGlow() {
		return nightVisionGlow;
	}

	public ITextureRegion getPenguin() {
		return penguin;
	}

	public ITextureRegion getPenguinShine() {
		return penguinShine;
	}

	public ITiledTextureRegion getSceneButtons() {
		return sceneButtons;
	}

}
