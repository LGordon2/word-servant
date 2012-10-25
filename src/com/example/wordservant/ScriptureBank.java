package com.example.wordservant;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ScriptureBank extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.wordservant.R.layout.scripture_bank);
        displayScriptureBank();
        Button inputScripture = (Button) this.findViewById(com.example.wordservant.R.id.input_scripture);
        inputScripture.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View scriptureBank) {
				// Starts a new input scripture.
				Intent intent = new Intent(scriptureBank.getContext(),InputScriptureManual.class);
		    	startActivity(intent);
		    	finish();
			}
        	
        });
    }

	private void displayScriptureBank() {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		ObjectInputStream is = null;
		try {
			String FILENAME = "ScriptureBank";
			fis = openFileInput(FILENAME);
			is = new ObjectInputStream(fis);
			ArrayList<Scripture> allScriptures = new ArrayList<Scripture>();
			Scripture scriptureObject;
			try{
				while((scriptureObject=(Scripture) is.readObject())!=null){
					allScriptures.add(scriptureObject);
				}
			} catch (EOFException e){
				
			}
			Context context = this.getApplicationContext();
			ArrayAdapter<Scripture> scriptureAdapter = new ArrayAdapter<Scripture>(context, android.R.layout.simple_list_item_1, allScriptures);
			ListView scriptureList = (ListView) findViewById(com.example.wordservant.R.id.scripture_bank_list);
			scriptureList.setAdapter(scriptureAdapter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			makeScriptureBank();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void makeScriptureBank() {
		// TODO Auto-generated method stub

		FileOutputStream fos = null;
		ObjectOutputStream os = null;
		try {

			Scripture inputScripture = new Scripture("blah", "foo", "test");

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
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.example.wordservant.R.menu.scripture_bank, menu);
        return true;
    }
    
    @Override
    public void onBackPressed(){
		Intent intent = new Intent(this,LandingScreen.class);
    	startActivity(intent);
    	finish();
    }
    
}
