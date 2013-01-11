package com.app.wordservant.ui;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.app.wordservant.R;

public class SelectBibleVerseFragment extends SherlockFragment{
	private int mBookNumber;
	private int mChapterNumber;
	private ArrayAdapter<String> mAdapter;
	private GridView mGridView;
	private ArrayList<Integer> mCheckedCheckBoxes;
	private String mBookName;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.select_bible_chapter, null);
	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	public void onViewCreated (View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mBookNumber = ((SelectScriptureFragmentActivity) getActivity()).bookNumber;
		mChapterNumber = ((SelectScriptureFragmentActivity) getActivity()).chapterNumber;
		mBookName = ((SelectScriptureFragmentActivity) getActivity()).bookName;
		mCheckedCheckBoxes = ((SelectScriptureFragmentActivity) getActivity()).mVerseNumbers;
		updateTitle();
		
		mGridView = (GridView) getView().findViewById(R.id.gridView1);
		mGridView.setOnItemClickListener(new OnItemClickListener(){


			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onItemClick(AdapterView<?> aView,
					View view, int position, long id) {
				// Get scriptures and send them to the displayed scripture screen.
				if(Build.VERSION.SDK_INT>=11){
					TextView textView = (TextView) view;
					if(textView.isActivated()==((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix){
						mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
						textView.setActivated(true);
					}
					else{
						try{
							mCheckedCheckBoxes.remove(mCheckedCheckBoxes.indexOf((Integer) position+1));
							textView.setActivated(false);
						}catch(ArrayIndexOutOfBoundsException e){
							((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix = !((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix;
							mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
							textView.setActivated(true);

						}
					}
				}
				//What a hack!
				updateTitle();
			}



		});
		mAdapter = new ArrayAdapter<String>(getActivity(), 
				0, 
				Bible.getInstance().getBook(mBookNumber).getChapter(mChapterNumber).getVersesArray()){
			@SuppressLint("NewApi")
			public View getView(int position, View convertView, ViewGroup parent){
				if(Build.VERSION.SDK_INT < 11){
					final int currrentPosition = position;
					CheckBox textView;
					if(convertView==null)
						textView = (CheckBox) new CheckBox(getActivity());//((LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.layout_checkbox_item, null)).findViewById(R.id.checkBox1);
					else
						textView = (CheckBox) convertView;
					textView.setText(String.valueOf(position+1));
					if(mCheckedCheckBoxes.contains(position+1))
						textView.setChecked(true);
					else
						textView.setChecked(false);
					textView.setOnCheckedChangeListener(new OnCheckedChangeListener(){

						@Override
						public void onCheckedChanged(CompoundButton button,
								boolean checked) {
							// TODO Auto-generated method stub
							if(checked==((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix){
								mCheckedCheckBoxes.add(Integer.valueOf((String) button.getText()));
							}
							else{
								try{
									mCheckedCheckBoxes.remove(mCheckedCheckBoxes.indexOf((Integer) currrentPosition+1));
								}catch(ArrayIndexOutOfBoundsException e){
									((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix = !((SelectScriptureFragmentActivity) getActivity()).checkedActivatedFix;
									mCheckedCheckBoxes.add(Integer.valueOf((String) button.getText()));
								}
							}
							updateTitle();
						}
						
					});
					return textView;
				}else{

					TextView textView;
					if(convertView == null){
						textView = (TextView) getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_activated_1, null);
					}else{
						textView = (TextView) convertView;
					}
					textView.setText(String.valueOf(position+1));
					if(mCheckedCheckBoxes.contains(position+1))
						textView.setActivated(true);
					else
						textView.setActivated(false);
					return textView;
				}
			}
		};
		mGridView.setAdapter(mAdapter);
	}
	private void updateTitle() {
		// TODO Auto-generated method stub
		Collections.sort(mCheckedCheckBoxes);
		String verseString = "";
		for(int i=0;i<mCheckedCheckBoxes.size();i++){
			if(i==0){
				verseString = ":"+String.valueOf(mCheckedCheckBoxes.get(i));
			}else if(mCheckedCheckBoxes.get(i)==mCheckedCheckBoxes.get(i-1)+1){
				verseString+= "-";
				i+=1;
				while(i<mCheckedCheckBoxes.size() && mCheckedCheckBoxes.get(i)==mCheckedCheckBoxes.get(i-1)+1){
					i+=1;
				}
				i-=1;
				verseString+= mCheckedCheckBoxes.get(i);
			}else{
				verseString+=",";
				verseString+=mCheckedCheckBoxes.get(i);
			}

		}
		((SelectScriptureFragmentActivity) getActivity()).getSupportActionBar().setTitle(mBookName+" "+String.valueOf(mChapterNumber+1)+verseString);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.activity_select_scripture, menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent(getActivity(),DisplaySelectedScripture.class);
		intent.putExtra("book_name", mBookName);
		intent.putExtra("chapter_number", mChapterNumber+1);
		Bundle bundle = new Bundle();

		switch(item.getItemId()){
		case R.id.preview:
			if(mCheckedCheckBoxes.size()==0){
				ArrayList<Integer> allCheckBoxes = new ArrayList<Integer>();
				//Add all to the array list
				for(int i=1;i<=Bible.getInstance().getBook(mBookNumber).getChapter(mChapterNumber).getVersesArray().length;i++){
					allCheckBoxes.add(i);
				}
				Collections.sort(allCheckBoxes);
				bundle.putIntegerArrayList("verses", allCheckBoxes);
			}else{
				Collections.sort(mCheckedCheckBoxes);
				bundle.putIntegerArrayList("verses", mCheckedCheckBoxes);
			}
		}
		intent.putExtra("bundle", bundle);
		startActivityForResult(intent, 0);
		return true;
	}
}
