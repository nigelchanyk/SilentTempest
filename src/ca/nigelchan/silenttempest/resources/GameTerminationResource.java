package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import ca.nigelchan.silenttempest.scenes.GameTerminationScene;

public class GameTerminationResource extends Resource {

	private static final float PENGUIN_RELATIVE_HEIGHT = 0.7f;
	
	private ITextureRegion penguin;
	private GameTerminationScene.Mode mode;
	
	public GameTerminationResource(BaseGameActivity activity, GameTerminationScene.Mode mode) {
		super(activity);
		this.mode = mode;
	}

	@Override
	protected void onLoad() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/gametermination/");
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
			mode == GameTerminationScene.Mode.SUCCESS ? "penguin_success.svg" : "penguin_failure.svg",
			width,
			height
		);
		addAtlas(atlas);
	}

	public ITextureRegion getPenguin() {
		return penguin;
	}

}
