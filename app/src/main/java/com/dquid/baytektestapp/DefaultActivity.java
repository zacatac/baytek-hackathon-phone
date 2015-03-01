package com.dquid.baytektestapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dquid.bayteklib.BTMachineType;
import com.dquid.sdk.core.DQManager;
import com.dquid.sdk.utils.DQLog;

import java.util.HashMap;


public class DefaultActivity extends ActionBarActivity implements DQBaytekMachine.DQBaytekMachineListenerInterface{
    private static final int REQUEST_CODE_VENMO_APP_SWITCH = 2402;
    static Context context;
    private DQBaytekMachine myMachine = null;
    private RadioButton flappyRadio;
    private RadioButton pianoRadio;
    private RadioButton prizeRadio;
    private ToggleButton connectedToggle;
    private Button sendRequestButton;
    private EditText parameterEditText;
    private Spinner spinner;
    private TextView responseText;
    private Button venmoButton;


    private HashMap<String, Integer> allRequests = new HashMap<String, Integer>();
    private HashMap<String, Integer> fbRequests = new HashMap<String, Integer>();
    private HashMap<String, Integer> ptRequests = new HashMap<String, Integer>();
    private HashMap<String, Integer> phRequests = new HashMap<String, Integer>();
    {
        allRequests.put("get machineID", 0);
        allRequests.put("get majorVersion", 1 );
        allRequests.put("get minorVersion", 2 );
        allRequests.put("get prizeHubSubminVersion", 3 );
        allRequests.put("get majorAuxVersion", 4 );
        allRequests.put("get minorAuxVersion", 5 );
        allRequests.put("get prizeHubMajorAuxVersion", 6 );
        allRequests.put("get prizeHubMinorAuxVersion", 7 );
        allRequests.put("get lastScoreLSB", 8 );
        allRequests.put("get lastScoreMSB", 9 );
        allRequests.put("set lightCode", 10 );
        allRequests.put("get gameMode", 11 );
        allRequests.put("set toggleInput", 12 );
        allRequests.put("add credits", 13 );
        allRequests.put("clear credits", 14 );
        allRequests.put("dispense Tickets", 15 );
        allRequests.put("ph add tickets", 16 );
        allRequests.put("get numberOfPlayedGamesLSB", 17 );
        allRequests.put("get numberOfPlayedGamesMSB", 18 );
        allRequests.put("get numberOfDispensedTicketsLSB", 19 );
        allRequests.put("get numberOfDispensedTicketsMSB", 20 );
        allRequests.put("get flappyBirdCurrentDailyHighscore", 21 );
        allRequests.put("get prizeHubNumberOfAddedTicketsLSB", 22 );
        allRequests.put("get prizeHubNumberOfAddedTicketsMLSB", 23 );
        allRequests.put("get prizeHubNumberOfAddedTicketsMSB", 24 );
        allRequests.put("get prizeHubNumberOfRedeemedTicketsLSB", 25 );
        allRequests.put("get prizeHubNumberOfRedeemedTicketsMLSB", 26 );
        allRequests.put("get prizeHubNumberOfRedeemedTicketsMSB", 27 );
        allRequests.put("get prizeHubNumberOfPrintedTicketsMSB", 28 );
        allRequests.put("get prizeHubNumberOfPrintedTicketsLSB", 29 );
        allRequests.put("get prizeHubNumberOfVendSucceeded", 30 );
        allRequests.put("get prizeHubNumberOfVendFailed", 31 );
        allRequests.put("play sound", 32 );
        
        
        fbRequests.put("get machineID", 0);
        fbRequests.put("get majorVersion", 1 );
        fbRequests.put("get minorVersion", 2 );
        fbRequests.put("get majorAuxVersion", 4 );
        fbRequests.put("get minorAuxVersion", 5 );
        fbRequests.put("get lastScoreLSB", 8 );
        fbRequests.put("get lastScoreMSB", 9 );
        fbRequests.put("set lightCode", 10 );
        fbRequests.put("get gameMode", 11 );
        fbRequests.put("set toggleInput", 12 );
        fbRequests.put("add credits", 13 );
        fbRequests.put("clear credits", 14 );
        fbRequests.put("dispense Tickets", 15 );
        fbRequests.put("get numberOfPlayedGamesLSB", 17 );
        fbRequests.put("get numberOfPlayedGamesMSB", 18 );
        fbRequests.put("get numberOfDispensedTicketsLSB", 19 );
        fbRequests.put("get numberOfDispensedTicketsMSB", 20 );
        fbRequests.put("get flappyBirdCurrentDailyHighscore", 21 );
        fbRequests.put("play sound", 32 );
        
        
        
        ptRequests.put("get machineID", 0);
        ptRequests.put("get majorVersion", 1 );
        ptRequests.put("get minorVersion", 2 );
        ptRequests.put("get majorAuxVersion", 4 );
        ptRequests.put("get minorAuxVersion", 5 );
        ptRequests.put("get lastScoreLSB", 8 );
        ptRequests.put("get lastScoreMSB", 9 );
        ptRequests.put("set lightCode", 10 );
        ptRequests.put("get gameMode", 11 );
        ptRequests.put("set toggleInput", 12 );
        ptRequests.put("add credits", 13 );
        ptRequests.put("clear credits", 14 );
        ptRequests.put("dispense Tickets", 15 );
        ptRequests.put("get numberOfPlayedGamesLSB", 17 );
        ptRequests.put("get numberOfPlayedGamesMSB", 18 );
        ptRequests.put("get numberOfDispensedTicketsLSB", 19 );
        ptRequests.put("get numberOfDispensedTicketsMSB", 20 );
        ptRequests.put("play sound", 32 );
        
        
        
        phRequests.put("get machineID", 0);
        phRequests.put("get majorVersion", 1 );
        phRequests.put("get minorVersion", 2 );
        phRequests.put("get prizeHubSubminVersion", 3 );
        phRequests.put("get prizeHubMajorAuxVersion", 6 );
        phRequests.put("get prizeHubMinorAuxVersion", 7 );
        phRequests.put("ph add tickets", 16 );
        phRequests.put("get prizeHubNumberOfAddedTicketsLSB", 22 );
        phRequests.put("get prizeHubNumberOfAddedTicketsMLSB", 23 );
        phRequests.put("get prizeHubNumberOfAddedTicketsMSB", 24 );
        phRequests.put("get prizeHubNumberOfRedeemedTicketsLSB", 25 );
        phRequests.put("get prizeHubNumberOfRedeemedTicketsMLSB", 26 );
        phRequests.put("get prizeHubNumberOfRedeemedTicketsMSB", 27 );
        phRequests.put("get prizeHubNumberOfPrintedTicketsMSB", 28 );
        phRequests.put("get prizeHubNumberOfPrintedTicketsLSB", 29 );
        phRequests.put("get prizeHubNumberOfVendSucceeded", 30 );
        phRequests.put("get prizeHubNumberOfVendFailed", 31 );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        DefaultActivity.context = DefaultActivity.this;
        initUI();
        DQLog.setLogLevel(DQLog.DQLOG_VERBOSE);
    }


