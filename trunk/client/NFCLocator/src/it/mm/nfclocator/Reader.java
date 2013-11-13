package it.mm.nfclocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {
	
	private ProgressDialog progDial;
	private Context context;
	private Reader istance;
	private Handler handler;
	private AlertDialog.Builder builder;
	private String locationString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		istance = this;
		context = this.getApplicationContext();
		handler = new MyHandler();
		builder = new AlertDialog.Builder(this);
		
		final ImageButton sendButton = (ImageButton) findViewById(R.id.openButton);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		progDial = new ProgressDialog(Reader.this);
		progDial.setCancelable(false);
		
		TextView location = (TextView) findViewById(R.id.location);
		// read ndef info
		locationString = this.readTag();
		//location.setText(locationString);
		this.setTitle(locationString);
		
		AlphaAnimation  blinkanimation= new AlphaAnimation(1, (float) 0.5); // Change alpha from fully visible to invisible
		blinkanimation.setDuration(1200); // duration - half a second
		blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		blinkanimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		blinkanimation.setRepeatMode(Animation.REVERSE);
		
		sendButton.setAnimation(blinkanimation);
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// lock the button
				sendButton.setEnabled(false);
				// read the saved informations
				String address = prefs.getString("pref_server", null);
				String port = prefs.getString("pref_port", null);
				String user = prefs.getString("pref_username", null);
				String password = prefs.getString("pref_password", null);
				if(address==null || port==null || user==null || password==null) {
					//Context context = istance.getBaseContext();
					Toast error = Toast.makeText(context, "Some configuration is missing", Toast.LENGTH_LONG);
					error.show();
					return;
				}
				
				// connect to the server
				Thread connection = new Thread(new Communicator(handler, address, port, user, password, locationString, context));
				connection.start();
				// TODO check the result and show something to the user (alertDialog)
				// TODO close the activity
				
				sendButton.setEnabled(true);
			}
		});
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		progDial.dismiss();
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	private String readTag () {
		Intent nfcintent = getIntent();
		Tag myTag = (Tag) nfcintent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		if (myTag == null)
				return "Error"; // TODO terminare l'applicazione
		
		Ndef ndefTag = Ndef.get(myTag);
		//int size = ndefTag.getMaxSize();
		//boolean writable = ndefTag.isWritable();
		//String type = ndefTag.getType();
		
		
		NdefMessage ndefMesg = ndefTag.getCachedNdefMessage();
		NdefRecord[] ndefRecords = ndefMesg.getRecords();
		String reading = "";
		int len = ndefRecords.length;
		for (int i=0; i<len; i++) {
			byte[] payload = ndefRecords[i].getPayload();
			for(int j=3; j<payload.length; j++) {
				reading += (char)payload[j];
			}
			if(!"".equals(reading))
				return reading;
		}
		return reading;
	}
	
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int code = bundle.getInt("code");
			String content = bundle.getString("status");
			
			switch (code) {
			case -1: {
				// initialize the dialog
				progDial.setMessage(content);
				progDial.show();
			}
			case 0:
				// update message
				progDial.setMessage(content);
				break;
			case 1: {
				// Access Granted
				progDial.hide();
				builder.setTitle("Access Granted");
				builder.setMessage("Authentication successfull");
				builder.setCancelable(false);
				builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						istance.finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			}
			case 2: {
				// error for Access Denied
				progDial.hide();
				builder.setTitle("Access Denied");
				builder.setMessage(content);
				builder.setCancelable(false);
				builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						istance.finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			}
			case 3: {
				// settings error
				progDial.hide();
				builder.setTitle("An error occurred");
				builder.setMessage(content);
				builder.setCancelable(false);
				builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						istance.finish();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			}

			default:
				Log.d("Reader", "Unexpected error");
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reader, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	        Intent intent = new Intent(this, Configuration.class);
	        startActivity(intent);
	        this.finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
