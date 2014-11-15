package com.example.clock;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;


public class CustomClockView extends View{

	private Paint _paintSeconds = new Paint();
	private Paint _paintHours = new Paint();
	private Paint _paintMinutes = new Paint();
	
	public static String date = "10:03:33";
	public CustomClockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



	public CustomClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
	
	public CustomClockView(Context context, String strDate){
		super(context);
		CustomClockView.date = strDate;
		
	}

    private void init(AttributeSet attrs, int i) {
    	 _paintSeconds.setColor(Color.RED);
         _paintSeconds.setAntiAlias(true);
         _paintSeconds.setStyle(Paint.Style.FILL_AND_STROKE);		
         
         _paintMinutes.setColor(Color.GREEN);
         _paintMinutes.setAntiAlias(true);
         _paintMinutes.setStyle(Paint.Style.FILL_AND_STROKE);
         
         
         _paintHours.setColor(Color.BLUE);
         _paintHours.setAntiAlias(true);
         _paintHours.setStyle(Paint.Style.FILL_AND_STROKE);	
         
	}
    
    @Override
    protected synchronized void onDraw(Canvas canvas){
        super.onDraw(canvas);
        
        String[] tmp = CustomClockView.date.split(":");
        float sec=new Float((tmp[2].split(" "))[0]);
        float min=new Float(tmp[1]);
        float hour=(float)new Float(tmp[0])+min/60.0f;

        canvas.drawLine(getWidth()/60*sec, 75, getWidth() , 75, _paintSeconds);
        canvas.drawLine(getWidth()/60*min, 50, getWidth() , 50, _paintMinutes);
        canvas.drawLine(getWidth()/60*hour, 25, getWidth() , 25, _paintHours);
    }
    
}
