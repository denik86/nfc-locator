package it.mm.nfclocator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class Configuration extends Activity {

	private TextView writeLabel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		
		Fragment pref = new SettingFragment();
		getFragmentManager().beginTransaction().replace(android.R.id.content, pref).commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// No menu for this activity
		return true;
	}

}
