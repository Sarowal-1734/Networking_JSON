package com.sarowal.networkingjson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmerContainer;

    RecyclerView recyclerView;
    ArrayList<MobileModel> mobileInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        shimmerContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        // Personal API
        String api = "https://jsonkeeper.com/b/CF6J";

        new mobileTask().execute(api);  //Creating asyncTask to get data from web/api
    }


    //Creating asyncTask to get data from web/api
    public class mobileTask extends AsyncTask<String, Void, ArrayList<MobileModel>> {
        @Override
        protected ArrayList<MobileModel> doInBackground(String... strings) {
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
                if ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String JSONFile = stringBuilder.toString(); //stores all data to JSONFile
                JSONArray jsonArray = new JSONArray(JSONFile);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    MobileModel mobileInfo = new MobileModel();
                    mobileInfo.setModel(jsonObject.getString("model"));
                    mobileInfo.setPrice(jsonObject.getString("price")); //use int as String other app crashes!

                    String imageUrlString = jsonObject.getString("image");

                    // Get image from String Url to Bitmap
                    URL imageUrl = new URL(imageUrlString);
                    HttpURLConnection httpImageURLConnection = (HttpURLConnection) imageUrl.openConnection();
                    httpImageURLConnection.connect();
                    InputStream inputImageStream = httpImageURLConnection.getInputStream();
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputImageStream);
                    mobileInfo.setImage(imageBitmap);

                    mobileInfoList.add(mobileInfo);
                }
                return mobileInfoList; //return list of data to onPostExecute
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MobileModel> s) {
            super.onPostExecute(s);

            // Shimmer effect closing
            shimmerContainer.stopShimmer();
            shimmerContainer.setVisibility(View.GONE);
            // Make visible the recyclerView
            recyclerView.setVisibility(View.VISIBLE);

            // Setup recyclerView
            MobileAdapter adapter = new MobileAdapter(MainActivity.this, mobileInfoList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}