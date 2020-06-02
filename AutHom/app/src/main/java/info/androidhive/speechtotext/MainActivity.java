package info.androidhive.speechtotext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity   {

	private TextView txtSpeechInput;
	private String command;
	private ImageButton btnSpeak;
	private ImageButton refresh;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	private WebView myWebView;
	private String write_api_key ="N9CPD6BNJ44OVCEV";
	private String read_api_key = "NM5G53W1VFUXHXLJ";

	public static TextView [] textViews = new TextView[6];
	StringBuilder sb;


	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
		refresh = (ImageButton) findViewById(R.id.imageButtonRefresh);
		textViews[0] = (TextView) findViewById(R.id.textView_1);
		textViews[1] = (TextView) findViewById(R.id.textView_2);
		textViews[2] = (TextView) findViewById(R.id.textView_3);
		textViews[3] = (TextView) findViewById(R.id.textView_4);
		textViews[4] = (TextView) findViewById(R.id.textView_5);
		textViews[5] = (TextView) findViewById(R.id.textView_6);
		try {
			colorChange();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// hide the action bar
		getActionBar().hide();


		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					colorChange();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_SPEECH_INPUT: {
				if (resultCode == RESULT_OK && null != data) {

					ArrayList<String> result = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					txtSpeechInput.setText(command = result.get(0));
					boolean runStatus=false;
					try {
						runStatus = runCommand(command);
					}catch (Exception e){
						txtSpeechInput.setText("Sorry!! Something went WRONG.");
					}
					if(runStatus){
						Toast.makeText(getApplicationContext(),"Done Master ",Toast.LENGTH_SHORT);
					}
					else
						Toast.makeText(getApplicationContext(),"Unable to "+command,Toast.LENGTH_SHORT);

				}
				try {
					colorChange();
				} catch (ExecutionException e) {
					txtSpeechInput.setTextColor(Color.rgb(255,0,0));
				} catch (InterruptedException e) {
					txtSpeechInput.setTextColor(Color.rgb(0,255,0));
					e.printStackTrace();
				}

				break;
			}
		}
	}

	/*
	Running the command and selecting the command.

	 */
	public boolean runCommand(String command) throws ExecutionException, InterruptedException {
		if (command.contains("do not")||command.contains("Do not")||command.contains("don't")||command.contains("Don't")||
				(command.contains("on")&&command.contains("off"))){
			txtSpeechInput.setText("Command assertively and one at a time.");
		}
		else if((command.contains("On")||command.contains("on")||command.contains("start")||command.contains("kholo")||command.contains("jalao"))){
			if(command.contains("Light")||command.contains("light")||command.contains("Lights")||command.contains("lights")||command.contains("batti")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				//char[] pro = prev.toCharArray();
				//String s = '9'+String.copyValueOf(pro, 1, prev.length());
				String next = "1"+prev.substring(1,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Light turned ON");
			}
			else if(command.contains("Fan")||command.contains("fan")||command.contains("Fans")||command.contains("fans")||command.contains("pankha")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = prev.substring(0,1)+"1"+prev.substring(2,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Fan turned ON");
			}
			else if(command.contains("AC")||command.contains("ac")||command.contains("ACs")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = prev.substring(0,2)+"1"+prev.substring(3,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("AC turned ON");
			}
			else if(command.contains("everything")||command.contains("all")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = "111"+prev.substring(3,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Light, Fan, AC turned ON");
			}
		}

		else if((command.contains("Off")||command.contains("off")||command.contains("stop"))||command.contains("band")||command.contains("bujhao")){
			if(command.contains("Light")||command.contains("light")||command.contains("Lights")||command.contains("lights")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = "0"+prev.substring(1,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Light turned OFF");
			}
			else if(command.contains("Fan")||command.contains("fan")||command.contains("Fans")||command.contains("fans")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = prev.substring(0,1)+"0"+prev.substring(2,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Fan turned OFF");
			}
			else if(command.contains("AC")||command.contains("ac")||command.contains("ACs")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = prev.substring(0,2)+"0"+prev.substring(3,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("AC turned OFF");
			}
			else if(command.contains("everything")||command.contains("all")){
				BackgroundTask bgTask = new BackgroundTask(this);
				String prev = bgTask.execute("6").get();
				String next = "000"+prev.substring(3,prev.length());
				String url = "http://api.thingspeak.com/update?api_key="+write_api_key+"&field6="+next;
				executeReq(url);
				txtSpeechInput.setText("Light, Fan, AC turned OFF");
			}
		}
		else if(command.contains("humidity")||command.contains("Humidity")){
			String url = "5";
			BackgroundTask bgTask = new BackgroundTask(this);
			txtSpeechInput.setText("Humidity as of now is :\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+bgTask.execute(url).get()+"%");
		}
		else if(command.contains("temperature")||command.contains("Temperature")){
			String url = "4";
			BackgroundTask bgTask = new BackgroundTask(this);
			txtSpeechInput.setText("Temperature as of now is :\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+bgTask.execute(url).get()+"℃");
		}
		else if(command.contains("air quality")||command.contains("Air quality")){
			String url = "1";
			BackgroundTask bgTask = new BackgroundTask(this);
			txtSpeechInput.setText("Air Quality as of now is :\n\t\t\t\t\t\t\t\t\t\t\t\t\t"+bgTask.execute(url).get()+"ppm");
		}

		else if(command.contains("weather")||command.contains("climate")||command.contains("out there")){
			String url = "4";
			BackgroundTask bgTask = new BackgroundTask(this);
			String temp = bgTask.execute(url).get();
			url = "5";
			bgTask = new BackgroundTask(this);
			String hum = bgTask.execute(url).get();
			url = "1";
			bgTask = new BackgroundTask(this);
			String aq = bgTask.execute(url).get();
			txtSpeechInput.setText("Temperature :\t\t\t\t"+temp+"℃" +
					"\nHumidity        :\t\t\t\t"+hum+"%" +
					"\nAir Quality      :\t\t\t\t"+aq+"ppm");
		}
		return true;
	}

/*
Updating the database through url execution

 */
	private void executeReq(String url){
		myWebView =(WebView) findViewById(R.id.WebView);
		myWebView.loadUrl(url);
	}

	/*
	Colours of Light, FAN AC, etc...
	 */

	@SuppressLint("ResourceAsColor")
	protected void colorChange() throws ExecutionException, InterruptedException {
		BackgroundTask bgTask = new BackgroundTask(this);
		String prev = bgTask.execute("6").get();
		BackGround_Color bgColor = new BackGround_Color(this);
		bgColor.execute(prev);
	}

}
