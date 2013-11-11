package it.mm.nfclocator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class SettingFragment extends PreferenceFragment {
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefsEditor;
	private final int passwordIndex = 6;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getBaseContext());
		prefsEditor = prefs.edit();
		
		Preference QrDecoder = findPreference("pref_qr");
		
		QrDecoder.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				try {
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
				} catch (Exception e) {
					Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.google.zxing.client.android"));
					startActivity(goToMarket);
					return false;
				}
				return true;
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Context context = this.getActivity().getBaseContext();
		if (requestCode == 0 && resultCode == android.app.Activity.RESULT_OK) {
			// The string has the following pattern:
			// address:<address> port:<port> username:<username>
			String address, port, username;
			String[] addressArray, portArray, usernameArray;
			String result = intent.getStringExtra("SCAN_RESULT");
			String[] info = result.split(" ");
			if(info.length == 3) {
				addressArray = info[0].split(":");
				if(addressArray.length == 2) {
					address = addressArray[1];
					portArray = info[1].split(":");
					if(portArray.length == 2) {
						port = portArray[1];
						usernameArray = info[2].split(":");
						if(usernameArray.length == 2) {
							username = usernameArray[1];
							
							// save new data
							prefsEditor.putString("pref_server", address);
							prefsEditor.putString("pref_port", port);
							prefsEditor.putString("pref_username", username);
							prefsEditor.commit();
								
							// update GUI to show new data
							((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged(); // TODO not working
							
							// ask the password to the user
							PreferenceScreen screen = getPreferenceScreen();
							screen.onItemClick(null, null, passwordIndex, 0);
							
							// tell the user that all is ok
							Toast success = Toast.makeText(context, "Information updated succesfully", Toast.LENGTH_LONG);
							success.show();
								
							return;
						}
					}
				}
			}
			
			Toast success = Toast.makeText(context, "Invalid QR Code format", Toast.LENGTH_LONG);
			success.show();
		}
	}
	
}
