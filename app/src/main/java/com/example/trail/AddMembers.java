package com.example.trail;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AddMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
       // getContactCursor(this ," ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_members, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static Cursor getContactCursor(ContentResolver contactHelper,
                                          String startsWith) {
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        Cursor cur = null;
        try {
            if (startsWith != null && !startsWith.equals("")) {
                cur = contactHelper.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " like \"" + startsWith + "%\"", null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " ASC");
            } else {
                cur = contactHelper.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection, null, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                                + " ASC");
            }
            cur.moveToFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cur;
    }

}
