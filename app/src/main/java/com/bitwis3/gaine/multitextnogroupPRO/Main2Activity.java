package com.bitwis3.gaine.multitextnogroupPRO;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;
import spencerstudios.com.fab_toast.FabToast;

public class Main2Activity extends AppCompatActivity {
    static final Integer SMS = 0x5;
    static final Integer READ = 0x3;
    ArrayList<String> numbersToSend;
    ArrayList<String> namesToSend;
    String etMessage = "";
    DBRoom db;
    EditText editText;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fabsend = (FloatingActionButton) findViewById(R.id.fabsend);
        if(getIntent().hasExtra("timed")){
            getSupportActionBar().setTitle("Timed Text Message");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient3));
            fabsend.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.nextwhite_24dp));

        }else{
            getSupportActionBar().setTitle("Text Many No Group");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient2));        }


        handler = new Handler();
        editText = (EditText)findViewById(R.id.editText);
        numbersToSend = getIntent().getStringArrayListExtra("array");
        namesToSend = getIntent().getStringArrayListExtra("names");

        if(getIntent().hasExtra("edit")){
        editText.setText(getIntent().getStringExtra("editMessage"));
            fabsend.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_save_black_24dp));
        }

        fabsend.setOnClickListener(ask);
        MainActivity.returning = true;
        askForPermission("android.permission.READ_PHONE_STATE", READ);
        db = Room.databaseBuilder(this,DBRoom.class, "_database_multi_master")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();

    }



    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Main2Activity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{permission}, requestCode);
            }
        } else {
            switch (requestCode) {
                case 5:


                        sendTheMessage(numbersToSend,etMessage);




                    break;

                case 3:




                    break;
                //Accounts

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {


                case 5:


                        sendTheMessage(numbersToSend,etMessage);


                    break;
                //Accounts
                case 3:

            }

            FabToast.makeText(this, "Permission granted", Toast.LENGTH_SHORT, FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show();
        } else {
            Toast.makeText(this, "Permission denied, reset permissions for this app in your System -> App settings", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Permission denied, reset permissions for this app in your System -> App settings", Toast.LENGTH_SHORT).show();

            Main2Activity.this.finish();
        }
    }

//    public void sendTheMessage(ArrayList<String> numbers, String message) {
//
//        try{
//        ArrayList<String> AL = numbers;
//        int size = AL.size();
//        SmsManager smsManager = SmsManager.getDefault();
//
//            boolean ready = false;
//            if(Seed.isTelephonyMobileConnected(this)){
//                ready = true;
//            }
//
//        for (int i = 0; i < size; i++) {
//            Log.i("JOSHd", numbers.get(i) + " " + message);
//
//    if(message.length() > 155){
//        ArrayList<String> parts = smsManager.divideMessage(message);
//        smsManager.sendMultipartTextMessage(numbers.get(i), null,
//                parts, null, null);
//    }else{
//        smsManager.sendTextMessage(numbers.get(i), null,
//                message, null, null);
//    }
//
//            Contact contact = new Contact();
//            contact.setName(namesToSend.get(i));
//            contact.setNumber(numbersToSend.get(i));
//            if(ready){
//                contact.setTypeEntry("multi_text");
//            }else{
//                contact.setTypeEntry("missed");}
//            contact.setMessage(message);
//            contact.setTimeInMillis(System.currentTimeMillis());
//            db.multiDOA().insertAll(contact);
//
//        }
//            if(ready){
//                FabToast.makeText(Main2Activity.this , "Success, you can view your sent messages in your Logs, under the History tab.",
//                        Toast.LENGTH_LONG, FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show();
//
//            }else{
//                showNotificationForOld();}
//
//        Bungee.slideRight(this);
//        Main2Activity.this.finish();
//
//        }catch (Exception e){
//            FabToast.makeText(this, "We do apologize, but your device is not supported currently.", Toast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
//
//        }
//    }

    private void sendMessage(final int i, final SmsManager smsManager, final ArrayList<String> numbers,
                             final String message){

          handler.postDelayed(new Runnable() {
              @Override
              public void run() {

                  Log.i("JOSHnew", numbers.get(i) + " " + message);
                  if(numbers.get(i) != null) {
                      if(message.length() > 155){
                          ArrayList<String> parts = smsManager.divideMessage(message);
                          smsManager.sendMultipartTextMessage(numbers.get(i), null,
                                  parts, null, null);
                      }else{
                          smsManager.sendTextMessage(numbers.get(i), null,
                                  message, null, null);
                      }
                  }

                  Contact contact = new Contact();
                  contact.setName(namesToSend.get(i));
                  contact.setNumber(numbersToSend.get(i));
                  if(Seed.isTelephonyMobileConnected(Main2Activity.this)){
                      contact.setTypeEntry("multi_text");
                  }else{
                      contact.setTypeEntry("missed");}
                  contact.setMessage(message);
                  contact.setTimeInMillis(System.currentTimeMillis());
                  db.multiDOA().insertAll(contact);

                  if(i<numbers.size()-1){
                      sendMessage(i+1,smsManager,numbers,message);
                  }else{
                   return;
                  }

              }
          },100);

    }













    public void sendTheMessage(ArrayList<String> numbers, String message) {

        try{
            ArrayList<String> AL = numbers;
            int size = AL.size();
            SmsManager smsManager = SmsManager.getDefault();

            boolean ready = false;
            if(Seed.isTelephonyMobileConnected(this)){
                ready = true;
            }

            sendMessage(0,smsManager,numbers,message);

            if(ready){
                FabToast.makeText(Main2Activity.this , "Success, you can view your sent messages in your Logs, under the History tab.",
                        Toast.LENGTH_LONG, FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show();

            }else{
                showNotificationForOld();}

            Bungee.slideRight(this);
            Main2Activity.this.finish();

        }catch (Exception e){
            FabToast.makeText(this, "We do apologize, but your device is not supported currently.", Toast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();

        }
    }




    private View.OnClickListener ask = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {


                case R.id.fabsend:
                    SharedPreferences.Editor prefs = getSharedPreferences("AUTO_PREF", Context.MODE_PRIVATE).edit();
                    prefs.putBoolean("showAd", true);
                    prefs.apply();


                    etMessage = editText.getText().toString().trim();

                    if (getIntent().hasExtra("edit")) {
                        db.multiDOA().updateMessageWithID(getIntent().getIntExtra("edit", 0), etMessage);
                        FabToast.makeText(Main2Activity.this, "Successfully Updated", Toast.LENGTH_LONG, FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show();
                        finish();
                    } else {


                        if (etMessage.length() > 0) {
                            if (getIntent().hasExtra("timed")) {
                                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                                intent.putExtra("timed", "timed");
                                intent.putExtra("array", numbersToSend);
                                intent.putExtra("names", namesToSend);
                                intent.putExtra("message", etMessage);
                                startActivity(intent);
                                Bungee.slideLeft(Main2Activity.this);
                                Main2Activity.this.finish();
                            } else {


                                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                askForPermission(Manifest.permission.SEND_SMS, SMS);
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:

                                                break;
                                        }

                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                                builder.setMessage("NOTE - this is using your default texting service, if you are not on an unlimited texting plan, this could incur charges.\n\nRoll out Message to: " + getStringOfPeople()).setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();


                            }
                        } else {
                            FabToast.makeText(Main2Activity.this, "There is no message to send!",
                                    Toast.LENGTH_LONG, FabToast.ERROR, FabToast.POSITION_DEFAULT).show();
                        }
                        break;

                    }
            }
        }
    };

    public String getStringOfPeople(){
        StringBuilder builders = new StringBuilder();
        for(int i = 0 ; i< namesToSend.size(); i++){
             if(i == 0){
                 builders.append(namesToSend.get(i));
             }else {
                 builders.append(", ");

                 builders.append(namesToSend.get(i));
             }
        }
        return builders.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                Bungee.slideRight(this);
                this.finish();

                return true;


            case R.id.emergencytoolbar:
                startActivity(new Intent(this, Emergency.class));
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Bungee.slideRight(this);

    }




    private void showNotificationForOld() {
        RemoteViews contentView = new RemoteViews("com.bitwis3.gaine.multitextnogroup", R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.icon);
        contentView.setTextViewText(R.id.notificationtext, "Your Scheduled Message(s) were not sent! Click to handle.");

        Notification.Builder builder =
                new Notification.Builder(this);

        builder.setSmallIcon
                (R.drawable.ic_message_black_24dp);
        Intent intent2 =
                new Intent(this, ActivityLog.class);

        intent2.putExtra("missed", "missed");

        int latestCode = db.multiDOA().getLatestCode()+1000000;
        Log.i("JOSHser", String.valueOf(latestCode));
        PendingIntent pendingIntent;
        if(latestCode >=  0){

            pendingIntent =
                    PendingIntent.getActivity(this, db.multiDOA().getLatestCode()+1000000
                            , intent2, 0);
        }else{
            pendingIntent =
                    PendingIntent.getActivity(this, 1
                            , intent2, 0);
        }
        intent2.putExtra("code", latestCode);

        builder.setContentIntent(pendingIntent);
        builder.setContent(contentView);
        builder.setContentTitle("Your Scheduled Messages were not sent! Click to handle.");
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_LOW);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = "default use";
            String description = "get reminders from this app";
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel("ChannelID", name, importance);
            mChannel.setDescription(description);
            mChannel.setSound(null, null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId("ChannelID");
        }

        Notification notification = builder.build();


        NotificationManager notificationMgr = (NotificationManager)
                this.getSystemService(NOTIFICATION_SERVICE);


        notificationMgr.notify(latestCode, notification);
    }
}

