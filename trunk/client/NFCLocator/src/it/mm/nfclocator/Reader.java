package it.mm.nfclocator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {
	
	private ProgressDialog progDial;
	private Reader istance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		istance = this;
		
		final ImageButton sendButton = (ImageButton) findViewById(R.id.openButton);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		progDial = new ProgressDialog(Reader.this);
		TextView location = (TextView) findViewById(R.id.location);
		// TODO set the location with tag informations
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// lock the button
				sendButton.setEnabled(false);
				// read the saved informations
				String address = prefs.getString("pref_address", null);
				String port = prefs.getString("pref_port", null);
				String user = prefs.getString("pref_username", null);
				String password = prefs.getString("pref_password", null);
				if(address==null || port==null || user==null || password==null) {
					Context context = istance.getBaseContext();
					Toast error = Toast.makeText(context, "Some configuration is missing", Toast.LENGTH_LONG);
					error.show();
					return;
				}
				
				progDial.setMessage("Connecting...");
				progDial.setCancelable(true); // TODO for now
				progDial.show();
				// TODO connect to the server
				Thread connection = new Thread(new Communicator(istance, address, port, user, password));
				connection.start();
				// TODO check the result and show something to the user (alertDialog)
				// TODO close the activity
				
				sendButton.setEnabled(true);
			}
		});
		
	}
	
	/**
	 * Communicate with the user
	 * @param end true when the connection is over
	 * @param error true if the end is due to an error
	 * @param status message to communicate to the user
	 */
	public void updateConnectionStatus(boolean end, boolean error, String status) {
		progDial.setMessage("prova");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reader, menu);
		return true;
	}
}
