package com.alam.birthdayreminder;
import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BACKUP_LOADER_ID = 1;
    private TabLayout tabLayout;
    private SimpleFragmentPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startNotification();
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new SimpleFragmentPageAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBirthdayActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
            }
        });
    }

    private void startNotification() {
        AlarmManager alarmManager;

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        Date date = new Date();
        now.setTime(date);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        Log.v(LOG_TAG, "Time: " + calendar.getTimeInMillis() + "");
        if (calendar.before(now)) {
            calendar.add(Calendar.DATE, 1);
        }
        Intent intent = new Intent(this, NotiReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        Log.v(LOG_TAG, "Alarm started");
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "BackupLoader Created");
        return new BackupLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        Loader<JSONObject> backupLoader = getLoaderManager().getLoader(BACKUP_LOADER_ID);
        BackupLoader backupLoader1 = (BackupLoader) backupLoader;
        if (backupLoader1.progressDialogIsShow())
            backupLoader1.stopProgressDialog();
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.btn_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
    }
}
