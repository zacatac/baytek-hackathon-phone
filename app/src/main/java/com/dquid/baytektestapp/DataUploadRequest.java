/**
 * DataUploadRequest.java
 * 
 * @author Giorgio Scibilia <giorgioscibilia@gmail.com>
 * 
 *         Created on 08/gen/2015 
 * 
 * Copyright (c) 2014 DQuid s.r.l. All rights reserved.
 */
package com.dquid.baytektestapp;

import android.util.Log;

import com.dquid.sdk.core.DQObject;
import com.dquid.sdk.utils.DQLog;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.SSLException;

interface DataUploadRequestListener {
	void onResponseReceived(String url, HttpResponse response);
	void onRequestFailed(String url, String errorMessage);
}

/**
 * @author Giorgio Scibilia <giorgioscibilia@gmail.com>
 * 
 */
class DataUploadRequest {
	
	private static final String TAG = "DataUploadRequest";
	private HttpUriRequest request = null;
	private String urlString = null;
	private DataUploadRequestListener listener = null;

    private final static String DATA_UPLOAD_URL = "http://server-api.dquid.io/api/v1/data";
    private final static boolean ENABLE_DATA_UPLOAD = true;


    void performRequest() {
        if(listener==null) return;
		
		new Thread(new Runnable() {

			@Override
            public void run(){
				// 2015/1/15 Dario - The SSL certificate on server-api.dquid.io currently gives an hostname mismatch since it
				// is released for *.herokuapp.com. ATM If we get this mismatch error we will try again by ignoring it,
				// but this should never happen.
				HttpResponse response = null;
				//String responseString = null;
				try {
					response = defaultPerformRequest();
					if (response != null) {
				        listener.onResponseReceived(urlString, response);
					} else {
			        	listener.onRequestFailed(urlString, "Server gave an empty response");
					}
				} catch (SSLException ssle) {
					DQLog.w(TAG, "Got SSLException, re-executing the request ignoring hostname mismatch (Exception: " + ssle.getMessage() + ")");
					try {
						response = performRequesAllowingSSLHostnameMismatch();
						if (response != null) {
							listener.onResponseReceived(urlString, response);
						} else {
				        	listener.onRequestFailed(urlString, "Server gave an empty response");
						}
					} catch (ParseException | IOException nestedException) {
						DQLog.e(TAG,"Giving up! :( "+ nestedException.getMessage());
			        	listener.onRequestFailed(urlString, nestedException.getMessage());
					}
		        } catch (ClientProtocolException e) {
			        DQLog.e(TAG, "ClientProtocolException with message: " + e.getMessage());
		        	listener.onRequestFailed(urlString, e.getMessage());
			        
		        } catch (IOException e) {
			        DQLog.e(TAG, "IOException with message: " + e.getMessage());
			        listener.onRequestFailed(urlString, e.getMessage());
                } 
	            
            }}).start();
		
		return;
	}

	/**
	 * Perform the http request without any specific SSL verifier condition. 
	 * This is the standard request and can be used also with classic http. 
	 * @return the response 
	 * @throws SSLException
	 * @throws ParseException
	 * @throws IOException
	 */
	private HttpResponse defaultPerformRequest() throws SSLException, ParseException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		return httpClient.execute(request);
	}
	
	/**
	 * Perform the http request allowing SSL hostname mismatch. This is currently needed since 
	 * our server URL (server-api.dquid.io) is different from is SSL certificate hostname (*.herokuapp.com) 
	 * @return the response
	 * @throws SSLException
	 * @throws ParseException
	 * @throws IOException
	 */
	private HttpResponse performRequesAllowingSSLHostnameMismatch() throws SSLException, ParseException, IOException {
		DefaultHttpClient client = new DefaultHttpClient();
    	SchemeRegistry registry = new SchemeRegistry();
    	SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
    	socketFactory.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    	registry.register(new Scheme("https", socketFactory, 443));
    	SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
    	HttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
		return httpClient.execute(request);
	}

	/**
     * @param request
     * @param listener
     */
    DataUploadRequest(HttpUriRequest request, DataUploadRequestListener listener) {
	    super();
    	
	    this.request = request;
	    this.urlString = request.getURI().toString();
	    this.listener = listener;
	    
	    DQLog.d(TAG,"Creating DataUploadRequest for url: " + urlString);
    }

    public static void uploadData(DataUploadRequestListener l, DQObject dqobject, String propertyName, byte data, boolean isRead) {
        byte[] dataAsArray = new byte[1];
        dataAsArray[0] = data;
        uploadData(l,dqobject,propertyName,dataAsArray,isRead);
    }

    public static void uploadData(DataUploadRequestListener l, DQObject dqobject, String propertyName, byte[] data, boolean isRead) {
        if (propertyName == null || data == null || ENABLE_DATA_UPLOAD == false) return;

        try {
            String json =
                    "[{"
                            + "\"serialNumber\":\"" + dqobject.getObjectId() + "\","
                            + "\"propertyName\":\""+ propertyName +"\","
                            + "\"propertyValue\":\"" + bytesToHex(data) + "\","
                            + "\"timestamp\":\"" + System.currentTimeMillis() + "\","
                            + "\"objectName\":\"" + dqobject.getName() + "\","
                            + "\"type\":\""+(isRead?"read":"write")+"\""
                            + "}]";
            //Log.d(TAG, json);
            HttpPost httpRequest = new HttpPost(DATA_UPLOAD_URL);
            httpRequest.setHeader("Content-Type", "application/json");
            StringEntity xmlEntity = new StringEntity(json);
            httpRequest.setEntity(xmlEntity);
            new DataUploadRequest(httpRequest, l).performRequest();

        } catch (UnsupportedEncodingException uee) {
            Log.e(TAG,"uploadData(): "+uee.getLocalizedMessage());
        }

    }


    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
