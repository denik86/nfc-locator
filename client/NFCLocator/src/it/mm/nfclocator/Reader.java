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
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Reader extends Activity {
	
	private ProgressDialog progDial;
	private Context context;
	private Reader istance;
	private Handler handler;
	private AlertDialog.Builder builder;
	private String locationString;
	
	private TextView customTitle;

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
		
		//TextView location = (TextView) findViewById(R.id.location);
		ImageView row = (ImageView) findViewById(R.id.row);
		ImageView text1 = (ImageView) findViewById(R.id.text1);
		ImageView text2 = (ImageView) findViewById(R.id.text2);
		ImageView text3 = (ImageView) findViewById(R.id.text3);
		
		// read ndef info
		locationString = this.readTag();
		//location.setText(locationString);
		this.setTitle("                       "+locationString); // TODO find a way to do that in a nicer way
		
		// sendButton animations:
		
		// fade in animation
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new LinearInterpolator());
		fadeIn.setDuration(1000);
		// scale animation
		Animation scale = AnimationUtils.loadAnimation(this, R.anim.scale);
		scale.setInterpolator(new DecelerateInterpolator());
		// set glow animation for the sendButton
		AlphaAnimation  blinkanimation= new AlphaAnimation(1, 0.5f);
		blinkanimation.setDuration(1200);
		blinkanimation.setInterpolator(new LinearInterpolator());
		blinkanimation.setRepeatCount(Animation.INFINITE);
		blinkanimation.setRepeatMode(Animation.REVERSE);
		// combine the animations
		AnimationSet animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeIn);
		animation.addAnimation(scale);
		animation.addAnimation(blinkanimation);
		// set the the animations
		sendButton.setAnimation(animation);
		
		// row animation:
		// fade in animation
		Animation fadeInRow = new AlphaAnimation(0, 1);
		fadeInRow.setInterpolator(new LinearInterpolator());
		fadeInRow.setDuration(1000);
		fadeInRow.setStartOffset(1500);
		row.setAnimation(fadeInRow);
		
		// text animation:
		Animation fadeInText1 = new AlphaAnimation(0, 1);
		Animation fadeInText2 = new AlphaAnimation(0, 1);
		Animation fadeInText3 = new AlphaAnimation(0, 1);
		fadeInText1.setInterpolator(new LinearInterpolator());
		fadeInText2.setInterpolator(new LinearInterpolator());
		fadeInText3.setInterpolator(new LinearInterpolator());
		fadeInText1.setDuration(1000);
		fadeInText2.setDuration(1000);
		fadeInText3.setDuration(1000);
		fadeInText1.setStartOffset(5000);
		fadeInText2.setStartOffset(5500);
		fadeInText3.setStartOffset(6000);
		text1.setAnimation(fadeInText1);
		text2.setAnimation(fadeInText2);
		text3.setAnimation(fadeInText3);
		
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
				
				sendButton.setEnabled(true); // TODO maybe false?
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
