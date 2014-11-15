package com.example.clock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
	
	private SeekBar seekBar;
	Button button;
	public static Boolean military = false;
	public static SimpleDateFormat sdfDate;
	public static String Text;
	public static CustomClockView clockview;
	Context context;
	private TextView textView;
	Button buttonReset;
	
	private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		addListenerOnButton();
		context = this;
		
		//Steps
		textView = (TextView) findViewById(R.id.steps);
		mSensorManager = (SensorManager)        
	            getSystemService(Context.SENSOR_SERVICE);
	      mStepCounterSensor = mSensorManager
	            .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
	      mStepDetectorSensor = mSensorManager
	            .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
		
		
		
		
		//Dropdown Menu for timezones
		final Spinner spinner = (Spinner) findViewById(R.id.spinner_time);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.planets_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	Text = spinner.getSelectedItem().toString();
		    	
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    }
		});
		
		//Slider for color
		seekBar = (SeekBar) findViewById(R.id.seekBar2);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			 public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				 if(progresValue == 0){
					 getWindow().getDecorView().setBackgroundColor(Color.rgb(255,255,255));
				 }
				 else{
					 getWindow().getDecorView().setBackgroundColor(Color.rgb(progresValue+80,progresValue+120,progresValue+60));
				 }
			 }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		
		MainActivity.clockview = (CustomClockView)findViewById(R.id.clockface);
		Thread t = new Thread() {
				
			  @Override
			  public void run() {
			    try {
			      while (!isInterrupted()) {
			        Thread.sleep(1000);
			        runOnUiThread(new Runnable() {
			          @Override
			          public void run() {
			        	  //update Textview
			        	  	        	  
			        	  if(military){
			        		  sdfDate = new SimpleDateFormat("h:mm:ss a");
			        		  
			        	  }
			        	  else{
			        		  sdfDate = new SimpleDateFormat("HH:mm:ss");
			        	  }

			        	  TimeZone.setDefault(TimeZone.getTimeZone(Text));
			        	  Date now = new Date();
			        	  String strDate = sdfDate.format(now);
			        	  
			        	  
			        	  CustomClockView.date = strDate;
			        	  MainActivity.clockview.invalidate();
			        	  MainActivity.clockview.postInvalidate();
			        	  
			        	  ((TextView) findViewById(R.id.hello)).setText(""+strDate);
			          }
			        });
			      }
			    } catch (InterruptedException e) {
			    }
			  }
			};

			t.start();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	public boolean reset = false;
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.reset) {
			reset = true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//button to change standard to military time
	public void addListenerOnButton() {
		
		this.buttonReset =(Button) findViewById(R.id.reset);
		buttonReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reset = true;
				((TextView) findViewById(R.id.steps)).setText("Step Counter Detected : " + 0);
				remove = steps;
			}
			
		});
		button = (Button) findViewById(R.id.military);
		button.setText("Standard Time");
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				military = !military;
				if(military)
					button.setText("Military Time");
				else
					button.setText("Standard Time");
			}
 
		});
 
	}
	
	public int remove = 0;
	public int steps = 0;
	@Override
	public void onSensorChanged(SensorEvent event) {
		 Sensor sensor = event.sensor;
	     float[] values = event.values;
	     
	     int value = -1;
	    
	     
	     if (values.length > 0) {
	        value = (int) values[0];
	        steps = value;
	     }

	     if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
	    	  ((TextView) findViewById(R.id.steps)).setText("Step Counter Detected : " + (value-remove));
	        
	     } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
	         // For test only. Only allowed value is 1.0 i.e. for step taken
	    	 ((TextView) findViewById(R.id.steps)).setText("Step Counter Detected : " + (value-remove));
	     }
		
	}

	 protected void onResume() {

	     super.onResume();

	     mSensorManager.registerListener(this, mStepCounterSensor,

	           SensorManager.SENSOR_DELAY_FASTEST);      
	     mSensorManager.registerListener(this, mStepDetectorSensor,

	           SensorManager.SENSOR_DELAY_FASTEST);

	 }

	 protected void onStop() {
	     super.onStop();
	     mSensorManager.unregisterListener(this, mStepCounterSensor);
	     mSensorManager.unregisterListener(this, mStepDetectorSensor);
	 }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
}
