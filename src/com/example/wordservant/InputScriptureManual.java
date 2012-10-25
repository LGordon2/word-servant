package com.example.wordservant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputScriptureManual extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_scripture_manual);
        
        Button doneButton = (Button) this.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View inputScriptureView) {
				// Grab all scripture data and write it to the file.
				FileOutputStream fos = null;
				ObjectOutputStream os = null;
				try {
					EditText scriptureReference = (EditText) findViewById(R.id.scriptureReference);
					EditText categoryName = (EditText) findViewById(R.id.scriptureReference);
					EditText scriptureText = (EditText) findViewById(R.id.scriptureText);

					Scripture inputScripture = new Scripture(scriptureReference.getText().toString(), categoryName.getText().toString(), scriptureText.getText().toString());

					fos = openFileOutput((String) getResources().getText(R.string.scripture_bank_filename), Context.MODE_APPEND);
					os = new ObjectOutputStream(fos);
					os.writeObject(inputScripture);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} finally {
					try {
						fos.close();
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Intent intent = new Intent(inputScriptureView.getContext(),ScriptureBank.class);
		    	startActivity(intent);
		    	finish();
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_input_scripture_manual, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
		Intent intent = new Intent(this,ScriptureBank.class);
    	startActivity(intent);
    	finish();
    }
}
