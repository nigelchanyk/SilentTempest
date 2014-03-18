package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class LoadingResource extends Resource {
	
	public static final float PENGUIN_INVERSE_ASPECT_RATIO = 620f / 740f;
	
	private ITiledTextureRegion penguin;

	public LoadingResource(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	public void onLoad() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/loading/");
		createPenguin();
	}
	
	private void createPenguin() {
		int width = getDPI() * 2;
		int height = (int)(getDPI() * PENGUIN_INVERSE_ASPECT_RATIO);
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height
		);
		addAtlas(atlas);
		penguin = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			atlas,
			activity,
			"penguin.svg",
			width,
			height,
			2,
			1
		);
	}
	
	// Getters
	public ITiledTextureRegion getPenguin() {
		return penguin;
	}
}
