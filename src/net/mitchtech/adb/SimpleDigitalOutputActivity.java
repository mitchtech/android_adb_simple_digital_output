package net.mitchtech.adb;

import java.io.IOException;

import net.mitchtech.adb.simpledigitaloutput.R;

import org.microbridge.server.Server;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class SimpleDigitalOutputActivity extends Activity implements OnClickListener {

	private final String TAG = SimpleDigitalOutputActivity.class.getSimpleName();

	Button mLedButton1;
	Button mLedButton2;
	Button mLedButton3;

	boolean mLedState1 = false;
	boolean mLedState2 = false;
	boolean mLedState3 = false;

	int mLedData1 = 0;
	int mLedData2 = 0;
	int mLedData3 = 0;

	// Create TCP server (based on MicroBridge LightWeight Server).
	// Note: This Server runs in a separate thread.
	Server mServer = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		mLedButton1 = (Button) findViewById(R.id.btn1);
		mLedButton1.setOnClickListener(this);
		mLedButton2 = (Button) findViewById(R.id.btn2);
		mLedButton2.setOnClickListener(this);
		mLedButton3 = (Button) findViewById(R.id.btn3);
		mLedButton3.setOnClickListener(this);

		// Create TCP server (based on MicroBridge LightWeight Server)
		try {
			mServer = new Server(4568); // Use ADK port
			mServer.start();
		} catch (IOException e) {
			Log.e(TAG, "Unable to start TCP server", e);
			System.exit(-1);
		}

	} // End of TCP Server code

	// Called when one of the LED buttons is clicked
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			if (mLedState1 == true) {
				mLedState1 = false;
				mLedData1 = 0;
				mLedButton1.setText("LED Off");
			} else {
				mLedState1 = true;
				mLedData1 = 1;
				mLedButton1.setText("LED On");
			}
			break;

		case R.id.btn2:
			if (mLedState2 == true) {
				mLedState2 = false;
				mLedData2 = 0;
				mLedButton2.setText("LED Off");
			} else {
				mLedState2 = true;
				mLedData2 = 1;
				mLedButton2.setText("LED On");
			}
			break;

		case R.id.btn3:
			if (mLedState3 == true) {
				mLedState3 = false;
				mLedData3 = 0;
				mLedButton3.setText("LED Off");
			} else {
				mLedState3 = true;
				mLedData3 = 1;
				mLedButton3.setText("LED On");
			}
			break;

		default:
			break;
		}

		try {
			// Send the state of each LED to ADK Main Board as a byte
			mServer.send(new byte[] { (byte) mLedData1, (byte) mLedData2, (byte) mLedData3 });
		} catch (IOException e) {
			Log.e(TAG, "problem sending TCP message", e);
		}

	}

}
