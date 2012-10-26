package com.example.wordservant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Settings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        Button myButton = (Button) findViewById(R.id.button1);
        myButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				displayToast();
			}

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
	private void displayToast() {
		// TODO Auto-generated method stub
		Context context = getApplicationContext();
		try{
			RadioGroup myGroup = (RadioGroup) this.findViewById(R.id.radioGroup1);
			RadioButton selectedButton = (RadioButton) findViewById(myGroup.getCheckedRadioButtonId());
			CharSequence text = selectedButton.getText();
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		catch(NullPointerException e){
			System.err.println("Found null...");
		}
		catch(Exception e){
			System.out.println("Here");
			//System.out.println(e);
		}finally{
			Intent intent = new Intent(this, LandingScreen.class);
        	startActivity(intent);
			this.finish();
		}
	}
}
