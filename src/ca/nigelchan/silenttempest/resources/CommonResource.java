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
	
	public static final int BUTTON_CANCEL = 2;
	public static final int BUTTON_CANCEL_ACTIVE = 3;
	public static final int BUTTON_OK = 0;
	public static final int BUTTON_OK_ACTIVE = 1;
	public static final int BUTTON_EXIT = 8;
	public static final int BUTTON_EXIT_ACTIVE = 9;
	public static final int BUTTON_RESTART = 6;
	public static final int BUTTON_RESTART_ACTIVE = 7;
	public static final int BUTTON_PLAY = 10;
	public static final int BUTTON_PLAY_ACTIVE = 11;
	public static final int BUTTON_SETTINGS = 4;
	public static final int BUTTON_SETTINGS_ACTIVE = 5;

	private ITiledTextureRegion buttons;
	private Font headerFont;
	private ITiledTextureRegion dialog;
	private ITextureRegion headerArrow;
	private ITextureRegion rain;

	public CommonResource(BaseGameActivity activity) {
		super(activity);
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		SVGBitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/common/");
		createButtons();
		createDialogTextureRegion();
		createHeaderArrow();
		createRain();
		
		FontFactory.setAssetBasePath("fnt/");
		createHeaderFont();
	}
	
	private void createButtons() {
		boolean useLarge = isLargeScreen();
		int width = useLarge ? getDPI() * 4 : (getDPI() * 2);
		int height = useLarge ? getDPI() * 3 : (getDPI() * 3 / 2);
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		buttons = SVGBitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
			atlas,
			activity,
			"buttons.svg",
			width,
			height,
			4,
			3
		);
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
		BitmapTextureAtlas headerFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
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
	
	private void createRain() {
		int width = Math.max(getDPI() / 50, 1);
		int height = getDPI() / 5;
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
			activity.getTextureManager(),
			width,
			height,
			TextureOptions.DEFAULT
		);
		addAtlas(atlas);
		rain = SVGBitmapTextureAtlasTextureRegionFactory.createFromAsset(
			atlas,
			activity,
			"rain.svg",
			width,
			height
		);
	}
	
	// Getters
	public ITiledTextureRegion getButtons() {
		return buttons;
	}

	public ITiledTextureRegion getDialog() {
		return dialog;
	}
	
	public ITextureRegion getHeaderArrow() {
		return headerArrow;
	}

	public Font getHeaderFont() {
		return headerFont;
	}
	
	public ITextureRegion getRain() {
		return rain;
	}

}
