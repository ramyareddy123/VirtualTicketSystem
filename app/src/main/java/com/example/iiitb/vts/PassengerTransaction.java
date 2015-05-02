package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PassengerTransaction extends ActionBarActivity {

    String source1 = new String();
    String dest1 = new String();
    int amount1 = 0;
    String date1 = new String();

    String source2 = new String();
    String dest2 = new String();
    int amount2 = 0;
    String date2 = new String();

    String source3 = new String();
    String dest3 = new String();
    int amount3 = 0;
    String date3 = new String();



    SessionManager session;
    private ProgressDialog pDialog;
    public static final String KEY_USERNAME = "username";
    JSONParser jsonParser = new JSONParser();
    private static final String TXNDETAILS_URL = "http://vts.herobo.com/transaction_details.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String content = new String();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_transaction);
        session = new SessionManager(getApplicationContext());

        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                content = getIntent().getExtras().getString("content");
                System.out.print("IN TRANSACTION SCREEN : ");
                System.out.println(content);

            }
            else
            {
                new Transaction().execute();
            }
        }
        else
        {
            content = (String) savedInstanceState.getSerializable("content");
        }

    System.out.print("MY CONTENT : ");
    System.out.println(content);
    content=session.pref.getString("barcode", null);
    System.out.print("AFTER SESSION : ");
    System.out.println(content);
    new Transaction().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_passenger_transaction, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        Intent logoutIntent = new Intent(PassengerTransaction.this,
                MainScreen.class);

        finish();
        session.logoutUser();

        startActivity(logoutIntent);
    }

    class Transaction extends AsyncTask<String,JSONObject,JSONObject> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PassengerTransaction.this);
            pDialog.setMessage("loading....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            int success;
            JSONObject json;
           // String uname = session.pref.getString(KEY_USERNAME, null);
           // System.out.println(uname);
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                System.out.println(content);
                params.add(new BasicNameValuePair("pbarcode", content));
                json = jsonParser.makeHttpRequest(
                        TXNDETAILS_URL, "POST", params);
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {
                    return json;
                }
                else
                {
                    return json;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            try {

                 int success = json.getInt(TAG_SUCCESS);
                 if( success == 1)
                 {
                    int count = json.getInt("count");
                    System.out.println("COUNT : ");
                    System.out.println(count);
                    int i=1;
                    while(i<=count) {
                        //System.out.println("source"+Integer.toString(i));
                        source1 = json.getString("source" + Integer.toString(i));
                        dest1 = json.getString("dest" + Integer.toString(i));
                        amount1 = json.getInt("amount" + Integer.toString(i));
                        date1 = json.getString("date" + Integer.toString(i));


                        if (i == 1) {
                            final TextView S1 = (TextView) findViewById(R.id.Source1);
                            S1.setText(source1);

                            final TextView D1 = (TextView) findViewById(R.id.Dest1);
                            D1.setText(dest1);

                            final TextView A1 = (TextView) findViewById(R.id.Amount1);
                            String str_amount1 = Integer.toString(amount1);
                            A1.setText(str_amount1);

                            final TextView Dat1 = (TextView) findViewById(R.id.date1);
                            Dat1.setText(date1);

                        }

                        if (i==2) {
                            final TextView S2 = (TextView) findViewById(R.id.Source2);
                            S2.setText(source1);

                            final TextView D2 = (TextView) findViewById(R.id.Dest2);
                            D2.setText(dest1);

                            final TextView A2 = (TextView) findViewById(R.id.Amount2);
                            String str_amount2 = Integer.toString(amount1);
                            A2.setText(str_amount2);

                            final TextView Dat2 = (TextView) findViewById(R.id.date2);
                            Dat2.setText(date1);
                        }
                        if (i==3) {
                            final TextView S3 = (TextView) findViewById(R.id.Source3);

                            S3.setText(source1);

                            final TextView D3 = (TextView) findViewById(R.id.Dest3);
                            D3.setText(dest1);

                            final TextView A3 = (TextView) findViewById(R.id.Amount3);
                            String str_amount3 = Integer.toString(amount1);
                            A3.setText(str_amount3);

                            final TextView Dat3 = (TextView) findViewById(R.id.date3);
                            Dat3.setText(date1);
                        }
                        i = i + 1;
                    }
                }
                }catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
            /*if (message != null){
                Toast.makeText(PassengerTransaction.this, message, Toast.LENGTH_LONG).show();
            }*/
        }
    }

}
