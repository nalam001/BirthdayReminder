package com.alam.birthdayreminder;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import com.alam.birthdayreminder.db.PersonDBHelper;
import com.alam.birthdayreminder.db.PersonDBQueries;
import java.util.Calendar;

public class NotifyIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static String LOG_TAG = NotifyIntentService.class.getSimpleName();

    public NotifyIntentService() {
        super(NotifyIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_MONTH, -1);
        PersonDBQueries dbQuery = new PersonDBQueries(new PersonDBHelper(this));
        Cursor cursor = dbQuery.queryTodayBirthday(calender);
        int todayBirthday = cursor.getCount();
        Log.v(LOG_TAG, "today birthday: " + todayBirthday);
        if (todayBirthday > 0) {
            Notification.Builder builder = new Notification.Builder(this);
                    builder.setContentTitle(getString(R.string.notification_title));
                    builder.setContentText(String.format(getString(R.string.notification_content), todayBirthday));
                    builder.setAutoCancel(true);
                    builder.setColor(getResources().getColor(R.color.colorPrimary));
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            Log.v(LOG_TAG, "notification built");

            Intent todayBirthdayIntent = new Intent(this, TodayBirthdayActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, todayBirthdayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            Notification notification = builder.build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(NOTIFICATION_ID, notification);

            builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        }
    }
}