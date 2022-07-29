package com.myapplication.asynctasksexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    TextView tex1, tex2;
    ProgressDialog progressDialog;
    Button DisBtn;
    ImageView ImgV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tex1=findViewById(R.id.txt1);
        tex2=findViewById(R.id.tex2);
        DisBtn=findViewById(R.id.DisBtn);
        ImgV=findViewById(R.id.ImgV);
        DisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              MyAsyncTasks myAsyncTasks=new MyAsyncTasks();
              myAsyncTasks.execute();

            }
        });

    }

    private class MyAsyncTasks extends AsyncTask<String,String,String> {
        String apiUrl = "http://mobileappdatabase.in/demo/smartnews/app_dashboard/jsonUrl/single-article.php?article-id=71";

        Context context;
        String title, image, category;
        TextView tex1, tex2;
        ImageView ImgV;

            @Override
            protected String doInBackground (String...strings){
                String current = "";
                try {
                    URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL(apiUrl);

                        urlConnection = (HttpURLConnection) url
                                .openConnection();

                        InputStream in = urlConnection.getInputStream();

                        InputStreamReader isw = new InputStreamReader(in);

                        int data = isw.read();
                        while (data != -1) {
                            current += (char) data;
                            data = isw.read();
                            System.out.print(current);

                        }
                        return current;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return current;
            }

            @Override
            protected void onPreExecute () {
                super.onPreExecute();
                 progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected void onPostExecute (String s){
                super.onPostExecute(s);
                Log.d("data", s.toString());
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject oneObject = jsonArray.getJSONObject(0);
                    title = oneObject.getString("title");
                    category = oneObject.getString("category");
                    image = oneObject.getString("image");
                    tex1.setText("Title: " + title);
                    tex2.setText("Category: " + category);
                    // Picasso library to display the image from URL
                    Picasso.get()
                            .load(image)
                            .into(ImgV);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            @Override
            protected void onProgressUpdate (String...values){
                super.onProgressUpdate(values);
            }

    }
}