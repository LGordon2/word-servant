package com.app.wordservant.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.app.wordservant.R;

public class FlashcardFragment extends ReviewFragment{

	public void onActivityCreated (Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		Integer positionOnScreen = getActivity().getIntent().getIntExtra("positionOnScreen", 0);
		final Bundle bundledScriptureList = getActivity().getIntent().getBundleExtra("bundledScriptureList");
		this.displayScriptureContent(bundledScriptureList.getInt(String.valueOf(positionOnScreen)));
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.flashcard_layout, null);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		//Set up click events, as well as fields.
		final ViewSwitcher cardFlipper = (ViewSwitcher) getView().findViewById(R.id.cardSwitcher);
		OnClickListener flipViewListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cardFlipper.getDisplayedChild()==0)
					cardFlipper.setDisplayedChild(1);
				else
					cardFlipper.setDisplayedChild(0);
			}
		};
		RelativeLayout referenceLayout = (RelativeLayout) getView().findViewById(R.id.referenceLayout);
		TextView scriptureText = (TextView) getView().findViewById(R.id.scriptureText);
		referenceLayout.setOnClickListener(flipViewListener);
		scriptureText.setOnClickListener(flipViewListener);

		//Set up coloring.
		//Define the front and back of the cards.

		RelativeLayout frontCard;
		RelativeLayout backCard;

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){
			cardFlipper.setDisplayedChild(1);
			frontCard = (RelativeLayout) cardFlipper.findViewById(R.id.scriptureLayout);
			backCard = (RelativeLayout) cardFlipper.findViewById(R.id.referenceLayout);
		}else{
			cardFlipper.setDisplayedChild(0);
			frontCard = (RelativeLayout) cardFlipper.findViewById(R.id.referenceLayout);
			backCard = (RelativeLayout) cardFlipper.findViewById(R.id.scriptureLayout);
		}

		//Change the color for these cards accordingly.
		frontCard.setBackgroundResource(R.drawable.front_of_flashcard);
		for (int i=0;i<frontCard.getChildCount()-1;i++){
			TextView textView = (TextView) frontCard.getChildAt(i);
			textView.setTextColor(getResources().getColor(R.color.card_front_text_color));
		}
		backCard.setBackgroundResource(R.drawable.back_of_flashcard);
		for (int i=0;i<backCard.getChildCount()-1;i++){
			TextView textView = (TextView) backCard.getChildAt(i);
			textView.setTextColor(getResources().getColor(R.color.word_servant_purple));
		}
	}
}