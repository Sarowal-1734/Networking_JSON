package com.sarowal.networkingjson;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvModel, tvPrice;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.ivImage);
        tvModel = findViewById(R.id.tvModel);
        tvPrice = findViewById(R.id.tvPrice);
        String api = "https://jsonkeeper.com/b/HXGT";
        //String api = "https://androidtutorialpoint.com/api/MobileJSONArray.json";
        new mobileTask().execute(api);  //Creating asyncTask to get data from web/api
    }

    //Creating asyncTask to get data from web/api
    public class mobileTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String s = strings[0];
            try {
                URL url = new URL(s);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();  //get byte code
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //byte code to char
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //partially display
                String line = "";
                StringBuilder stringBuilder = new StringBuilder();
                if((line=bufferedReader.readLine())!= null){
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(5);
                tvModel.setText("Model: "+jsonObject.getString("model"));
                tvPrice.setText("Price: $"+jsonObject.getString("price"));
                //Creating another asyncTask to get image
                new imageLoaderTask().execute(jsonObject.getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    //Creating another asyncTask to get image
    public class imageLoaderTask extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }
}