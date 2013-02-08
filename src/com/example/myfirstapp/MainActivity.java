package com.example.flashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Filter;
import android.widget.Toast;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.graphics.Color;

import java.util.Random;
import java.io.InputStream;
import java.io.IOException;


public class MainActivity extends Activity
implements OnClickListener
{
	private int requestCode = 1;

	private static final int SWIPE_MIN_DISTANCE = 100; //120
	private static final int SWIPE_MAX_OFF_PATH = 250; //250
	private static final int SWIPE_THRESHOLD_VELOCITY = 200; //was 200
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	VocabularyCard v;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
			return gestureDetector.onTouchEvent(event);
		    }
		};

		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_text);
		rl.setOnTouchListener(gestureListener);

		v = new VocabularyCard(5);
	}

	public void nextCard(View view) {
		v.readRandomPairFromDataBase();
		v.front();
   	}

	public class VocabularyCard{
		String[][] database;
		String[] front;
		String[] back;
		int textSize = 50;
		int textColor = Color.rgb(50,255,50);

		VocabularyCard( int N ){
			front = new String[N];
			back = new String[N];
			generateDataBase();
			readRandomPairFromDataBase();
			front();
		}
		void generateDataBase(){
			//int Nwords = 0;
			try{
				InputStream input = getAssets().open("vocabulary.txt");//limited to 2GB
				int size = input.available();
				byte[] buffer = new byte[size];
				input.read(buffer);
				input.close();
	 
				String text = new String(buffer);
				
				String[] words = text.split("\\r?\\n");
				database = new String[words.length][2];
				for(int i=0;i<words.length;i++){
					String[] tmp = words[i].split("--");
					database[i][0]=tmp[0];
					database[i][1]=tmp[1];
				}


			}catch(IOException e){
			}


		}


		public void front(){
			TextView[] tv = new TextView[front.length];
			tv[0] = (TextView) findViewById(R.id.premiere_mot);
			tv[1] = (TextView) findViewById(R.id.deuxieme_mot);
			tv[2] = (TextView) findViewById(R.id.troisieme_mot);
			tv[3] = (TextView) findViewById(R.id.quatrieme_mot);
			tv[4] = (TextView) findViewById(R.id.cinquieme_mot);

			for(int i=0; i<front.length;i++){
				tv[i].setTextSize( textSize );
				tv[i].setText( front[i] );
				tv[i].setTextColor( textColor );
			}
			setTitle("Français!");
		}
		public void setTitle(String s){
			TextView title = (TextView) findViewById(R.id.title);
			title.setTextSize(75);
			title.setText(s);
			title.setTextColor( Color.rgb(255,255,255) );
		}
		public void back(){
			TextView[] tv = new TextView[front.length];
			tv[0] = (TextView) findViewById(R.id.premiere_mot);
			tv[1] = (TextView) findViewById(R.id.deuxieme_mot);
			tv[2] = (TextView) findViewById(R.id.troisieme_mot);
			tv[3] = (TextView) findViewById(R.id.quatrieme_mot);
			tv[4] = (TextView) findViewById(R.id.cinquieme_mot);

			for(int i=0; i<front.length;i++){
				tv[i].setTextSize( textSize );
				tv[i].setText( back[i] );
				tv[i].setTextColor( textColor );
			}
			setTitle("Anglais!");
		}

		void readRandomPairFromDataBase(){

			for(int i=0;i<front.length;i++){
				Random r = new Random();
				int u = r.nextInt(database.length-1);
				front[i] = database[u][0];
				back[i] = database[u][1];
				//uniqueId[i]=u;
			}//for

		}//function

	}

	public void onClick(View v) {
	}

	public void printStatus( String s ){
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}
	public void left(){
		v.back();
	}
	public void right(){
		v.front();
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		    try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			    return false;
			if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				left();
				
			}  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				right();
			}
		    } catch (Exception e) {
		    }
		    return false;
		}

	}

}
