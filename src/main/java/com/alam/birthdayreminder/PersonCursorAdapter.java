package com.alam.birthdayreminder;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.alam.birthdayreminder.db.PersonContract;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PersonCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public PersonCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTv = (TextView) view.findViewById(R.id.name);
        TextView birthdayTv = (TextView) view.findViewById(R.id.birthday);
        TextView ageTv = (TextView) view.findViewById(R.id.age);
        String name = cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_NAME));
        long dob = cursor.getLong(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_DOB));
        nameTv.setText(name);
        birthdayTv.setText(new SimpleDateFormat("EEEE, MMM,  d").format(dob));
        ageTv.setText(Integer.toString(getAge(dob)));
    }

    private int getAge(long dob) {
        Calendar calendar = Calendar.getInstance();
        Calendar age = Calendar.getInstance();
        age.setTimeInMillis(dob);
        return calendar.get(Calendar.YEAR) - age.get(Calendar.YEAR);
    }
}