 package com.nkdroid.sos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

 public class GooglemapActivity extends FragmentActivity {


     private ListView postListView;
     private ArrayList<User> postArrayList;
     private PostAdapter postAdapter;
     private ProgressDialog dialog;

     Object response;
     public final String SOAP_ACTION = "http://tempuri.org/getpolice";
     public  final String OPERATION_NAME = "getpolice";
     public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
     public  final String SOAP_ADDRESS = "http://testst1.somee.com/WebService.asmx";

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);
        // Get a handle to the Map Fragment

         new AsyncTask<Void,Void,Void>(){
             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 dialog=new ProgressDialog(GooglemapActivity.this);
                 dialog.setMessage("Loading...");
                 dialog.show();
             }

             @Override
             protected Void doInBackground(Void... params) {

                 SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
                 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                 envelope.dotNet = true;
                 envelope.setOutputSoapObject(request);
                 HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
                 httpTransport.debug=true;
                 try {
                     httpTransport.call(SOAP_ACTION, envelope);
                 } catch (IOException e) {
                     e.printStackTrace();
                 } catch (XmlPullParserException e) {
                     e.printStackTrace();
                 }
                 try {
                     response=  envelope.getResponse();
                     Log.e("response: ", response.toString() + "");
                 } catch (SoapFault soapFault) {
                     soapFault.printStackTrace();
                 }

                 return null;
             }

             @Override
             protected void onPostExecute(Void aVoid) {
                 super.onPostExecute(aVoid);
                 dialog.dismiss();
                 StringTokenizer tokens = new StringTokenizer(response.toString(), "=");
                 GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//                 LatLng india = new LatLng(22.3000, 73.2003);
                 map.setMyLocationEnabled(true);
				List<String> mylist=new ArrayList<String>();
				for(int i=0;tokens.hasMoreTokens();i++){
					StringTokenizer tokens1 = new StringTokenizer(tokens.nextToken(), ";");

					mylist.add(tokens1.nextToken());
				}
                 mylist.remove(0);
                 Log.e("mylist",mylist+"");
				int partitionSize =5;
				List<List<String>> partitions = new LinkedList<List<String>>();
				for (int i = 0; i < mylist.size(); i += partitionSize) {
				    partitions.add(mylist.subList(i,
				            i + Math.min(partitionSize, mylist.size() - i)));
				}


                 postArrayList=new ArrayList<User>();
                 for(int k=0;k<partitions.size();k++){
                     Log.e("name", partitions.get(k).get(0) + "");
                     postArrayList.add(new User(partitions.get(k).get(0), partitions.get(k).get(1), partitions.get(k).get(2), partitions.get(k).get(3), partitions.get(k).get(4)));
                     map.addMarker(new MarkerOptions()
                             .title(partitions.get(k).get(0))
                             .snippet(partitions.get(k).get(3))
                             .position(new LatLng(Double.parseDouble(partitions.get(k).get(1)),Double.parseDouble(partitions.get(k).get(2)))));
                 }

                 postListView=(ListView)findViewById(R.id.lstvw);
                 postAdapter=new PostAdapter(GooglemapActivity.this,postArrayList);
                 postListView.setAdapter(postAdapter);


//                 map.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 13));



             }

         }.execute();


     }

     public class PostAdapter extends BaseAdapter {

         Context context;
         ArrayList<User> postArrayList;


         public PostAdapter(Context context, ArrayList<User> postArrayList) {

             this.context = context;
             this.postArrayList = postArrayList;
         }

         public int getCount() {
             return postArrayList.size();
         }

         public Object getItem(int position) {
             return postArrayList.get(position);
         }

         public long getItemId(int position) {
             return position;
         }

         class ViewHolder {
             TextView txtPostTitle,txtPostDate,txtPostDescription;
//            ImageView imgPost;
         }

         public View getView(final int position, View convertView,ViewGroup parent) {

             final ViewHolder holder;
             LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
             if (convertView == null) {
                 convertView = mInflater.inflate(R.layout.item, parent, false);
                 holder = new ViewHolder();
                 holder.txtPostTitle = (TextView) convertView.findViewById(R.id.title);
                 holder.txtPostDate = (TextView) convertView.findViewById(R.id.date);
                 holder.txtPostDescription = (TextView) convertView.findViewById(R.id.desc);
                 // holder.imgPost = (ImageView) convertView.findViewById(R.id.imgPost);
                 convertView.setTag(holder);
             } else {
                 holder = (ViewHolder) convertView.getTag();
             }
             holder.txtPostTitle.setText(postArrayList.get(position).policename);
//            holder.txtPostDate.setText(postArrayList.get(position).getPost_date());
            holder.txtPostDescription.setText(postArrayList.get(position).address);
             holder.txtPostDate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent callIntent = new Intent(Intent.ACTION_CALL);
                     callIntent.setData(Uri.parse("tel:" +postArrayList.get(position).mobile));//change the number
                     startActivity(callIntent);
                 }
             });


             return convertView;
         }

     }

 }