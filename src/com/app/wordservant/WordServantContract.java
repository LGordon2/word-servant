package com.app.wordservant;

import android.net.Uri;
import android.provider.BaseColumns;

public class WordServantContract {
	private WordServantContract(){}
	public static final String DATABASE_NAME = "wordservant_db";
	public static final String AUTHORITY = "com.app.provider.wordservant";
	public abstract static class ScriptureEntry implements BaseColumns{
		//Uri making.
		private static final String SCHEME = "content://";
		public static final String TABLE_NAME = "scriptures";
        /**
         * Path part for the Scriptures URI
         */
		private static final String PATH_SCRIPTURES = "/"+TABLE_NAME;

        /**
         * Path part for the Scripture ID URI
         */
        private static final String PATH_SCRIPTURE_ID = "/"+TABLE_NAME+"/";

        /**
         * 0-relative position of a note ID segment in the path part of a note ID URI
         */
        public static final int SCRIPTURE_ID_PATH_POSITION = 1;
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_SCRIPTURES);
        /**
         * The content URI base for a single scripture. Callers must
         * append a numeric note id to this Uri to retrieve a scripture
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_SCRIPTURE_ID);
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
