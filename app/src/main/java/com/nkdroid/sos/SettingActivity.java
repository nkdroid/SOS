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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nkdroid.sos.Model.ContactInfo;
import com.nkdroid.sos.Model.SettingData;

import org.w3c.dom.Text;


public class SettingActivity extends ActionBarActivity {

    private TextView addcontact;
    String cname,cno;
    private SettingData settingData;
    private LinearLayout contactContainer,messageContainer;
    private Switch smsSwitch,emailSwitch,sensorSwitch,soundSwitch;



        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);




    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        addcontact = (TextView) findViewById(R.id.addcontact);
        settingData=PrefUtils.getSettingData(SettingActivity.this);
//        etcname= (EditText)findViewById(R.id.etcname);
//        etcno= (EditText)findViewById(R.id.etcno);
        contactContainer= (LinearLayout) findViewById(R.id.contactContainer);
        messageContainer= (LinearLayout) findViewById(R.id.messageContainer);
        smsSwitch= (Switch) findViewById(R.id.smsSwitch);
        emailSwitch= (Switch) findViewById(R.id.emailSwitch);
        sensorSwitch= (Switch) findViewById(R.id.sensorSwitch);
        soundSwitch= (Switch) findViewById(R.id.soundSwitch);
//        try {
            if (settingData != null) {
                smsSwitch.setChecked(settingData.isSms);
                emailSwitch.setChecked(settingData.isEmail);
                sensorSwitch.setChecked(settingData.isSensor);
                soundSwitch.setChecked(settingData.isSound);
                if (settingData.contactList.size() > 0) {

                    for(int i=0;i<settingData.contactList.size();i++) {

                        final int position=i;
                        final View contactView = getLayoutInflater().inflate(R.layout.contactview, contactContainer, false);
                        TextView contactName = (TextView) contactView.findViewById(R.id.contactName);
                        contactName.setText(settingData.contactList.get(i).contactName);
                        TextView contactNumber = (TextView) contactView.findViewById(R.id.contactNumber);
                        ImageView deleteContact = (ImageView) contactView.findViewById(R.id.deleteContact);
                        deleteContact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                settingData=PrefUtils.getSettingData(SettingActivity.this);
//                                settingData.contactList.remove(new ContactInfo(settingData.contactList.get(position).contactName,settingData.contactList.get(position).contactNumber));
                                if(settingData.contactList.size()==1) {

                                    settingData.contactList.remove(0);
                                } else {
                                    settingData.contactList.remove(position);
                                }
                                contactContainer.removeView(contactView);
                                PrefUtils.saveSettingData(settingData, SettingActivity.this);

                            }
                        });
                        contactNumber.setText(settingData.contactList.get(i).contactNumber);
                        contactContainer.addView(contactView);
                    }
                }

                 View contactView = getLayoutInflater().inflate(R.layout.smsview, messageContainer, false);
                TextView smsName = (TextView) contactView.findViewById(R.id.txtMessage);
                smsName.setText(settingData.selectedSms);
                messageContainer.addView(contactView);

            } else {
                settingData = new SettingData();
                View contactView = getLayoutInflater().inflate(R.layout.smsview, messageContainer, false);
                TextView smsName = (TextView) contactView.findViewById(R.id.txtMessage);
                smsName.setText(settingData.selectedSms);
                messageContainer.addView(contactView);
            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        smsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingData.isSms = true;
                    PrefUtils.saveSettingData(settingData, SettingActivity.this);
                } else {
                    settingData.isSms = false;
                    PrefUtils.saveSettingData(settingData, SettingActivity.this);
                }
            }
        });

        emailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settingData.isEmail=true;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                } else {
                    settingData.isEmail=false;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                }
            }
        });

        sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settingData.isSensor=true;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                } else {
                    settingData.isSensor=false;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                }
            }
        });

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    settingData.isSound=true;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                } else {
                    settingData.isSound=false;
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);
                }
            }
        });


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
                try {
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
                            cname = cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                //Query phone here.  Covered next
                                if (Integer.parseInt(cur.getString(
                                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                    Cursor pCur = cr.query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                            new String[]{id}, null);
                                    while (pCur.moveToNext()) {
                                        // Do something with phones
                                        cno = pCur.getString(
                                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    }

                                    pCur.close();
                                }
                            }
                        }
                    }
                    contactContainer.removeAllViews();
                    messageContainer.removeAllViews();
                    settingData.selectedSms="new";
                    settingData.contactList.add(new ContactInfo(cname, cno));
                    PrefUtils.saveSettingData(settingData,SettingActivity.this);

                } catch(Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }



}
