package com.firozanwar.sample.multithreading1;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText etURL;
    private ListView listview;
    private LinearLayout layoutProgress;
    private String[] listOfImages;
    private static final String TAG = "DownloadImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etURL = (EditText) findViewById(R.id.et_url);
        listview = (ListView) findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listOfImages = getResources().getStringArray(R.array.imageurls);
        layoutProgress = (LinearLayout) findViewById(R.id.layout_progress);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        etURL.setText(listOfImages[i]);
    }

    public void downloadImage(View view) {

        String url = etURL.getText().toString();
        Thread downloadImageThread = new Thread(new MyThread(url));
        downloadImageThread.start();
    }

    public boolean downloadImageUsingThread(String url) {
        boolean isSuccessful = false;
        URL downloadurl = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            downloadurl = new URL(url);
            connection = (HttpURLConnection) downloadurl.openConnection();
            inputStream = connection.getInputStream();
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                    + "/" + Uri.parse(url).getLastPathSegment());
            Log.i(TAG, "File path :: " + file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);

            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                //Log.i(TAG, read + "");
                fileOutputStream.write(buffer, 0, read);
            }
            isSuccessful = true;

        } catch (MalformedURLException e) {
            Log.i(TAG, "MalformedURLException:: " + e);
        } catch (IOException e) {
            Log.i(TAG, "IOException :: " + e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.i(TAG, "" + e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.i(TAG, "" + e);
                }
            }

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layoutProgress.setVisibility(View.GONE);
                }
            });
        }

        return isSuccessful;

    }

    private class MyThread implements Runnable {

        private String myurl;

        MyThread(String url) {
            this.myurl = url;
        }

        @Override
        public void run() {

            // This is inside and it cannot interact directly to UI thread.
            // Interact with UI and visible the progress layout
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layoutProgress.setVisibility(View.VISIBLE);
                }
            });

            downloadImageUsingThread(myurl);
        }
    }
}
