package com.app.wordservant;

import android.provider.BaseColumns;

public class WordServantContract {
	private WordServantContract(){}
	public abstract class ScriptureEntry implements BaseColumns{
	    public static final String TABLE_NAME = "scriptures";
	    public static final String COLUMN_NAME_REFERENCE = "reference";
	    public static final String COLUMN_NAME_TAG_ID = "tag_id";
	    public static final String COLUMN_NAME_TEXT = "text";
	    public static final String COLUMN_NAME_CREATED_DATE = "created_date";
	    public static final String COLUMN_NAME_SCHEDULE = "schedule";
	    public static final String COLUMN_NAME_LAST_REVIEWED_DATE = "last_reviewed_date";
	    public static final String COLUMN_NAME_TIMES_REVIEWED = "times_reviewed";
	    public static final String COLUMN_NAME_NEXT_REVIEW_DATE = "next_review_date";
	    
	}
	public abstract class TagEntry implements BaseColumns{
		public static final String TABLE_NAME = "tags";
	}
}
