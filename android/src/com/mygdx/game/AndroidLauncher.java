package com.mygdx.game;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidxFragmentApplication;

public class AndroidLauncher extends FragmentActivity implements AndroidxFragmentApplication.Callbacks {
	TextView btStart;

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

		btStart = findViewById(R.id.btStart);
		btStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(getBaseContext(), "Start!", Toast.LENGTH_LONG).show();
				GameWorld.start = true;
			}
		});
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
//		initialize(new TestTouchGestures(), config);
//	}
//}