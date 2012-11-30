/**
 * 
 */
package com.app.wordservant;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * @author lewis.gordon
 *
 */
public class NotificationService extends IntentService{

	public NotificationService(){
		super("NotificationService");
	}

	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		synchronized (this) {
			try {
				String contentText = null;
				SQLiteDatabase wordservant_db = new WordServantOpenHelper(this, getResources().getString(R.string.database_name), null, 1).getReadableDatabase();
				Cursor wordservantCursor = wordservant_db.query("scriptures", new String[]{"NEXT_REVIEW_DATE"}, "NEXT_REVIEW_DATE<=date('now')", null, null, null, null);
				NotificationManager mNotificationManager =
						(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				if (wordservantCursor.getCount() == 0){
					mNotificationManager.cancel(0);
					//continue;
				} else if(wordservantCursor.getCount() == 1){
					contentText = wordservantCursor.getCount()+" scripture due today.";
				} else {
					contentText = wordservantCursor.getCount()+" scriptures due today.";
				}

				NotificationCompat.Builder mBuilder =
						new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.word_servant_icon)
				.setContentTitle("Word Servant")
				.setContentText(contentText)
				.setAutoCancel(true);
				// Creates an explicit intent for an Activity in your app
				Intent resultIntent = new Intent(this, TodaysMemoryVerses.class);

				// The stack builder object will contain an artificial back stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				// Adds the back stack for the Intent (but not the Intent itself)
				stackBuilder.addParentStack(TodaysMemoryVerses.class);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
						stackBuilder.getPendingIntent(
								0,
								PendingIntent.FLAG_UPDATE_CURRENT
								);
				mBuilder.setContentIntent(resultPendingIntent);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(0, mBuilder.build());
				wordservant_db.close();
				wordservantCursor.close();
			} catch (Exception e) {
			}
		}

	}

}
