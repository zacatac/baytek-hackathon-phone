package com.dquid.baytektestapp;

import com.dquid.bayteklib.BTMachineType;
import com.dquid.baytektestapp.util.SystemUiHider;
import com.dquid.sdk.utils.DQLog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class PlayActivity extends Activity implements DQBaytekMachine.DQBaytekMachineListenerInterface{
    private static final int REQUEST_CODE_VENMO_APP_SWITCH = 2402;
    static Context context;
    private TextView gameNameField;
    private TextView gameIdField;
    private TextView creditsAddedField;
    private Button addCreditButton;
    private Button gameBrokenButton;
    private Button doneButton;
    private Button uploadCreditsButton;
    private TextView gameModeField;
    public static int gameCredits = 0;
    private DQBaytekMachine myMachine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
//
//        int mUIFlag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);

        PlayActivity.context = PlayActivity.this;
        DQLog.setLogLevel(DQLog.DQLOG_VERBOSE);
        initUI();
        connectGame();
    }

    private String getGameOver(){
        try {
            if (myMachine.getGameMode())
                return "Keep Playing";
            else
                return "Game Over";
        } catch (Exception e){
            return "";
        }
    }

    private void sendGameCredits(byte gameCredits){
        myMachine.addCredits(gameCredits);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                creditsAddedField.setText("");
                creditsAddedField.setText(PlayActivity.gameCredits+ " Credits Added");
            }
        });

    }
    private void connectGame() {
        myMachine = new DQBaytekMachine(BTMachineType.BAYTEK_MACHINE_TYPE_FLAPPY_BIRD, PlayActivity.this);
        myMachine.connect();
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
                            //Payment successful.  Use data from response object to display a success message
                            String added = response.getAmount();
                            float addedFloat = Float.parseFloat(added);
                            int addedInt = Math.round(addedFloat);
                            gameCredits += addedInt;
                            sendGameCredits((byte) addedInt);
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
                    }
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
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
    void initUI(){
        Intent intent = getIntent();
        gameNameField = (TextView) findViewById(R.id.gameNameField);
        gameIdField = (TextView) findViewById(R.id.gameIdField);
        creditsAddedField = (TextView) findViewById(R.id.creditsAddedField);
        addCreditButton = (Button) findViewById(R.id.addCreditButton);
        gameBrokenButton = (Button) findViewById(R.id.gameBrokenButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        uploadCreditsButton = (Button) findViewById(R.id.uploadCreditsButton);
        gameModeField = (TextView) findViewById(R.id.gameModeField);
        uploadCreditsButton.setEnabled(false);
        addCreditButton.setEnabled(false);
        if (!getGameOver().equals("")){
            uploadCreditsButton.setEnabled(true);
            addCreditButton.setEnabled(true);
            sendGameCredits((byte) gameCredits);
            gameModeField.setText(getGameOver());
        }

        gameCredits = Integer.parseInt(intent.getStringExtra("credits"));
        gameNameField.setText(intent.getStringExtra("name"));
        gameIdField.setText(intent.getStringExtra("id"));

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gameBrokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCredits("1");
            }
        });

        uploadCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameCredits += 2;
                sendGameCredits((byte) 2);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnection() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadCreditsButton.setEnabled(true);
                addCreditButton.setEnabled(true);
                sendGameCredits((byte) gameCredits);
                gameModeField.setText(getGameOver());
            }
        });

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public void onDisconnection() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadCreditsButton.setEnabled(false);
                addCreditButton.setEnabled(false);
            }
        });
    }

    @Override
    public void onDataUpdated(String name, byte value) {

    }
}
