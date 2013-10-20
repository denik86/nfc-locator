package it.mm.nfclocator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Reader extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		
		ImageButton sendButton = (ImageButton) findViewById(R.id.openButton);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		TextView location = (TextView) findViewById(R.id.location);
		// TODO set the location with tag indormations
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO lock the button
				ProgressDialog progDial = new ProgressDialog(Reader.this);
				progDial.setMessage("Connecting...");
				progDial.setCancelable(true); // TODO for now
				progDial.show();
				// TODO connect to the server
				// TODO check the result and show something to the user (alertDialog)
				// TODO close the activity
			}
		});
		
		//response.setText(prefs.getString("pref_username", "error"));
		
		/*try {
			FileInputStream fis = openFileInput("configuration");
			String reading = "";
			
			int content;
			while ((content = fis.read()) != -1) {
				reading += (char)content;
			}
			if(!reading.equals("")) 
				response.setText(reading);
			else
				response.setText("file empty");
		} catch (Exception e) {
			response.setText("no file found");
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reader, menu);
		return true;
	}
}
