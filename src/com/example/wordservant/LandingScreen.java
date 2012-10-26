package com.example.wordservant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LandingScreen extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onStart();
        displayLandingScreen();
    }

	private void displayLandingScreen() {
		// TODO Auto-generated method stub
		setContentView(R.layout.landing_screen);
		final ListView landingScreenListView = (ListView) this.findViewById(R.id.listView1);
		
		//myListView.setOnClickListener(mCorkyListener);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
		        android.R.layout.simple_list_item_1, this.getResources().getStringArray(R.array.landing_menu));
		landingScreenListView.setAdapter(adapter);
		
		landingScreenListView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View landingScreenView, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent;
				Toast.makeText(LandingScreen.this, landingScreenListView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
				switch (position){
					case 0:
						intent = new Intent(landingScreenView.getContext(),TodaysMemoryVerses.class);
				    	startActivity(intent);
				    	break;
					case 2:
						intent = new Intent(landingScreenView.getContext(),ScriptureBank.class);
				    	startActivity(intent);
				    	break;
				}
				
			}


			
		});
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_screen, menu);
        return true;
    }


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	Intent intent = new Intent(this, Settings.class);
	        	startActivity(intent);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
