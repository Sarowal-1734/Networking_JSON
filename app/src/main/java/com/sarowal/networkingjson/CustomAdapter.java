package com.sarowal.networkingjson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    TextView tvModel, tvPrice;
    ImageView imageView;

    Context applicationContext;
    int list;
    List<MobileInfo> s;

    public CustomAdapter(Context applicationContext, int list, List<MobileInfo> s) {
        this.applicationContext = applicationContext;
        this.list = list;
        this.s = s;
    }

    @Override
    public int getCount() {
        return s.size();
    }

    @Override
    public Object getItem(int position) {
        return s.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list,parent,false);
        }
        tvModel = convertView.findViewById(R.id.tvModel);
        tvPrice = convertView.findViewById(R.id.tvPrice);
        imageView = convertView.findViewById(R.id.ivImage);

        tvModel.setText("Model: " + s.get(position).getModel());
        tvPrice.setText("Price: $" + s.get(position).getPrice());
        String imageurl = s.get(position).getImage();
        new imageLoaderTask().execute(imageurl); //Loading image in background thread

        return convertView;
    }
    //background Thread
    public class imageLoaderTask extends AsyncTask<String,Void,Bitmap>{
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
