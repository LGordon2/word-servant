package com.app.wordservant.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.actionbarsherlock.app.SherlockFragment;
import com.app.wordservant.R;

@SuppressLint("NewApi")
public class FlashcardFragment extends SherlockFragment implements ReviewFragment{

	private TextView mScriptureTextField;
	private TextView mScriptureReferenceField;

	public void onActivityCreated (Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.flashcard_layout, null);
	}

	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean referenceOnFront = sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture") ? false : true;
		//Set up click events, as well as fields.
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

		mScriptureReferenceField = (TextView) getView().findViewById(R.id.referenceText);
		mScriptureTextField = (TextView) getView().findViewById(R.id.scriptureText);

		//Set up coloring.
		//Define the front and back of the cards.

		RelativeLayout frontCard;
		RelativeLayout backCard;

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

	public void resetView(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		final ViewSwitcher cardFlipper = (ViewSwitcher) getView().findViewById(R.id.cardSwitcher);
		
		if(sharedPreferences.getString("pref_key_review_select", "none").equals("showing_scripture")){
			cardFlipper.setDisplayedChild(1);
		}else{
			cardFlipper.setDisplayedChild(0);
		}
		((ScrollView) getView().findViewById(R.id.scriptureScroll)).scrollTo(0,0);
	}
	
	@Override
	public void setScriptureReference(CharSequence reference) {
		// TODO Auto-generated method stub
		mScriptureReferenceField.setText(reference);
	}

	@Override
	public CharSequence getScriptureReference() {
		// TODO Auto-generated method stub
		return mScriptureReferenceField.getText();
	}

	@Override
	public void setScriptureText(CharSequence text) {
		// TODO Auto-generated method stub
		mScriptureTextField.setText(text);
	}

	@Override
	public CharSequence getScriptureText() {
		// TODO Auto-generated method stub
		return mScriptureTextField.getText();
	}
}
