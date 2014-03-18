package ca.nigelchan.silenttempest.asynctasks;

import ca.nigelchan.silenttempest.scenes.BaseScene;
import ca.nigelchan.silenttempest.scenes.LoadingScene;
import android.os.AsyncTask;

public class AsyncSceneLoader extends AsyncTask<Void, Integer, Boolean> {
	
	private LoadingScene loadingScene;
	private BaseScene scene;
	
	public AsyncSceneLoader(BaseScene scene, LoadingScene loadingScene) {
		this.scene = scene;
		this.loadingScene = loadingScene;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		scene.prepareScene();
		loadingScene.abortWhenReady();
		return true;
	}

}
