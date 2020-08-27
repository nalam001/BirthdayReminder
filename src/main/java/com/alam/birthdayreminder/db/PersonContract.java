package com.alam.birthdayreminder.db;
import android.provider.BaseColumns;

public class PersonContract {

    public static String[] columns = {
            PersonContract.PersonEntry._ID,
            PersonContract.PersonEntry.COLUMN_NAME_NAME,
            PersonContract.PersonEntry.COLUMN_NAME_DOB,
            PersonContract.PersonEntry.COLUMN_NAME_NOTIFY,
    };

    public static class PersonEntry implements BaseColumns {
        public static final String TABLE_NAME = "person";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DOB = "dob";
        public static final String COLUMN_NAME_NOTIFY = "notify";
    }
}