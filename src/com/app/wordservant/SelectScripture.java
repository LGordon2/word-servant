package com.app.wordservant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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
import android.widget.TabHost.TabSpec;

public class SelectScripture extends Activity {

	private class Bible{

		TreeMap<Integer, String> bookNumbers;
		Hashtable<String, BibleBook> books;
		private Bible(){
			books = new Hashtable<String,BibleBook>();
			bookNumbers = new TreeMap<Integer, String>();
		}
		public String[] getBookNames() {
			// TODO Auto-generated method stub
			return bookNumbers.values().toArray(new String[]{});
		}
		public void addBook(BibleBook b) {
			// TODO Auto-generated method stub
			bookNumbers.put(books.size(), b.bookName);
			books.put(b.bookName, b);
		}
		public BibleBook getBook(int position) {
			// TODO Auto-generated method stub
			return this.getBook(bookNumbers.get(position));
		}
		public BibleBook getBook(String name){
			return books.get(name);
		}

	}
	private class BibleBook{
		String bookName;
		int bookNumber;
		ArrayList<BibleChapter> chapters;
		public BibleBook(String bookName) {
			// TODO Auto-generated constructor stub
			this.bookName = bookName;
			chapters = new ArrayList<BibleChapter>();
		}
		public void addChapter(BibleChapter c){
			chapters.add(c);
		}
		public String[] getChaptersArray() {
			// TODO Auto-generated method stub
			ArrayList<String> allChapters = new ArrayList<String>();
			for(BibleChapter c : chapters){
				allChapters.add(c.chapterNumber);
			}
			return allChapters.toArray(new String[]{});
		}
		public BibleChapter getChapter(int position) {
			// TODO Auto-generated method stub
			return chapters.get(position);
		}

	}
	
	private class BibleChapter{
		String chapterNumber;
		ArrayList<BibleVerse> verses;
		public BibleChapter(String chapterNumber){
			this.chapterNumber = chapterNumber;
			verses = new ArrayList<BibleVerse>();
		}
		public void addVerse(BibleVerse v){
			verses.add(v);
		}
		public String[] getVersesArray() {
			// TODO Auto-generated method stub
			ArrayList<String> s = new ArrayList<String>();
			for(BibleVerse v : verses){
				s.add(v.verseNumber);
			}
			return s.toArray(new String[]{});
		}
		public String getVerseText(int position) {
			// TODO Auto-generated method stub
			return verses.get(position).text;
		}
		public BibleVerse getVerse(int position) {
			// TODO Auto-generated method stub
			return verses.get(position);
		}
	}
	
	private class BibleVerse{
		String verseNumber;
		String text;
		public BibleVerse(String verseNumber, String text){
			this.verseNumber = verseNumber;
			this.text = text;
		}
	}
	private class AsyncXMLParse extends AsyncTask<Void, Void, Bible>{

		@Override
		protected Bible doInBackground(Void... params) {

			//ArrayList<BibleBook> books = new ArrayList<BibleBook>();
			Bible bible = new Bible();
			BibleBook b = null;
			BibleChapter c = null;
			BibleVerse v = null;
			Integer currentBook = -1;
			//ArrayList<String> aList = new ArrayList<String>();
			//ArrayList<String> chapters = new ArrayList<String>();
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(getResources().openRawResource(R.raw.kjv), null);
				
				int eventType = xpp.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					if(eventType == XmlPullParser.START_TAG){
						if(xpp.getName().equals("BIBLEBOOK")){
							currentBook += 1;
							b = new BibleBook(xpp.getAttributeValue(null, "bname"));
						}else if(xpp.getName().equals("CHAPTER")){
							c = new BibleChapter(xpp.getAttributeValue(null, "cnumber"));
						}else if(xpp.getName().equals("VERS")){
							v = new BibleVerse(xpp.getAttributeValue(null, "vnumber"),xpp.nextText());
							c.addVerse(v);
						}
						
					}else if(eventType == XmlPullParser.END_TAG){
						if(xpp.getName().equals("CHAPTER") && currentBook != null){
							b.addChapter(c);
						}else if(xpp.getName().equals("BIBLEBOOK")){
							bible.addBook(b);
						}
					}
					eventType = xpp.next();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return bible;
		}

		protected void onPreExecute(){
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
			pBar.setProgress(0);
		}
		protected void onPostExecute(final Bible bible){
			ProgressBar pBar = (ProgressBar) findViewById(R.id.progressBar1);
			pBar.setProgress(100);
			pBar.setVisibility(ProgressBar.GONE);
			ListView lView = (ListView) findViewById(R.id.listView1);
			lView.setOnItemClickListener(new OnItemClickListener(){
			
				@Override
				public void onItemClick(AdapterView<?> aView, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					TabHost tHost = (TabHost) findViewById(R.id.tabhost);
					tHost.setCurrentTab(1);
					final BibleBook b = bible.getBook(position);
					GridView gView = (GridView) findViewById(R.id.gridView1);
					gView.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> aView, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							TabHost tHost = (TabHost) findViewById(R.id.tabhost);
							tHost.setCurrentTab(2);
							GridView gView = (GridView) findViewById(R.id.gridView2);
							final BibleChapter chapter = b.getChapter(position);
							gView.setOnItemClickListener(new OnItemClickListener(){

								@Override
								public void onItemClick(AdapterView<?> aView,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(getApplicationContext(),DisplaySelectedScripture.class);
									BibleVerse v = chapter.getVerse(position);
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
					adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_1, bible.getBook(position).getChaptersArray());
					gView.setAdapter(adapter);
				}
				
			});

			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(SelectScripture.this, android.R.layout.simple_list_item_1, bible.getBookNames());
			lView.setAdapter(adapter);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select_scripture);

		AsyncXMLParse parseXML = new AsyncXMLParse();
		parseXML.execute();

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
