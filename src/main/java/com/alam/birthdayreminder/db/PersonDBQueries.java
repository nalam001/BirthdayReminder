package com.alam.birthdayreminder.db;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alam.birthdayreminder.Person;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PersonDBQueries {
    private PersonDBHelper helper;
    public PersonDBQueries(PersonDBHelper helper) {
        this.helper = helper;
    }

    public static Person getPerson(Cursor cursor) {
        Person person = null;
        if (cursor.moveToNext()) {
            person = setPerson(cursor);
        }
        return person;
    }

    public static List<Person> getPersonList(Cursor cursor) {
        List<Person> persons = new ArrayList<>();
        while (cursor.moveToNext()) {
            Person person = setPerson(cursor);
            persons.add(person);
        }
        return persons;
    }

    private static Person setPerson(Cursor cursor) {
        Person person = new Person(
                cursor.getString(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_NAME)),
                new Date(cursor.getLong(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_DOB))),
                checkBoolean(cursor.getInt(cursor.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_NOTIFY)))
        );
        person.setId(cursor.getLong(cursor.getColumnIndex(PersonContract.PersonEntry._ID)));

        return person;
    }

    private static boolean checkBoolean(int value) {
        return value > 0;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(PersonContract.PersonEntry.TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public Cursor queryTodayBirthday(Calendar calender) {
        String[] columns = PersonContract.columns;
        String[] selectionArgs = {calender.getTimeInMillis() + "", "" + calender.getTimeInMillis()};
        return query(columns, "strftime('%m-%d'," + PersonContract.PersonEntry.COLUMN_NAME_DOB + "/1000, 'unixepoch')"
                        + " BETWEEN strftime('%m-%d',?/1000, 'unixepoch') AND strftime('%m-%d',?/1000, 'unixepoch')"
                        + "AND " + PersonContract.PersonEntry.COLUMN_NAME_NOTIFY + " = '1'",
                selectionArgs, null, null, null);
    }

    public long insert(Person person) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = putValues(person);
        long id = db.insert(PersonContract.PersonEntry.TABLE_NAME, null, values);
        person.setId(id);

        return id;
    }

    public int update(Person person) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = putValues(person);
        String selection = PersonContract.PersonEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(person.getId())};

        return db.update(PersonContract.PersonEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteOne(Long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String selection = PersonContract.PersonEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        db.delete(PersonContract.PersonEntry.TABLE_NAME, selection, selectionArgs);
    }

    private ContentValues putValues(Person person) {
        ContentValues values = new ContentValues();
        values.put(PersonContract.PersonEntry.COLUMN_NAME_NAME, person.getName());
        values.put(PersonContract.PersonEntry.COLUMN_NAME_DOB, person.getDOBAsCalender().getTimeInMillis());
        values.put(PersonContract.PersonEntry.COLUMN_NAME_NOTIFY, person.isNotify());
        return values;
    }
}