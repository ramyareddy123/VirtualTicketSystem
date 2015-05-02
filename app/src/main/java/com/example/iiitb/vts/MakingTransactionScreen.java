package com.example.iiitb.vts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MakingTransactionScreen extends ActionBarActivity {

    private Button TransactionButton;
    private EditText source;
    private EditText destination;
    private EditText amount;
    SessionManager session;
    private ProgressDialog pDialog;
    String content = new String();
    String name = new String();
    JSONParser jsonParser = new JSONParser();
    private static final String TRANSACTION_URL = "http://vts.herobo.com/make_transaction.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_transaction_screen);
        session = new SessionManager(getApplicationContext());

        TransactionButton = (Button) findViewById(R.id.MakeTransactionButton);
        source = (EditText)findViewById(R.id.Source);
        destination = (EditText) findViewById(R.id.Destination);
        amount = (EditText) findViewById(R.id.Ticketcharge);



        if (savedInstanceState == null) {
            if(getIntent().getExtras()!=null)
            {
                content = getIntent().getExtras().getString("content");
                System.out.print("content   ");
                System.out.println(content);
                name = getIntent().getExtras().getString("username");
            }
            else
            {
                System.out.print(content);
                new MakeTransaction().execute();
            }
        }
        else
        {
            content = (String) savedInstanceState.getSerializable("content");
            name = (String) savedInstanceState.getSerializable("username");
        }
        name=session.pref.getString("username", null);
        String[] parts = name.split("@");
        name = parts[0];
        TransactionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new MakeTransaction().execute();
               // Toast.makeText(MakingTransactionScreen.this, "Successful Transaction", Toast.LENGTH_SHORT).show();

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_making_transaction_screen, menu);
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
        Intent logoutIntent = new Intent(MakingTransactionScreen.this,
                MainScreen.class);

        finish();
        session.logoutUser();

        startActivity(logoutIntent);
    }

    class MakeTransaction extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MakingTransactionScreen.this);
            pDialog.setMessage("Sending....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            final TextView source = (TextView)findViewById(R.id.Source);
            final TextView destination = (TextView)findViewById(R.id.Destination);
            final TextView amount = (TextView)findViewById(R.id.Ticketcharge);

            String sourcename = source.getText().toString();
            String destinationname = destination.getText().toString();
            String amountnumber = amount.getText().toString();
            try {
                System.out.print("Hereeeeeeeeee: ");
                System.out.println(content);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pbarcode", content));
                params.add(new BasicNameValuePair("amount", amountnumber));
                params.add(new BasicNameValuePair("source", sourcename));
                params.add(new BasicNameValuePair("destination", destinationname));
                params.add(new BasicNameValuePair("tname", name));
                JSONObject json = jsonParser.makeHttpRequest(
                        TRANSACTION_URL, "POST", params);
                int success = json.getInt(TAG_SUCCESS);
                System.out.print("Success is here : ");
                System.out.println(success);
                if (success == 1) {
                    Log.d("Successfully register!", json.toString());

                    Intent ii = new Intent(MakingTransactionScreen.this, TicketUseCasesScreen.class);
                    finish();
                    // this finish() method is used to tell android os that we are done with current //activity now! Moving to other activity
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }
                else if(success == 2)
                {
                    Intent ii = new Intent(MakingTransactionScreen.this, TicketUseCasesScreen.class);
                    finish();
                    ii.putExtra("content",content);
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }
                else if(success == 3)
                {
                    Intent ii = new Intent(MakingTransactionScreen.this, TicketUseCasesScreen.class);
                    finish();
                    startActivity(ii);
                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String message) {

            pDialog.dismiss();
            if (message != null){
                Toast.makeText(MakingTransactionScreen.this, message, Toast.LENGTH_LONG).show();

            }
        }
    }

}
