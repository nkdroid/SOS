package com.nkdroid.sos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class RegistrationActivity extends ActionBarActivity {

    private EditText etUsername,etPassword,etMobile,etEmail;
    private Button btnRegistration;
    String c1,c2,c3,c4;
    private ProgressDialog dialog;
    private String resp;
    public static final String SOAP_ACTION = "http://tempuri.org/AddUser";
    public static  final String OPERATION_NAME = "AddUser";

    public static  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";


    public static  final String SOAP_ADDRESS = "http://testst1.somee.com/WebService.asmx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();
    }



    private void initView() {
        etUsername= (EditText) findViewById(R.id.etUsername);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etMobile= (EditText) findViewById(R.id.etmobile);
        etEmail= (EditText) findViewById(R.id.etemail);
        btnRegistration= (Button) findViewById(R.id.btnRegister);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etUsername)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Username", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    c1 = etUsername.getText().toString();
                    c2 = etPassword.getText().toString();
                    c3 = etEmail.getText().toString();
                    c4 = etPassword.getText().toString();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(RegistrationActivity.this);
                            dialog.setMessage("Loading...");
                            dialog.show();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            resp = Call(c1, c2, c3, c4);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            if(resp.equalsIgnoreCase("1")){
                                PrefUtils.setLoggedIn(RegistrationActivity.this, true, etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Please Enter Proper Data ", Toast.LENGTH_LONG).show();
                            }
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
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    public String Call(String a,String b,String c,String d)
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

        PropertyInfo p3=new PropertyInfo();
        p3.setName("email");
        p3.setValue(c);
        p3.setType(String.class);
        request.addProperty(p3);


        PropertyInfo p4=new PropertyInfo();
        p4.setName("mobile");
        p4.setValue(d);
        p4.setType(String.class);
        request.addProperty(p4);



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
