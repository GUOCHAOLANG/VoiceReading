package com.example.test;

import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnInitListener {

	Button mBtn;
	EditText infoTxt;

	private TextToSpeech mTts;
	private String mFullBodyString;
	private int MY_DATA_CHECK_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

		infoTxt = (EditText)findViewById(R.id.txt_info);
		mBtn = (Button) findViewById(R.id.btn_click);
		mBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ss = infoTxt.getText().toString();
				mTts.speak(ss, TextToSpeech.QUEUE_FLUSH, null);

			}
		});
		//mTts = new TextToSpeech(this, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				mTts = new TextToSpeech(this, this);
			} else {
				Intent installTTSIntent = new Intent();
				installTTSIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("--guo--", "TTS language is not available.");
			} else {
				mTts.speak(mFullBodyString, TextToSpeech.QUEUE_ADD, null);
			}
		} else {
			// Initialization failed.
			Log.e("--guo--", "Could not initialize TTS.");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
			mTts = null;
		}
		super.onDestroy();
	}

}
