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
		
		/*final Button saveButton = (Button) findViewById(R.id.saveButton);
		final EditText text = (EditText) findViewById(R.id.pswd	);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String FILENAME = "configuration";
				try {
					FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
					fos.write(text.getText().toString().getBytes());
					fos.close();
					Context context = getApplicationContext();
					Toast toast = Toast.makeText(context, "saved!", Toast.LENGTH_SHORT);
					toast.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
		
		/*Button read = (Button) findViewById(R.id.read);
		writeLabel = (TextView) findViewById(R.id.write);
		
		myReceiver = new MyReceiver();
		registerReceiver(myReceiver, new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED));
		
		read.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				readTag();
				
			}
		});*/
	}
	
	/*private void readTag () {
		Intent nfcintent = getIntent();
		Tag myTag = (Tag) nfcintent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		Ndef ndefTag = Ndef.get(myTag);
		int size = ndefTag.getMaxSize();
		boolean writable = ndefTag.isWritable();
		String type = ndefTag.getType();
		
		NdefMessage ndefMesg = ndefTag.getCachedNdefMessage();
		NdefRecord[] ndefRecords = ndefMesg.getRecords();
		int len = ndefRecords.length;
		for (int i=0; i<len; i++) {
			byte[] payload = ndefRecords[i].getPayload();
			for(int j=0; j<payload.length; j++) {
				System.out.println(payload[j]);
			}
		}
	}*/
	
	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//Intent nfcintent = getIntent(); // ???
		Tag myTag = (Tag) data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		Ndef ndefTag = Ndef.get(myTag);
		int size = ndefTag.getMaxSize();
		boolean writable = ndefTag.isWritable();
		String type = ndefTag.getType();
		
		NdefMessage ndefMesg = ndefTag.getCachedNdefMessage();
		NdefRecord[] ndefRecords = ndefMesg.getRecords();
		int len = ndefRecords.length;
		for (int i=0; i<len; i++) {
			byte[] payload = ndefRecords[i].getPayload();
			for(int j=0; j<payload.length; j++) {
				System.out.println(payload[j]);
			}
		}
		
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_configuration, menu);
		return true;
	}
	
	/*public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent data) {
			Tag myTag = (Tag) data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			
			Ndef ndefTag = Ndef.get(myTag);
			int size = ndefTag.getMaxSize();
			boolean writable = ndefTag.isWritable();
			String type = ndefTag.getType();
			
			NdefMessage ndefMesg = ndefTag.getCachedNdefMessage();
			NdefRecord[] ndefRecords = ndefMesg.getRecords();
			int len = ndefRecords.length;
			for (int i=0; i<len; i++) {
				byte[] payload = ndefRecords[i].getPayload();
				for(int j=0; j<payload.length; j++) {
					System.out.println(payload[j]);
				}
			}
		}
		
	}*/

}