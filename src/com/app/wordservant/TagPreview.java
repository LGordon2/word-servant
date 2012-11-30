package com.app.wordservant;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TagPreview extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_preview);
		Spinner testSpinner = (Spinner) findViewById(R.id.spinner1);
		GridView gridView = (GridView) findViewById(R.id.gridView1);
		ArrayList<HashMap<String,String>> myList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> button = new HashMap<String,String>();
		HashMap<String, String> button2 = new HashMap<String,String>();
		button.put("blah","coyote");
		myList.add(button);
		button2.put("blah","foo");
		myList.add(button2);
		myList.add(button2);
		SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.button_layout, new String[]{"blah","test"}, new int[]{R.id.button1});
		gridView.setAdapter(adapter);
		
		testSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(view == null){
					return;
				}
				TextView textView = (TextView) view;
				Button newButton = new Button(getApplicationContext());
				newButton.setText(textView.getText().toString());
				newButton.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						//linearLayout.removeView((Button) view);
					}
					
				});
				boolean contains = false;
				/*for(int i=0;i<linearLayout.getChildCount();i++){
					Button childButton = (Button) linearLayout.getChildAt(i);
					if(childButton.getText().equals(newButton.getText())){
						contains = true;
					}
				}*/
				//if(!contains)
					//linearLayout.addView(newButton);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_preview, menu);
		return true;
	}
}
