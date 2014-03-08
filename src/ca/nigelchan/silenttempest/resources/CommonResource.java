package ca.nigelchan.silenttempest.resources;

import org.andengine.extension.svg.opengl.texture.atlas.bitmap.SVGBitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;

public class CommonResource extends Resource {

	private Font headerFont;
	private ITiledTextureRegion dialog;
	private ITextureRegion headerArrow;

	public CommonResource(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/common/");
		createDialogTextureRegion();
		createHeaderArrow();
		
		FontFactory.setAssetBasePath("fnt/");
		createHeaderFont();
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
	
	private void createHeaderArrow() {
		int width = getDPI() * 6 / 25;
		int height = getDPI() * 3 / 25;
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		headerArrow = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"header_arrow.svg",
			width,
			height
        );
	}
	
	private void createHeaderFont() {
		BitmapTextureAtlas headerFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		headerFont = FontFactory.createFromAsset(
			activity.getFontManager(),
			headerFontTexture,
			activity.getAssets(),
			"DancingScript.ttf",
			Math.min(getDPI() * 2 / 5, 128),
			true,
			Color.WHITE
		);
	    headerFont.load();
	    addAtlas(headerFontTexture);
	}
	
	// Getters
	public ITiledTextureRegion getDialog() {
		return dialog;
	}
	
	public ITextureRegion getHeaderArrow() {
		return headerArrow;
	}

	public Font getHeaderFont() {
		return headerFont;
	}

}
