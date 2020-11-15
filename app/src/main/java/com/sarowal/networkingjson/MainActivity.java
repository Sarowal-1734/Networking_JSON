package com.sarowal.networkingjson;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_item);
        String api = "https://jsonkeeper.com/b/CF6J";
        new mobileTask().execute(api);  //Creating asyncTask to get data from web/api
    }

    //Creating asyncTask to get data from web/api
    public class mobileTask extends AsyncTask<String,Void,List<MobileInfo>>{
        @Override
        protected List<MobileInfo> doInBackground(String... strings) {
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
                String JSONFile =  stringBuilder.toString(); //stores all data to JSONFile
                JSONArray jsonArray = new JSONArray(JSONFile);

                List<MobileInfo> mobileInfoList = new ArrayList<>();

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MobileInfo mobileInfo = new MobileInfo();
                    mobileInfo.setModel(jsonObject.getString("model"));
                    mobileInfo.setPrice(jsonObject.getString("price")); //use int as String other app crashes!
                    mobileInfo.setImage(jsonObject.getString("image"));
                    mobileInfoList.add(mobileInfo);
                }
                return mobileInfoList; //return list of data to onPostExecute
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<MobileInfo> s) {
            super.onPostExecute(s);
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),R.layout.list,s);
            listView.setAdapter(customAdapter);
        }
    }
}