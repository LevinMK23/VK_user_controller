package com.example.levinmk.vk_user_controller;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


public class GetAccessToken {

    static InputStream isT = null;
    static JSONObject jObjT = null;
    static String jsonT = "";

    public GetAccessToken() {
    }

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    Map<String, String> mapn;
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public JSONObject gettoken(String address, String token, String client_id,
                               String client_secret, String redirect_uri) {
        // Making HTTP request
        try {
            // DefaultHttpClient
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);
            params.add(new BasicNameValuePair("code", token));
            params.add(new  BasicNameValuePair("client_id", client_id));
            params.add(new  BasicNameValuePair("client_secret", client_secret));
            params.add(new  BasicNameValuePair("redirect_uri", redirect_uri));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            isT = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(isT, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            isT.close();
            jsonT = sb.toString();
            Log.e("JSONStr", jsonT);
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Parse the String to a JSON Object
        try {
            jObjT = new JSONObject(jsonT);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // Return JSON String
        return jObjT;
    }
}
