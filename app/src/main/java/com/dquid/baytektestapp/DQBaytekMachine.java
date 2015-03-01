package com.dquid.baytektestapp;

import android.content.Context;
import android.util.Log;

import com.dquid.bayteklib.BTMachineType;
import com.dquid.bayteklib.BTProtocol;
import com.dquid.bayteklib.BTReqInterface;
import com.dquid.bayteklib.BTRespListener;
import com.dquid.sdk.core.DQData;
import com.dquid.sdk.core.DQError;
import com.dquid.sdk.core.DQManager;
import com.dquid.sdk.core.DQManagerListener;
import com.dquid.sdk.core.DQObject;
import com.dquid.sdk.core.DQObjectListener;
import com.dquid.sdk.core.DQProperty;
import com.dquid.sdk.utils.StringUtils;

import org.apache.http.HttpResponse;

/**
 * Created by giorgioscibilia on 11/02/15.
 */
public class DQBaytekMachine extends BTProtocol implements BTRespListener, BTReqInterface, DQManagerListener, DQObjectListener, DataUploadRequestListener {
    private final String TAG = this.getClass().getSimpleName();
    private String serialToLookFor = "0000000000000087";
    private String serialInPropertyName = "srx";
    private String serialOutPropertyName = "stx";
    private DQObject dqobject = null;
    private DQBaytekMachineListenerInterface listener = null;

    public interface DQBaytekMachineListenerInterface {
        void onConnection();

        void onConnectionFailed();

        void onDisconnection();

        void onDataUpdated(String name, byte value);
    }


    public DQBaytekMachine(BTMachineType machineType, DQBaytekMachineListenerInterface listener) {
        super(machineType.getId(), null, null);
        this.listener = listener;
    }