    @Override
    public void onConnection() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectedToggle.setChecked(true);
                sendRequestButton.setEnabled(true);
                parameterEditText.setEnabled(false);
                spinner.setEnabled(true);
            }
        });

    }

    @Override
    public void onConnectionFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectedToggle.setChecked(false);
                sendRequestButton.setEnabled(false);
                parameterEditText.setEnabled(false);
                spinner.setEnabled(false);
            }
        });

    }

    @Override
    public void onDisconnection() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectedToggle.setChecked(false);
                sendRequestButton.setEnabled(false);
                parameterEditText.setEnabled(false);
                spinner.setEnabled(false);
            }
        });

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
                            //Payment successful.  Use data from response object to display a success message
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, response.getNote() + " : " + response.getAmount(), duration);
                            toast.show();
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");

                        //An error ocurred.  Make sure to display the error_message to the user
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, error_message, duration);
                        toast.show();
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Transaction Cancelled", duration);
                    toast.show();
                }
                break;
            }
        }
    }

    void initUI(){
        flappyRadio = (RadioButton) findViewById(R.id.flappyBirdRadioButton);
        pianoRadio = (RadioButton) findViewById(R.id.pianoTilesRadioButton);
        prizeRadio = (RadioButton) findViewById(R.id.prizeHubRadioButton);
        connectedToggle = (ToggleButton) findViewById(R.id.connectedToggle);
        sendRequestButton = (Button) findViewById(R.id.sendRequestButton);
        parameterEditText = (EditText) findViewById(R.id.parameterEditText);
        spinner = (Spinner)  findViewById(R.id.spinner);
        responseText = (TextView) findViewById(R.id.responseText);
        venmoButton = (Button) findViewById(R.id.venmo);


        // Toggle Setup
        connectedToggle.setTextOn("Disconnect");
        connectedToggle.setTextOff("Connect");
        connectedToggle.setChecked(false);

        // Disabling everything but the Radio Buttons
        flappyRadio.setEnabled(true);
        pianoRadio.setEnabled(true);
        prizeRadio.setEnabled(true);
        connectedToggle.setEnabled(false);
        sendRequestButton.setEnabled(false);
        parameterEditText.setEnabled(false);
        spinner.setEnabled(false);

        flappyRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Init the DQBaytekMachine object
                myMachine = new DQBaytekMachine(BTMachineType.BAYTEK_MACHINE_TYPE_FLAPPY_BIRD, DefaultActivity.this);

                // Disable radio buttons and enable connect toggle
                pianoRadio.setChecked(false);
                pianoRadio.setEnabled(false);
                prizeRadio.setChecked(false);
                prizeRadio.setEnabled(false);
                connectedToggle.setEnabled(true);
                initSpinner(fbRequests);
            }
        });

        venmoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent venmoIntent = VenmoLibrary.openVenmoPayment("2402", "Kick Ass", "nickwissman@berkeley.edu", "0.01", "Credits Purchase", "pay");
                startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
            }
        });


        pianoRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Init the DQBaytekMachine object
                myMachine = new DQBaytekMachine(BTMachineType.BAYTEK_MACHINE_TYPE_PIANO_TILES, DefaultActivity.this);

                // Disable radio buttons and enable connect toggle
                flappyRadio.setChecked(false);
                flappyRadio.setEnabled(false);
                prizeRadio.setChecked(false);
                prizeRadio.setEnabled(false);
                connectedToggle.setEnabled(true);
                initSpinner(ptRequests);
            }
        });

        prizeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Init the DQBaytekMachine object
                myMachine = new DQBaytekMachine(BTMachineType.BAYTEK_MACHINE_TYPE_PRIZE_HUB, DefaultActivity.this);

                // Disable radio buttons and enable connect toggle
                flappyRadio.setChecked(false);
                flappyRadio.setEnabled(false);
                pianoRadio.setChecked(false);
                pianoRadio.setEnabled(false);
                connectedToggle.setEnabled(true);
                initSpinner(phRequests);
            }
        });

        connectedToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectedToggle.setChecked(!connectedToggle.isChecked());
                if (myMachine == null)
                    return;

                if (connectedToggle.isChecked())
                    myMachine.disconnect();
                else
                    myMachine.connect();
            }
        });


        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int key = allRequests.get(spinner.getSelectedItem().toString());
                switch (key){
                    case 0:{
                        myMachine.getMachineID();
                        break;
                    }

                    case 1:{
                        myMachine.getMajGameVer();
                        break;
                    }

                    case 2:{
                        myMachine.getMinGameVer();
                        break;
                    }

                    case 3:{
                        myMachine.getPHSubminGameVer();
                        break;
                    }

                    case 4:{
                        myMachine.getdMajAuxVer();
                        break;
                    }

                    case 5:{
                        myMachine.getMinAuxVer();
                        break;
                    }

                    case 6:{
                        myMachine.getPHMajAuxVer();
                        break;
                    }

                    case 7:{
                        myMachine.getPHMinAuxVer();
                        break;
                    }

                    case 8:{
                        myMachine.getLastScoreLSB();
                        break;
                    }

                    case 9:{
                        myMachine.getLastScoreMSB();
                        break;
                    }

                    case 10:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.setLights(paramByte);
                        break;
                    }

                    case 11:{
                        myMachine.getGameMode();
                        break;
                    }

                    case 12:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.toggleInput(paramByte);
                        break;
                    }

                    case 13:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.addCredits(paramByte);
                        break;
                    }

                    case 14:{
                        myMachine.clearAllCredits();
                        break;
                    }

                    case 15:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.dispenseTickets(paramByte);
                        break;
                    }

                    case 16:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.phAddTickets(paramByte);
                        break;
                    }

                    case 17:{
                        myMachine.getNoOfPlayedGamesLSB();
                        break;
                    }

                    case 18:{
                        myMachine.getNoOfPlayedGamesMSB();
                        break;
                    }

                    case 19:{
                        myMachine.getNoOfDispensedTicketsLSB();
                        break;
                    }

                    case 20:{
                        myMachine.getNoOfDispensedTicketsMSB();
                        break;
                    }

                    case 21:{
                        myMachine.getFBCurrentDailyHighScore();
                        break;
                    }

                    case 22:{
                        myMachine.getPHTotalAddedTicketsLSB();
                        break;
                    }

                    case 23:{
                        myMachine.getPHTotalAddedTicketsMLSB();
                        break;
                    }

                    case 24:{
                        myMachine.getPHTotalAddedTicketsMSB();
                        break;
                    }

                    case 25:{
                        myMachine.getPHTotalRedeemedTicketsLSB();
                        break;
                    }

                    case 26:{
                        myMachine.getPHTotalRedeemedTicketsMLSB();
                        break;
                    }

                    case 27:{
                        myMachine.getPHTotalAddedTicketsMSB();
                        break;
                    }

                    case 28:{
                        myMachine.getPHTotalPrintedTicketsMSB();
                        break;
                    }

                    case 29:{
                        myMachine.getPHTotalPrintedTicketsLSB();
                        break;
                    }

                    case 30:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.getPHNumberOfVendSucceededForLocation(paramByte);
                        break;
                    }

                    case 31:{
                        String param = parameterEditText.getText().toString();
                        byte paramByte;

                        if(param == null || param.equals(""))
                            break;
                        try{
                            paramByte = (byte) Integer.parseInt(param);
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            break;
                        }
                        myMachine.getPHNumberOfVendFailureForLocation(paramByte);
                        break;
                    }

                    case 32:{
                        myMachine.playSound();
                        break;
                    }
                }
            }

        });

    }

    @Override
    public void onDataUpdated(final String name, final byte value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText("Received updated value for: '" + name + "': " + String.format("%02X", value));
            }
        });

    }



    
    private void initSpinner(final HashMap<String, Integer> hashmap){
    	// Spinner Setup
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, hashmap.keySet().toArray(new String[0]));
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                int pos = hashmap.get(spinner.getSelectedItem().toString());
                if(pos == 10 || pos == 12 || pos == 13 || pos == 15 || pos == 16 || pos == 30 || pos == 31)
                    parameterEditText.setEnabled(true);
                else
                    parameterEditText.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setSelection(0);
    }


}