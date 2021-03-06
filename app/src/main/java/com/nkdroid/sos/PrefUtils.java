/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nkdroid.sos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.nkdroid.sos.Model.ComplexPreferences;
import com.nkdroid.sos.Model.SettingData;


public class PrefUtils  {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("isLogin", false);
    }

    public static User getLoggedIn(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        User user=new User();
        user.isLogin=sp.getBoolean("isLogin", false);
        user.setUsername(sp.getString("username", ""));
        user.setPassword(sp.getString("password", ""));
        Log.e("uname...",user.getUsername()+"");
        return user;
    }

    public static void setLoggedIn(final Context context, final boolean isLoggedIn, final String username,final String password) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isLogin", isLoggedIn).putString("username",username).putString("password",password).commit();

    }

    public static void clearLogin(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isLogin", false).commit();
    }



    public static void saveSettingData(SettingData offer, Context ctx){

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "setting_data", 0);
        complexPreferences.putObject("setting_value", offer);
        complexPreferences.commit();

    }

    public static SettingData getSettingData(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "setting_data", 0);
        SettingData offer = complexPreferences.getObject("setting_value", SettingData.class);
        return offer;
    }


}
