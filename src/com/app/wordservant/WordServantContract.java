package com.app.wordservant;

import android.provider.BaseColumns;

public class WordServantContract {
	private WordServantContract(){}
	public static final String DB_NAME = "wordservant_db";
	public abstract class ScriptureEntry implements BaseColumns{
	    public static final String TABLE_NAME = "scriptures";
	    public static final String COLUMN_NAME_REFERENCE = "reference";
	    public static final String COLUMN_NAME_TEXT = "text";
	    public static final String COLUMN_NAME_CREATED_DATE = "created_date";
	    public static final String COLUMN_NAME_SCHEDULE = "schedule";
	    public static final String COLUMN_NAME_LAST_REVIEWED_DATE = "last_reviewed_date";
	    public static final String COLUMN_NAME_TIMES_REVIEWED = "times_reviewed";
	    public static final String COLUMN_NAME_NEXT_REVIEW_DATE = "next_review_date";
		public static final String COLUMN_NAME_CORRECTLY_REVIEWED_COUNT = "correctly_reviewed_count";
		public static final String COLUMN_NAME_INCORRECTLY_REVIEWED_COUNT = "incorrectly_reviewed_count";
		public static final String COLUMN_NAME_SKIPPED_REVIEW_COUNT = "skipped_review_count";
	    
	}
	public abstract class TagEntry implements BaseColumns{
		public static final String TABLE_NAME = "tags";
		public static final String COLUMN_NAME_TAG_NAME = "tag_name";
	}
	public abstract class CategoryEntry implements BaseColumns{
		public static final String TABLE_NAME = "categories";
		public static final String COLUMN_NAME_SCRIPTURE_ID = "scripture_id";
		public static final String COLUMN_NAME_TAG_ID = "tag_id";
	}
}
