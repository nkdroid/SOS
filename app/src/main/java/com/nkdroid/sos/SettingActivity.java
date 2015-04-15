package com.nkdroid.sos;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingActivity extends ActionBarActivity {

    private Button addcontact;
    String cname,cno;
    private EditText etcname,etcno;


        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

    }
    private void initView() {
        addcontact = (Button) findViewById(R.id.addcontact);

        etcname= (EditText)findViewById(R.id.etcname);
        etcno= (EditText)findViewById(R.id.etcno);
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove sharedpreference

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);

            }
        });

    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (1):
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri contactData = data.getData();
//                    Cursor c = managedQuery(contactData, null, null, null, null);
//                    if (c.moveToFirst()) {
//                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                        String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        contactDetail.setText(name+number);
//                    }
//                }

                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(data.getData(),
                        null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        cname= cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            //Query phone here.  Covered next
                            if (Integer.parseInt(cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                Cursor pCur = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                        new String[]{id}, null);
                                while (pCur.moveToNext()) {
                                    // Do something with phones
                                    cno= pCur.getString(
                                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                }

                                pCur.close();
                            }
                        }
                    }
                }
                etcname.setText(cname);
                etcno.setText(cno);

                break;
        }
    }



}