    public boolean connect() {
        try {
            DQManager.MAIN_INSTANCE.setListener(this);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        DQManager.MAIN_INSTANCE.startDiscovering();
        return true;
    }


    public boolean disconnect() {
        if (dqobject != null && dqobject.isConnected())
            dqobject.disconnect();
        else
            return false;

        return true;
    }

    /**
     * BTRespListener Methods
     * These callback methods are called as soon as a response from the machine is received
     */

    @Override
    public void onMachineIDReceived(byte machineID) {
        Log.d(TAG, "onMachineIDReceived: " + (machineID & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "machineID", machineID, true);
        if (listener != null)
            listener.onDataUpdated("machineID", machineID);
    }

    @Override
    public void onMajGameVerReceived(byte majVer) {
        Log.d(TAG, "onMajGameVerReceived: " + (majVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "majGameVer", majVer, true);
        if (listener != null)
            listener.onDataUpdated("majVer", majVer);
    }

    @Override
    public void onMinGameVerReceived(byte minVer) {
        Log.d(TAG, "onMinGameVerReceived: " + (minVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "minGameVer", minVer, true);
        if (listener != null)
            listener.onDataUpdated("minVer", minVer);
    }

    @Override
    public void onPHSubminGameVerReceived(byte phSubminVer) {
        Log.d(TAG, "onPHSubminGameVerReceived: " + (phSubminVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "pHSubminGameVer", phSubminVer, true);
        if (listener != null)
            listener.onDataUpdated("phSubminVer", phSubminVer);
    }

    @Override
    public void ondMajAuxVerReceived(byte majAuxVer) {
        Log.d(TAG, "ondMajAuxVerReceived: " + (majAuxVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "majAuxVer", majAuxVer, true);
        if (listener != null)
            listener.onDataUpdated("majAuxVer", majAuxVer);
    }

    @Override
    public void onMinAuxVerReceived(byte minAuxVer) {
        Log.d(TAG, "onMinAuxVerReceived: " + (minAuxVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "minAuxVer", minAuxVer, true);
        if (listener != null)
            listener.onDataUpdated("minAuxVer", minAuxVer);
    }

    @Override
    public void onPHMajAuxVerReceived(byte phMajAuxVer) {
        Log.d(TAG, "onPHMajAuxVerReceived: " + (phMajAuxVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "pHMajAuxVer", phMajAuxVer, true);
        if (listener != null)
            listener.onDataUpdated("phMajAuxVer", phMajAuxVer);
    }

    @Override
    public void onPHMinAuxVerReceived(byte phMinAuxVer) {
        Log.d(TAG, "onPHMinAuxVerReceived: " + (phMinAuxVer & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "pHMinAuxVer", phMinAuxVer, true);
        if (listener != null)
            listener.onDataUpdated("phMinAuxVer", phMinAuxVer);
    }

    @Override
    public void onPlaySoundACKReceived() {
        Log.d(TAG, "onPlaySoundACKReceived");
        DataUploadRequest.uploadData(this, dqobject, "playSound", (byte) 1, true);
    }

    @Override
    public void onLastScoreLSBReceived(byte lastScoreLSB) {
        Log.d(TAG, "onLastScoreLSBReceived: " + String.format("%02x", lastScoreLSB));
        DataUploadRequest.uploadData(this, dqobject, "lastScoreLSB", lastScoreLSB, true);
        if (listener != null)
            listener.onDataUpdated("lastScoreLSB", lastScoreLSB);
    }

    @Override
    public void onLastScoreMSBReceived(byte lastScoreMSB) {
        Log.d(TAG, "onLastScoreMSBReceived: " + String.format("%02x", lastScoreMSB));
        DataUploadRequest.uploadData(this, dqobject, "lastScoreMSB", lastScoreMSB, true);
        if (listener != null)
            listener.onDataUpdated("lastScoreMSB", lastScoreMSB);
    }

    @Override
    public void onLightsACKReceived(byte lightCode) {
        Log.d(TAG, "onLightsACKReceived: " + (lightCode & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "lights", lightCode, true);
        if (listener != null)
            listener.onDataUpdated("lightCode", lightCode);
    }

    @Override
    public void onGameModeReceived(byte gameMode) {
        Log.d(TAG, "onGameModeReceived: " + (gameMode & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "gameMode", gameMode, true);
        if (listener != null)
            listener.onDataUpdated("gameMode", gameMode);
    }

    @Override
    public void onToggleInputACKReceived(byte inputBitmask) {
        Log.d(TAG, "onToggleInputACKReceived: " + String.format("%02x", inputBitmask));
        DataUploadRequest.uploadData(this, dqobject, "toggleInput", inputBitmask, true);
        if (listener != null)
            listener.onDataUpdated("inputBitmask", inputBitmask);
    }

    @Override
    public void onAddCreditsACKReceived(byte noOfCredits) {
        Log.d(TAG, "onAddCreditsACKReceived: " + (noOfCredits & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "addCredits", noOfCredits, true);
        if (listener != null)
            listener.onDataUpdated("noOfCredits", noOfCredits);
    }

    @Override
    public void onClearAllCreditsACKReceived() {
        Log.d(TAG, "onClearAllCreditsACKReceived: ");
        DataUploadRequest.uploadData(this, dqobject, "clearAllCredits", (byte) 1, true);
    }

    @Override
    public void onDispenseTicketsACKReceived(byte noOfTickets) {
        Log.d(TAG, "onDispenseTicketsACKReceived: " + (noOfTickets & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "dispenseTickets", noOfTickets, true);
        if (listener != null)
            listener.onDataUpdated("noOfTickets", noOfTickets);
    }

    @Override
    public void onPHAddTicketsACKReceived(byte phAddTicketsACK) {
        Log.d(TAG, "onPHAddTicketsACKReceived: " + (phAddTicketsACK & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "phAddTickets", phAddTicketsACK, true);
        if (listener != null)
            listener.onDataUpdated("noOfTickets", phAddTicketsACK);
    }

    @Override
    public void onNoOfPlayedGamesLSBReceived(byte noOfPlayedGamesLSB) {
        Log.d(TAG, "onNoOfPlayedGamesLSBReceived: " + String.format("%02x", noOfPlayedGamesLSB));
        DataUploadRequest.uploadData(this, dqobject, "noOfPlayedGamesLSB", noOfPlayedGamesLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfPlayedGamesLSB", noOfPlayedGamesLSB);
    }

    @Override
    public void onNoOfPlayedGamesMSBReceived(byte noOfPlayedGamesMSB) {
        Log.d(TAG, "onNoOfPlayedGamesMSBReceived: " + String.format("%02x", noOfPlayedGamesMSB));
        DataUploadRequest.uploadData(this, dqobject, "noOfPlayedGamesMSB", noOfPlayedGamesMSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfPlayedGamesMSB", noOfPlayedGamesMSB);
    }

    @Override
    public void onNoOfDispensedTicketsLSBReceived(byte noOfDispensedTicketsLSB) {
        Log.d(TAG, "onNoOfDispensedTicketsLSBReceived: " + String.format("%02x", noOfDispensedTicketsLSB));
        DataUploadRequest.uploadData(this, dqobject, "noOfDispensedTicketsLSB", noOfDispensedTicketsLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfDispensedTicketsLSB", noOfDispensedTicketsLSB);
    }

    @Override
    public void onNoOfDispensedTicketsMSBReceived(byte noOfDispensedTicketsMSB) {
        Log.d(TAG, "onNoOfDispensedTicketsMSBReceived: " + String.format("%02x", noOfDispensedTicketsMSB));
        DataUploadRequest.uploadData(this, dqobject, "noOfDispensedTicketsMSB", noOfDispensedTicketsMSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfDispensedTicketsMSB", noOfDispensedTicketsMSB);
    }

    @Override
    public void onFBCurrentDailyHighScoreReceived(byte currentDailyHighscore) {
        Log.d(TAG, "onFBCurrentDailyHighScoreReceived: " + (currentDailyHighscore & 0xFF));
        DataUploadRequest.uploadData(this, dqobject, "fBCurrentDailyHighScore", currentDailyHighscore, true);
        if (listener != null)
            listener.onDataUpdated("currentDailyHighscore", currentDailyHighscore);
    }

    @Override
    public void onPHTotalAddedTicketsLSBReceived(byte noOfAddedTicketsLSB) {
        Log.d(TAG, "onPHTotalAddedTicketsLSBReceived: " + String.format("%02x", noOfAddedTicketsLSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalAddedTicketsLSB", noOfAddedTicketsLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfAddedTicketsLSB", noOfAddedTicketsLSB);
    }

    @Override
    public void onPHTotalAddedTicketsMLSBReceived(byte noOfAddedTicketsMLSB) {
        Log.d(TAG, "onPHTotalAddedTicketsMLSBReceived: " + String.format("%02x", noOfAddedTicketsMLSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalAddedTicketsMLSB", noOfAddedTicketsMLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfAddedTicketsMLSB", noOfAddedTicketsMLSB);
    }

    @Override
    public void onPHTotalAddedTicketsMSBReceived(byte noOfAddedTicketsMSB) {
        Log.d(TAG, "onPHTotalAddedTicketsMSBReceived: " + String.format("%02x", noOfAddedTicketsMSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalAddedTicketsMSB", noOfAddedTicketsMSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfAddedTicketsMSB", noOfAddedTicketsMSB);
    }

    @Override
    public void onPHTotalRedeemedTicketsLSBReceived(byte noOfRedeemedTicketsLSB) {
        Log.d(TAG, "onPHTotalRedeemedTicketsLSBReceived: " + String.format("%02x", noOfRedeemedTicketsLSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalRedeemedTicketsLSB", noOfRedeemedTicketsLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfRedeemedTicketsLSB", noOfRedeemedTicketsLSB);
    }

    @Override
    public void onPHTotalRedeemedTicketsMLSBReceived(byte noOfRedeemedTicketsMLSB) {
        Log.d(TAG, "onPHTotalRedeemedTicketsMLSBReceived: " + String.format("%02x", noOfRedeemedTicketsMLSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalRedeemedTicketsMLSB", noOfRedeemedTicketsMLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfRedeemedTicketsMLSB", noOfRedeemedTicketsMLSB);
    }

    @Override
    public void onPHTotalRedeemedTicketsMSBReceived(byte noOfRedeemedTicketsMSB) {
        Log.d(TAG, "onPHTotalRedeemedTicketsMSBReceived: " + String.format("%02x", noOfRedeemedTicketsMSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalRedeemedTicketsMSB", noOfRedeemedTicketsMSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfRedeemedTicketsMSB", noOfRedeemedTicketsMSB);
    }

    @Override
    public void onPHTotalPrintedTicketsMSBReceived(byte noOfPrintedTicketsMSB) {
        Log.d(TAG, "onPHTotalPrintedTicketsMSBReceived: " + String.format("%02x", noOfPrintedTicketsMSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalPrintedTicketsMSB", noOfPrintedTicketsMSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfPrintedTicketsMSB", noOfPrintedTicketsMSB);
    }

    @Override
    public void onPHTotalPrintedTicketsLSBReceived(byte noOfPrintedTicketsLSB) {
        Log.d(TAG, "onPHTotalPrintedTicketsLSBReceived: " + String.format("%02x", noOfPrintedTicketsLSB));
        DataUploadRequest.uploadData(this, dqobject, "pHTotalPrintedTicketsLSB", noOfPrintedTicketsLSB, true);
        if (listener != null)
            listener.onDataUpdated("noOfPrintedTicketsLSB", noOfPrintedTicketsLSB);
    }

    @Override
    public void onPHNumberOfVendSucceededReceivedForLocation(byte location, byte noOfVendSucceeded) {
        Log.d(TAG, "onPHNumberOfVendSucceededReceivedForLocation: " + (location & 0xFF) + " - noOfVendFailed: " + (noOfVendSucceeded & 0xFF));
        byte[] data = {noOfVendSucceeded, location};
        DataUploadRequest.uploadData(this, dqobject, "pHNumberOfVendSuccForLocation", data, true);
        if (listener != null) {
            listener.onDataUpdated("location", location);
            listener.onDataUpdated("noOfVendSucceeded", noOfVendSucceeded);
        }
    }

    @Override
    public void onPHNumberOfVendFailedReceivedForLocation(byte location, byte noOfVendFailed) {
        Log.d(TAG, "onPHNumberOfVendFailedReceivedForLocation: " + (location & 0xFF) + " - noOfVendFailed: " + (noOfVendFailed & 0xFF));
        byte[] data = {noOfVendFailed, location};
        DataUploadRequest.uploadData(this, dqobject, "pHNumberOfVendFailureForLocation", data, true);
        if (listener != null) {
            listener.onDataUpdated("location", location);
            listener.onDataUpdated("noOfVendFailed", noOfVendFailed);
        }
    }

    @Override
    public void onInvalidCommandReceived(Exception e) {
        Log.d(TAG, "onInvalidCommandReceived: " + e.getLocalizedMessage());
    }

    @Override
    public void onReceiveError(Exception e) {
        Log.d(TAG, "onReceiveError: " + e.getLocalizedMessage());
    }


    /**
     * BTReqInterface Methods
     * These methods can be called to send messages to the machine
     */


    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getMachineID()
     */
    @Override
    public boolean getMachineID() {
        super.setRespListener(this);
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_MACHINE_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getMajGameVer()
     */
    @Override
    public boolean getMajGameVer() {
        super.setRespListener(this);
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_MAJ_GAME_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getMinGameVer()
     */
    @Override
    public boolean getMinGameVer() {
        super.setRespListener(this);
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_MIN_GAME_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHSubminGameVer()
     */
    @Override
    public boolean getPHSubminGameVer() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_PH_SMIN_GAME_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getdMajAuxVer()
     */
    @Override
    public boolean getdMajAuxVer() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_AUX_MAJ_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getMinAuxVer()
     */
    @Override
    public boolean getMinAuxVer() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_AUX_MIN_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHMajAuxVer()
     */
    @Override
    public boolean getPHMajAuxVer() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_PH_AUX_MAJ_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHMinAuxVer()
     */
    @Override
    public boolean getPHMinAuxVer() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_VER_AND_ID, BTProtocol.PL_GET_PH_AUX_MIN_VER_ID);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#playSound()
     */
    @Override
    public boolean playSound() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_PLAY_SOUND);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getLastScoreLSB()
     */
    @Override
    public boolean getLastScoreLSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_LAST_SCORE, BTProtocol.PL_LAST_SCORE_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getLastScoreMSB()
     */
    @Override
    public boolean getLastScoreMSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_LAST_SCORE, BTProtocol.PL_LAST_SCORE_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#setLights(byte)
     */
    @Override
    public boolean setLights(byte lightCode) {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_SET_LIGHTS, lightCode);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getGameMode()
     */
    @Override
    public boolean getGameMode() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_GAME_MODE);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#toggleInput(byte)
     */
    @Override
    public boolean toggleInput(byte inputBitmask) {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_TOGGLE_INPUT, inputBitmask);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#addCredits(byte)
     */
    @Override
    public boolean addCredits(byte noOfCredits) {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_ADD_CREDITS, noOfCredits);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#clearAllCredits()
     */
    @Override
    public boolean clearAllCredits() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_CLEAR_ALL_CREDITS);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#dispenseTickets(byte)
     */
    @Override
    public boolean dispenseTickets(byte noOfTickets) {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_DISPENSE_TICKETS, noOfTickets);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#phAddTickets(byte)
     */
    @Override
    public boolean phAddTickets(byte noOfTickets) {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_ADD_TICKETS, noOfTickets);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getNoOfPlayedGamesLSB()
     */
    @Override
    public boolean getNoOfPlayedGamesLSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_NO_OF_PLAYED_GAMES_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getNoOfPlayedGamesMSB()
     */
    @Override
    public boolean getNoOfPlayedGamesMSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_NO_OF_PLAYED_GAMES_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getNoOfDispensedTicketsLSB()
     */
    @Override
    public boolean getNoOfDispensedTicketsLSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_NO_OF_DISP_TICKETS_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getNoOfDispensedTicketsMSB()
     */
    @Override
    public boolean getNoOfDispensedTicketsMSB() {
        super.setRespListener(this);
        if (machineID == BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_NO_OF_DISP_TICKETS_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getFBCurrentDailyHighScore()
     */
    @Override
    public boolean getFBCurrentDailyHighScore() {
        super.setRespListener(this);
        if (machineID != BTProtocol.FLAPPY_BIRD) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_FB_CURR_DAILY_HIGHSCORE);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalAddedTicketsLSB()
     */
    @Override
    public boolean getPHTotalAddedTicketsLSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_ADDED_TICKETS_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalAddedTicketsMLSB()
     */
    @Override
    public boolean getPHTotalAddedTicketsMLSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_ADDED_TICKETS_MLSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalAddedTicketsMSB()
     */
    @Override
    public boolean getPHTotalAddedTicketsMSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_ADDED_TICKETS_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalRedeemedTicketsLSB()
     */
    @Override
    public boolean getPHTotalRedeemedTicketsLSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_REDEEMED_TICKETS_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalRedeemedTicketsMLSB()
     */
    @Override
    public boolean getPHTotalRedeemedTicketsMLSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_REDEEMED_TICKETS_MLSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalRedeemedTicketsMSB()
     */
    @Override
    public boolean getPHTotalRedeemedTicketsMSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_REDEEMED_TICKETS_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalPrintedTicketsMSB()
     */
    @Override
    public boolean getPHTotalPrintedTicketsMSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_PRINTED_TICKETS_MSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHTotalPrintedTicketsLSB()
     */
    @Override
    public boolean getPHTotalPrintedTicketsLSB() {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_MACHINE_STATS, BTProtocol.PL_PH_TOT_PRINTED_TICKETS_LSB);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHNumberOfVendSucceededForLocation(byte)
     */
    @Override
    public boolean getPHNumberOfVendSucceededForLocation(byte location) {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_PH_MACHINE_STATS_1, location);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }

    /* (non-Javadoc)
     * @see com.dquid.baytek.proto.BTReqInterface#getPHNumberOfVendFailureForLocation(byte)
     */
    @Override
    public boolean getPHNumberOfVendFailureForLocation(byte location) {
        super.setRespListener(this);
        if (machineID != BTProtocol.PRIZE_HUB) return false;
        byte[] packetToSend = newReqPacket(BTProtocol.ID_PH_MACHINE_STATS_2, location);
        return dqobject.write(packetToSend, serialOutPropertyName);
    }


    /**
     * DQManagerListener Methods
     * These callback methods are called as soon as the DQManager has events to notify
     */

    @Override
    public void onNewObjectDiscovered(DQObject object) {
        if (object.getObjectId().equalsIgnoreCase(serialToLookFor)) {
            // The object discovered is the one we were looking for
            // we will connect to it and set as its delegate
            // so this class will receive updates from it (DQObjectListener callbacks)
            dqobject = object;
            dqobject.setListener(this);
            dqobject.connect();
        }
    }

    @Override
    public void onObjectConnected(DQObject object) {
        /**
         * This callback will be fired every time a connection to an object succeeds.
         * You can safely ignore this here and handle the callback in the DQObjectListener callback method
         */
    }

    @Override
    public void onObjectConnectionFail(DQObject object, DQError error) {
        /**
         * This callback will be fired every time a connection to an object fails.
         * You can safely ignore this here and handle the callback in the DQObjectListener callback method
         */
    }

    @Override
    public void onObjectPropertiesUpdated(DQObject object) {
        /**
         * This callback will be fired every time a connected object.
         * You can safely ignore this here and handle the callback in the DQObjectListener callback method
         */
    }

    @Override
    public void onObjectDisconnected(DQObject object) {
        /**
         * This callback will be fired every time the sdk disconnects from an object.
         * You can safely ignore this here and handle the callback in the DQObjectListener callback method
         */
    }

    @Override
    public void onErrorOccurred(DQError error) {
        // For some reason an error occurred, do something if needed
        Log.d(TAG, "onErrorOccurred: " + error.getLocalizedMessage());
    }

    @Override
    public void onDataReceived(DQObject object, DQProperty property, DQData data) {
        /**
         * This callback will be fired every time a connected object will receive properties updates.
         * You can safely ignore this here and handle the callback in the DQObjectListener callback method
         */
    }

    @Override
    public Context provideApplicationContext() {
        // DQuid SDK needs the application context, please provide it here;
        return MainActivity.context;
    }

    @Override
    public void onDiscoveryError(DQError e) {
        /**
         * Discovery error, restart discovery (if needed) calling:
         * DQManager.MAIN_INSTANCE.startDiscovering();
         */
    }


    /**
     * DQObjectListener Methods
     * These callback methods are called as soon as the DQObject has events to notify
     */

    @Override
    public void onConnectionEstablished() {
        // Connection to object established, do something if needed
        dqobject.subscribe(serialInPropertyName);
        if (listener != null)
            listener.onConnection();

    }

    @Override
    public void onConnectionFailed(DQError error) {
        // Connection to object failed, do something if needed
        if (listener != null)
            listener.onConnectionFailed();

    }

    @Override
    public void onDisconnection() {
        // Disconnection from object, do something if needed
        if (listener != null)
            listener.onDisconnection();
    }

    @Override
    public void onPropertiesUpdated() {
        // New properties were found for this object
    }

    @Override
    public void onDataReceived(DQProperty property, DQData dataReceived) {
        Log.d(TAG, "Property " + property.getName() + " of object " + dqobject.getName() + " updated its value: " + StringUtils.bytesToHex(dataReceived.getRawValue()));

        // New data received from machine, forward it to the parser (that will call the proper callback)
        if (property.getName().equalsIgnoreCase(serialInPropertyName)) {
            byte[] bytes = dataReceived.getRawValue();
            parsePacket(bytes);
        }
    }

    @Override
    public void onResponseReceived(String url, HttpResponse response) {
        Log.d(TAG,"Data upload OK");
    }

    @Override
    public void onRequestFailed(String url, String errorMessage) {
        Log.e(TAG,"Data upload ERROR: "+errorMessage);
    }
}
