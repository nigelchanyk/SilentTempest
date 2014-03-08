package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class CommonResource extends Resource {

	private ITiledTextureRegion dialog;

	public CommonResource(BaseGameActivity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/common/");
		createDialogTextureRegion();
	}
	
	private void createDialogTextureRegion() {
		BuildableBitmapTextureAtlas dialogAtlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			256,
			256,
			TextureOptions.DEFAULT
		);
		addAtlas(dialogAtlas);
		dialog = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
            dialogAtlas,
            activity,
            "dialog.svg",
            getDPI() * 3 / 4,
            getDPI() * 3 / 4,
            3,
            3
        );
	}
	
	// Getters
	public ITiledTextureRegion getDialog() {
		return dialog;
	}

}
