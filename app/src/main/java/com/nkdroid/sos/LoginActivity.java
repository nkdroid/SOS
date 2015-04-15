package com.nkdroid.sos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class LoginActivity extends ActionBarActivity {

    private EditText etUsername,etPassword;
    private Button btnLogin,btnNewRegistration;
    String c1,c2,c3,c4;
    private ProgressDialog dialog;
    private String resp;
    public static final String SOAP_ACTION = "http://tempuri.org/UserLogin";
    public static  final String OPERATION_NAME = "UserLogin";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";


    public static  final String SOAP_ADDRESS = "http://testst1.somee.com/WebService.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //check isLogin or not
        if(PrefUtils.isLoggedIn(LoginActivity.this)){
            Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        initView();
    }

    private void initView() {
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnNewRegistration= (Button) findViewById(R.id.btnNewRegistration);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(etUsername)){
                    Toast.makeText(LoginActivity.this,"Please Enter Username",Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etPassword)){
                    Toast.makeText(LoginActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                } else {
                    c1 = etUsername.getText().toString();
                    c2 = etPassword.getText().toString();

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(LoginActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            resp = Call(c1, c2);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            dialog.dismiss();

if(resp.equalsIgnoreCase("1")){
    PrefUtils.setLoggedIn(LoginActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                          Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
}else {
    Toast.makeText(getApplicationContext(), "Invalid Username & Password", Toast.LENGTH_LONG).show();
}



                            //store in shared preference

//                            PrefUtils.setLoggedIn(RegistrationActivity.this, true, etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
//                            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }.execute();
//                    //store in shared preference
//                    PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
//                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
//
//                    startActivity(intent);
//                    finish();

                }

            }
        });

        btnNewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);

                startActivity(intent);
                finish();
            }
        });

    }
    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);

        startActivity(intent);
        finish();
    }

    public String Call(String a,String b)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("name");
        pi.setValue(a);
        pi.setType(String.class);
        request.addProperty(pi);

        PropertyInfo p2=new PropertyInfo();
        p2.setName("password");
        p2.setValue(b);
        p2.setType(String.class);
        request.addProperty(p2);





        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;

        try
        {

            httpTransport.debug=true;
            httpTransport.call(SOAP_ACTION, envelope);

            response = envelope.getResponse();
        }
        catch (Exception ex)
        {
            // ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
            //displayExceptionMessage(ex.getMessage());
            //System.out.println(exception.getMessage());
        }
        return response.toString() ;
    }




}
