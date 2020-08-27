package com.alam.birthdayreminder;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alam.birthdayreminder.db.PersonContract;

public class ContactListFragment extends Fragment {

    public static final String EXTRA_ID = "com.alam.birthdayreminder.ID";
    private PersonCursorAdapter adapter;
    private TextView tv;
    private ProgressBar loadingBar;

    public ContactListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        loadingBar = (ProgressBar) rootView.findViewById(R.id.loading_bar);
        tv = (TextView) rootView.findViewById(R.id.no_birthday);
        listView.setEmptyView(tv);
        adapter = new PersonCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ViewBirthdayActivity.class);
                intent.putExtra(EXTRA_ID, c.getLong(c.getColumnIndex(PersonContract.PersonEntry._ID)));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);
            }
        });
        return rootView;
    }
}