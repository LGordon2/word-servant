package com.app.wordservant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.app.wordservant.Bible.BibleBook;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

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
							GridView gView = (GridView) findViewById(R.id.gridView2);
							final Bible.BibleChapter chapter = b.getChapter(position);
							gView.setOnItemClickListener(new OnItemClickListener(){

								@Override
								public void onItemClick(AdapterView<?> aView,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(getApplicationContext(),DisplaySelectedScripture.class);
									Bible.BibleVerse v = chapter.getVerse(position);
									String reference = b.bookName + " " + chapter.chapterNumber + ":" + v.verseNumber;
									intent.putExtra("scripture_text", v.text);
									intent.putExtra("reference", reference);
									startActivity(intent);
								}

							});
							ArrayAdapter<String> adapter;
							adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_1, chapter.getVersesArray());
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
