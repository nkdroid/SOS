package com.nkdroid.sos.Model;

import java.util.ArrayList;

/**
 * Created by Android on 15-04-2015.
 */
public class SettingData {

    public boolean isSms=false;

    public boolean isEmail=false;

    public boolean isSensor=false;

    public boolean isSound=false;

    public String selectedSms;

    public ArrayList<ContactInfo> contactList=new ArrayList<>();
}
