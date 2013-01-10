package com.app.wordservant.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.app.wordservant.R;

public class SelectBibleChapterFragment extends SherlockFragment{
	private ArrayAdapter<String> mAdapter;
	private int mBookNumber;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.select_bible_chapter, null);
	}
	public void onStart(){
		super.onStart();
		getSherlockActivity().getSupportActionBar().setTitle(((SelectScriptureFragmentActivity) getActivity()).bookName);
		mBookNumber = ((SelectScriptureFragmentActivity) getActivity()).bookNumber;
		mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,Bible.getInstance().getBook(mBookNumber).getChaptersArray());
		GridView chapterView = (GridView) getView().findViewById(R.id.gridView1);
		chapterView.setAdapter(mAdapter);
		chapterView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				((SelectScriptureFragmentActivity) getActivity()).chapterNumber = position;
				((SelectScriptureFragmentActivity) getActivity()).setCurrentTab(2);
			}

		});

		mAdapter.notifyDataSetChanged();
	}
	
}