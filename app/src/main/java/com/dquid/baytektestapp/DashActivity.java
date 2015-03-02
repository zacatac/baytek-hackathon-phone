package com.dquid.baytektestapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;

import java.util.HashMap;


public class DashActivity extends Activity {
    private static final int REQUEST_CODE_VENMO_APP_SWITCH = 2402;
    static Context context;
    private BootstrapButton addCreditsButton;
    private BootstrapButton playButton;
    private TextView creditsAmt;
    private TextView userText;
    private BootstrapEditText  creditsAddField;
    private BootstrapButton fiveCreditButton;
    private BootstrapButton tenCreditButton;
    private BootstrapEditText creditsPlayField;
    private BootstrapEditText gameIdField;
    private int numCredits = 0;
    private HashMap<Integer, String> gameIdMap = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        gameIdMap.put(100,"Flappy Bird");
        initUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case REQUEST_CODE_VENMO_APP_SWITCH: {
                if(resultCode == RESULT_OK) {
                    String signedrequest = data.getStringExtra("signedrequest");
                    if(signedrequest != null) {
                        VenmoLibrary.VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, getString(R.string.venmo_app_secret));
                        if(response.getSuccess().equals("1")) {
                            // TODO Take into account float payment amounts
                            String added = response.getAmount();
                            float addedFloat = Float.parseFloat(added);
                            int addedInt = Math.round(addedFloat);
                            numCredits += addedInt;
                            creditsAmt.setText(Integer.toString(numCredits));
//                            numCredits += getIntent().getExtras().getInt("creditsAdded");
                            //Payment successful.  Use data from response object to display a success message
//                            int duration = Toast.LENGTH_SHORT;
//
//                            Toast toast = Toast.makeText(context, response.getNote() + " : " + response.getAmount(), duration);
//                            toast.show();
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
//                        int duration = Toast.LENGTH_SHORT;
//
//                        Toast toast = Toast.makeText(context, error_message, duration);
//                        toast.show();
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
//                    int duration = Toast.LENGTH_SHORT;
//
//                    Toast toast = Toast.makeText(context, "Transaction Cancelled", duration);
//                    toast.show();
                }
                break;
            }
        }
    }

    public void addCredits(String credits){
        int creditsInt = Integer.parseInt(credits);
        credits = Integer.toString(creditsInt);
        Intent venmoIntent = VenmoLibrary.openVenmoPayment("2402", "Kick Ass", "nickwissman@berkeley.edu", credits, "Credits Purchase", "pay");
        startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
    }

    public void playGame(int gameId, int creditsUsed){
        Intent playIntent = new Intent(this, PlayActivity.class);
        String gameName = gameIdMap.get(gameId);
        String creditsUsedString = Integer.toString(creditsUsed);
        playIntent.putExtra("name", gameName);
        playIntent.putExtra("id", Integer.toString(gameId));
        playIntent.putExtra("credits", creditsUsedString);
        startActivity(playIntent);
    }

    void initUI(){
        addCreditsButton = (BootstrapButton) findViewById(R.id.addCreditsButton);
        playButton = (BootstrapButton) findViewById(R.id.playButton);
        creditsAmt = (TextView) findViewById(R.id.creditsViewAmt);
        userText = (TextView) findViewById(R.id.userText);
        creditsAddField = (BootstrapEditText) findViewById(R.id.creditsAddField);
        creditsPlayField = (BootstrapEditText) findViewById(R.id.creditsPlayField);
        gameIdField = (BootstrapEditText) findViewById(R.id.gameIdField);
        fiveCreditButton = (BootstrapButton) findViewById(R.id.fiveCreditButton);
        tenCreditButton = (BootstrapButton) findViewById(R.id.tenCreditButton);

        userText.setText("field.zackery@gmail.com");

        creditsAmt.setText(Integer.toString(numCredits));
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCredits(creditsAddField.getText().toString());
            }
        });

        fiveCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCredits("5");
            }
        });

        tenCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCredits("10");
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gameId = Integer.parseInt(gameIdField.getText().toString());
                int creditsUsed = Integer.parseInt(creditsPlayField.getText().toString());
                playGame(gameId, creditsUsed);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
