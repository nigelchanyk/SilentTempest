package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class SplashResource extends Resource {

	private ITextureRegion splashTextureRegion;

	public SplashResource(BaseGameActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onLoad() {
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/splash/");
		int width = getScreenWidth() * 2 / 3;
		int height = width * 15 / 70;
		BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		addAtlas(splashTextureAtlas);
		splashTextureRegion = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			splashTextureAtlas,
			activity,
			"title.svg",
			width,
			height,
			0,
			0
		);
	}

	public ITextureRegion getSplashTextureRegion() {
		return splashTextureRegion;
	}
}
