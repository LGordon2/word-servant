package com.app.wordservant.ui;

public interface ReviewFragment{
	
	void setScriptureReference(CharSequence reference);
	CharSequence getScriptureReference();
	//public abstract void setScriptureTags(String tags);
	void setScriptureText(CharSequence text);
	CharSequence getScriptureText();
	void resetView();
	
}