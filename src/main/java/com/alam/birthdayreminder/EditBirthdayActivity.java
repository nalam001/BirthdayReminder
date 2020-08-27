package com.alam.birthdayreminder;
import android.content.Intent;
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
import java.util.Locale;

public class EditBirthdayActivity extends AppCompatActivity {
    private static final String LOG_TAG = EditBirthdayActivity.class.getSimpleName();
    private Person person;
    private EditText etName, etDob;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_birthday);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra(ViewBirthdayActivity.EXTRA_ID);
        setView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = etName.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(EditBirthdayActivity.this, R.string.warning_message_no_fillup, Toast.LENGTH_SHORT).show();
                    } else {
                        person.setName(name);
                        person.setDob(new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH).parse(etDob.getText().toString()));
                        person.setNotify(aSwitch.isChecked());

                        PersonDBQueries dbQueries = new PersonDBQueries(new PersonDBHelper(getApplicationContext()));
                        if (dbQueries.update(person) != 0) {
                            Toast.makeText(EditBirthdayActivity.this, R.string.updated, Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(EditBirthdayActivity.this, R.string.db_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    Log.v(LOG_TAG, e.getMessage());
                    Toast.makeText(EditBirthdayActivity.this, R.string.warning_message_no_fullup_date, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setView() {
        etName = (EditText) findViewById(R.id.add_birthday_name);
        etDob = (EditText) findViewById(R.id.birthday_date);
        aSwitch = (Switch) findViewById(R.id.show_noti);
        etName.setText(person.getName());
        etDob.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(person.getDOB()));
        aSwitch.setChecked(person.isNotify());
    }

    public void showDatePickerDialog(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }
}