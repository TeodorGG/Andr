package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notification 10 sec
        Button push_bottom = findViewById(R.id.push_bottom);

        push_bottom.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    Thread.sleep(10 * 1_000);
                    createNatification();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });


        //Search in Google
        EditText search_edittext;
        Button search_bottom;

        search_edittext = findViewById(R.id.search_edittext);
        search_bottom = findViewById(R.id.search_bottom);
        search_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_google(search_edittext.getText().toString());
            }
        });


        //Camera

        RadioGroup type_camera_rg = findViewById(R.id.type_camera_rg);

        Button camera_button = findViewById(R.id.camera_button);

        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = type_camera_rg.getCheckedRadioButtonId();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(selectedId == 1){
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                } else {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                }

                startActivityForResult(cameraIntent, 12);


            }
        });

        //Animation
        Button animation_button = findViewById(R.id.animation_button);
        animation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation_button.animate().rotationBy(360).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
            }
        });

    }

    private void search_google(String search) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        if (search.equals("")) {
            Toast.makeText(this, "SEARCH TEXT IN EMPTY", Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(SearchManager.QUERY, search);
            startActivity(intent);
        }
    }

    private void createNatification() {

        String CHANNEL_ID="Chanel";
        NotificationChannel notificationChannel= new NotificationChannel(CHANNEL_ID,"name",NotificationManager.IMPORTANCE_LOW);
        Notification notification=new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentText("Title Notification 10 sec")
                .setContentTitle("Content Notification 10 sec")
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .build();

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,notification);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {

            Bitmap image = (Bitmap) data.getExtras().get("data");

            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
            intent.putExtra("img", image);
            startActivity(intent);
        }
    }

}