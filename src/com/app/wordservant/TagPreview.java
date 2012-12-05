package com.app.wordservant;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TagPreview extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select_scripture);

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

		ListView lView = (ListView) findViewById(R.id.listView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Genesis","Exodus","Leviticus","Numbers","Deuteronomy","Joshua","Judges"});
		lView.setAdapter(adapter);
		GridView gView = (GridView) findViewById(R.id.gridView1);
		gView.setAdapter(adapter);
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(new File("sample_bible.xml"), new DefaultHandler());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.tag_preview, menu);
		return true;
	}
}
