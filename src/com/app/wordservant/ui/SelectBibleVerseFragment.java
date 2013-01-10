package com.app.wordservant.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.app.wordservant.R;

public class SelectBibleVerseFragment extends SherlockFragment{
	private int mBookNumber;
	private int mChapterNumber;
	private boolean checkedActivatedFix = true;
	private ArrayAdapter<String> mAdapter;
	private GridView mGridView;
	private ArrayList<Integer> mCheckedCheckBoxes;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.select_bible_chapter, null);
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	public void onStart(){
		super.onStart();
		getSherlockActivity().getSupportActionBar().setTitle(
				((SelectScriptureFragmentActivity) getActivity()).bookName
				+ " " + 
				String.valueOf(((SelectScriptureFragmentActivity) getActivity()).chapterNumber+1));
		mBookNumber = ((SelectScriptureFragmentActivity) getActivity()).bookNumber;
		mChapterNumber = ((SelectScriptureFragmentActivity) getActivity()).chapterNumber;
		mGridView = (GridView) getView().findViewById(R.id.gridView1);
		mCheckedCheckBoxes = new ArrayList<Integer>();
		mGridView.setOnItemClickListener(new OnItemClickListener(){


			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onItemClick(AdapterView<?> aView,
					View view, int position, long id) {
				// Get scriptures and send them to the displayed scripture screen.
				if(Build.VERSION.SDK_INT<11){
					CheckedTextView textView = (CheckedTextView) view;
					textView.setChecked(!textView.isChecked());
					if(textView.isChecked()==checkedActivatedFix){
						mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
					}
					else{
						try{
							mCheckedCheckBoxes.remove(mCheckedCheckBoxes.indexOf((Integer) position+1));
						}catch(ArrayIndexOutOfBoundsException e){
							checkedActivatedFix = !checkedActivatedFix;
							mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
						}
					}
				}else{
					TextView textView = (TextView) view;
					if(textView.isActivated()==checkedActivatedFix){
						mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
						textView.setActivated(true);
					}
					else{
						try{
							mCheckedCheckBoxes.remove(mCheckedCheckBoxes.indexOf((Integer) position+1));
							textView.setActivated(false);
						}catch(ArrayIndexOutOfBoundsException e){
							checkedActivatedFix = !checkedActivatedFix;
							mCheckedCheckBoxes.add(Integer.valueOf((String) textView.getText()));
							textView.setActivated(true);
							
						}
					}
				}
				//What a hack!
				SelectBibleVerseFragment.this.setMenuVisibility(false);
				SelectBibleVerseFragment.this.setMenuVisibility(true);
			}

		});
		mAdapter = new ArrayAdapter<String>(getActivity(), 
				0, 
				Bible.getInstance().getBook(mBookNumber).getChapter(mChapterNumber).getVersesArray()){
			@SuppressLint("NewApi")
			public View getView(int position, View convertView, ViewGroup parent){
				if(Build.VERSION.SDK_INT < 11){
					CheckedTextView textView;
					if(convertView==null)
						textView = (CheckedTextView) getActivity().getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);
					else
						textView = (CheckedTextView) convertView;
					textView.setText(String.valueOf(position+1));
					if(mCheckedCheckBoxes.contains(position+1))
						textView.setChecked(true);
					else
						textView.setActivated(true);
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.activity_select_scripture, menu);
	}
	public void onPrepareOptionsMenu(Menu menu){
		
		if(mCheckedCheckBoxes == null || mCheckedCheckBoxes.size()==0)
			menu.getItem(1).setEnabled(false);
		else
			menu.getItem(1).setEnabled(true);
	}
}
