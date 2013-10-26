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
	
	ProgressDialog progDial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		
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
				
				progDial.setMessage("Connecting...");
				progDial.setCancelable(true); // TODO for now
				progDial.show();
				// TODO connect to the server
				// usare un thread separato per la connessione
				// TODO check the result and show something to the user (alertDialog)
				// TODO close the activity
				
				sendButton.setEnabled(true);
			}
		});
		
	}
	
	public void updateConnectionStatus(boolean end, String status) {
		progDial.setMessage("prova");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reader, menu);
		return true;
	}
}
