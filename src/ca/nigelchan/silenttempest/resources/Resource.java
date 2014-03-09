package ca.nigelchan.silenttempest.resources;

import java.util.ArrayList;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;

public abstract class Resource {
	
	protected BaseGameActivity activity;
	
	private ArrayList<BitmapTextureAtlas> atlases = new ArrayList<BitmapTextureAtlas>(5);
	private ArrayList<BuildableBitmapTextureAtlas> buildableAtlases = new ArrayList<BuildableBitmapTextureAtlas>(5);
	private DisplayMetrics metrics;
	
	public Resource(BaseGameActivity activity) {
		this.activity = activity;
		metrics = activity.getResources().getDisplayMetrics();
	}

	public final void load() {
		onLoad();
		for (BitmapTextureAtlas atlas : atlases)
			atlas.load();
        try {
	        for (BuildableBitmapTextureAtlas atlas : buildableAtlases) {
                atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
                atlas.load();
	        }
        } catch (TextureAtlasBuilderException e) {
            e.printStackTrace();
        }
	}

	public final void unload() {
		for (BitmapTextureAtlas atlas : atlases)
			atlas.unload();
		for (BuildableBitmapTextureAtlas atlas : buildableAtlases)
			atlas.unload();
	}
	
	public boolean isLargeScreen() {
		return metrics.widthPixels / metrics.xdpi >= 5;
	}
	
	protected void addAtlas(BitmapTextureAtlas atlas) {
		atlases.add(atlas);
	}
	
	protected void addAtlas(BuildableBitmapTextureAtlas atlas) {
		buildableAtlases.add(atlas);
	}
	
	protected abstract void onLoad();

	// Getters
	public DisplayMetrics getDisplayMetrics() {
		return metrics;
	}

	public int getDPI() {
		return metrics.densityDpi;
	}

	public int getScreenHeight() {
		return metrics.heightPixels;
	}

	public int getScreenWidth() {
		return metrics.widthPixels;
	}

	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return activity.getVertexBufferObjectManager();
	}

}
