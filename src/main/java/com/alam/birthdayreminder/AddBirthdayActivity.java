package com.alam.birthdayreminder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.alam.birthdayreminder.db.PersonDBHelper;
import com.alam.birthdayreminder.db.PersonDBQueries;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddBirthdayActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddBirthdayActivity.class.getSimpleName();
    private EditText etName, etDob;
    private Switch aSwitch;
    private boolean saved = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("AddSavingState", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        etName = (EditText) findViewById(R.id.add_birthday_name);
        etDob = (EditText) findViewById(R.id.birthday_date);
        aSwitch = (Switch) findViewById(R.id.show_noti);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = etName.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH);
                    Date date = dateFormat.parse(etDob.getText().toString());
                    Boolean isChecked = aSwitch.isChecked();

                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(AddBirthdayActivity.this, R.string.warning_message_no_fillup, Toast.LENGTH_SHORT).show();
                    } else {
                        PersonDBQueries dbQueries = new PersonDBQueries(new PersonDBHelper(getApplicationContext()));
                        Person person = new Person(name, date, isChecked);
                        if (dbQueries.insert(person) != 0) {
                            saved = true;
                            Toast.makeText(AddBirthdayActivity.this, R.string.inserted, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } catch (ParseException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    Toast.makeText(AddBirthdayActivity.this, R.string.warning_message_no_fullup_date, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (saved) {
            editor.clear();
        } else {
            String name = etName.getText().toString();
            String birthday = etDob.getText().toString();
            editor.putString("SAVE_STATE_NAME", name);
            editor.putString("SAVE_STATE_DOB", birthday);
        }
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = sharedPreferences.getString("SAVE_STATE_NAME", "");
        String birthday = sharedPreferences.getString("SAVE_STATE_DOB", "");
        etName.setText(name);
        etDob.setText(birthday);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }
}