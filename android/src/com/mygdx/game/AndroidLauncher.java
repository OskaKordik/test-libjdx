package com.mygdx.game;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.badlogic.gdx.backends.android.AndroidxFragmentApplication;

public class AndroidLauncher extends FragmentActivity implements AndroidxFragmentApplication.Callbacks {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout);

		// Create libgdx fragment
		GameFragment libgdxFragment = new GameFragment();

		// Put it inside the framelayout (which is defined in the layout.xml file).
		getSupportFragmentManager().beginTransaction().
				add(R.id.content_framelayout, libgdxFragment).
				commit();
	}

	@Override
	public void exit() {

	}
}

//public class AndroidLauncher extends AndroidApplication {
//	@Override
//	protected void onCreate (Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		initialize(new Core(), config);
//	}
//}