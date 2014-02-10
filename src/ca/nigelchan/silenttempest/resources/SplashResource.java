package ca.nigelchan.silenttempest.resources;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class SplashResource extends Resource {

	private TextureRegion splashTextureRegion;

	public SplashResource(BaseGameActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLoad() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(
			activity.getTextureManager(),
			256,
			256,
			TextureOptions.DEFAULT
		);
		addAtlas(splashTextureAtlas);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
			splashTextureAtlas,
			activity,
			"splash.png",
			0,
			0
		);
	}

	public TextureRegion getSplashTextureRegion() {
		return splashTextureRegion;
	}
}
