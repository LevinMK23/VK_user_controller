package com.example.levinmk.vk_user_controller;

import android.util.Log;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// Класс для запроса и получения данных из профиля пользователя ВКонтакте
public class GetUserInfo {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public GetUserInfo() {
    }
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    DefaultHttpClient httpClient;
    HttpPost httpPost;

    public JSONObject getuserinfo(String address, JSONObject jsonToken) {
        // Making HTTP request
        try {
            // DefaultHttpClient
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);
            String tok = jsonToken.getString("access_token");
            String user_id = jsonToken.getString("user_id");
            //Набор запрашиваемых полей из профиля пользователя ВКонтакте
            String fields = "uid,first_name,last_name,screen_name,sex,bdate,photo_big";
            params.add(new BasicNameValuePair("uids", user_id));
            params.add(new BasicNameValuePair("fields", fields));
            params.add(new BasicNameValuePair("access_token", tok));
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            //Отправка Post запроса сервису ВКонтакте
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //Получение ответа на запрос
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent(); //Ответ сервера в виде объекта InputStream
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Преобразование ответа сервера InputStream в строку
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString(); //Информация о пользователе в JSON строке
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Строку преобразуем в JSON Object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // Return JSON объект
        return jObj;
    }
}
