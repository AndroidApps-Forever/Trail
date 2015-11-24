package com.example.trail;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ArrayList<String> nameOfContacts = new ArrayList<String>();
    private ArrayList<String> phoneNoOfContacts = new ArrayList<String>();
    private ActionMode mActionMode;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_contacts);

        fetchContacts(this.getContentResolver());

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ContactsActivity.this, PrivatechatActivity.class);
                startActivity(i);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //return false;
                //Toast.makeText(ContactsActivity.this,"Wanna make a group?",Toast.LENGTH_SHORT).show();
                mActionMode = startActionMode(new ActionModeCallback());
                return true;
            }
        });

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
        }    // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


        //return super.onOptionsItemSelected(item);
    }


    public void fetchContacts(ContentResolver cr)
    {
        Cursor phones = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
        if(phones.getCount() > 0)
        {
            while (phones.moveToNext())
            {
                String contactId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (Integer.parseInt(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //Query and loop for every phone number of the contact
                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] {contactId}, null);
                    if(phoneCursor.moveToNext())
                    {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        nameOfContacts.add(name);
                        phoneNoOfContacts.add(phoneNumber);
                    }
                    phoneCursor.close();
                }
            }
        }
        phones.close();

        CustomPhoneListAdapter adapter=new CustomPhoneListAdapter(this, nameOfContacts, phoneNoOfContacts);
        list=(ListView)findViewById(R.id.listView2);
        list.setAdapter(adapter);
    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.contextual_contacts_list_view, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(ContactsActivity.this, "Wanna create a group?", Toast.LENGTH_LONG).show();

            //PERFORM TASKS ON THE CLICK OF CREATE GROUP


            // close action mode
            mode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection
            mActionMode = null;
        }
    }
}
