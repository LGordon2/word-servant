package com.app.wordservant;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.app.wordservant.Bible.BibleBook;

public class SelectScripture extends Activity {

	private class WaitForBibleLoad extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			while(Bible.getInstance().books.size()<66){
				if(this.isCancelled())
					return null;
			}
			return null;
		}

		protected void onPostExecute(Void bible){
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
			pBar.setProgress(100);
			pBar.setVisibility(ProgressBar.GONE);
			TextView loadingMessage = (TextView) findViewById(R.id.loadingText);
			loadingMessage.setVisibility(TextView.GONE);
			ListView lView = (ListView) findViewById(R.id.listView1);
			lView.setVisibility(ListView.VISIBLE);
			lView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> aView, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					TabHost tHost = (TabHost) findViewById(R.id.tabhost);
					tHost.setCurrentTab(1);
					final BibleBook b = Bible.getInstance().getBook(position);
					GridView gView = (GridView) findViewById(R.id.gridView1);
					gView.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> aView, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							TabHost tHost = (TabHost) findViewById(R.id.tabhost);
							tHost.setCurrentTab(2);
							final GridView gView = (GridView) findViewById(R.id.gridView2);
							final Bible.BibleChapter chapter = b.getChapter(position);
							final ArrayList<Integer> checkedCheckBoxes = new ArrayList<Integer>();
							gView.setOnItemClickListener(new OnItemClickListener(){

								@Override
								public void onItemClick(AdapterView<?> aView,
										View view, int position, long id) {
									// Get scriptures and send them to the displayed scripture screen.
									CheckedTextView textView = (CheckedTextView) view;
									//view.setSelected(true);
									//CheckBox cb = (CheckBox) item.getChildAt(0);
									//cb.performClick();
									Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
									if(!textView.isChecked()){
										checkedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
									}
									else{
										checkedCheckBoxes.remove(checkedCheckBoxes.indexOf((Integer) position+1));
									}
									
									Button displayScriptures = (Button) findViewById(R.id.displayScriptures);
									
									if(checkedCheckBoxes.size()>0){
										displayScriptures.setVisibility(Button.VISIBLE);
									}
									else{
										displayScriptures.setVisibility(Button.INVISIBLE);
									}
								}

							});
							
							Button displayScripture = (Button) findViewById(R.id.displayScriptures);
							displayScripture.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View view) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(getApplicationContext(),DisplaySelectedScripture.class);
									intent.putExtra("book_name", b.bookName);
									intent.putExtra("chapter_number", Integer.valueOf(chapter.chapterNumber).intValue());
									Bundle bundle = new Bundle();
									Collections.sort(checkedCheckBoxes);
									bundle.putIntegerArrayList("verses", checkedCheckBoxes);
									intent.putExtra("bundle", bundle);

									startActivity(intent);
								}
								
							});
							ArrayAdapter<String> adapter;
							adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_checked, chapter.getVersesArray());
							gView.setAdapter(adapter);
						}

					});
					ArrayAdapter<String> adapter;
					adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_1, Bible.getInstance().getBook(position).getChaptersArray());
					gView.setAdapter(adapter);
				}

			});

			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_1, Bible.getInstance().getBookNames());
			lView.setAdapter(adapter);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select_scripture);

		new WaitForBibleLoad().execute();

		TabHost tHost = (TabHost) findViewById(R.id.tabhost);
		tHost.setup();
		TabSpec tabSpec = tHost.newTabSpec("tabs");
		tabSpec.setIndicator("Books");
		tabSpec.setContent(R.id.tab1);
		tHost.addTab(tabSpec);

		tabSpec = tHost.newTabSpec("tabs");
		tabSpec.setIndicator("Chapters");
		tabSpec.setContent(R.id.tab2);
		tHost.addTab(tabSpec);

		tabSpec = tHost.newTabSpec("tabs");
		tabSpec.setIndicator("Verses");
		tabSpec.setContent(R.id.tab3);
		tHost.addTab(tabSpec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.tag_preview, menu);
		return true;
	}
}
