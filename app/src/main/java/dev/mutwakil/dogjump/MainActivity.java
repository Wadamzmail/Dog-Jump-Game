package dev.mutwakil.dogjump;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.GdxNativeLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.GdxNativesLoader;
import dev.mutwakil.dogjump.game.Dj;

public class MainActivity extends AndroidApplication {
	public static Context ctx;
	public static AndroidApplication inst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = getApplicationContext();
		try {
			GdxNativesLoader.load();
			FreeType.initFreeType();
		} catch (Exception e) {
			toast(e.toString());
		}
		if (inst == null) {
			inst = this;

		} else {
			inst = this;
		}

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		//cfg.depth = 0;
		//	cfg.useAccelerometer = false;
		//cfg.useImmersiveMode = true;
		 
		initialize(new Dj(),cfg);
		
	}

	@Override
	protected void onResume() {
		if (inst != this) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				finishAndRemoveTask();
			} else {
				finish();
			}
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
	}

	public static void toast(String string) {
		if(Looper.myLooper()!=null)Looper.prepare();
		new Handler(Looper.getMainLooper()).post(() ->
		Toast.makeText(ctx, string, Toast.LENGTH_SHORT).show());
	}
}