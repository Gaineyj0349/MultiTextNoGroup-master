package com.bitwis3.gaine.multitextnogroupPRO

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import spencerstudios.com.fab_toast.FabToast

class MyServiceToSendLocation : Service() {

    lateinit var locationListener: LocationListener
    lateinit var locationManager: LocationManager
    lateinit var smsManager: SmsManager
    lateinit var  message: String
    var code1 = 0
    lateinit var  jsonString: String
    var iNum = 0
    var sourceLatitude = ""
    var sourceLongitude = ""
    lateinit var  db: DBRoom
    var handler = Handler()
    val gson = Gson()
    val typeOfSource = object : TypeToken<List<Contact>>() {
    }.type
    lateinit var listIn: List<Contact>
    val service = this


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


            if(iNum<1) {


                startForeground(2585, getNotificationforService())

                message = intent?.getStringExtra("message")!!
                code1 = intent?.getIntExtra("code1", 1)
                jsonString = intent?.getStringExtra("listIn")
                smsManager = SmsManager.getDefault()
                listIn = gson.fromJson(jsonString, typeOfSource)


                locationListener = MyLocationListener()
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                registerUpdater()


                db = Room.databaseBuilder(applicationContext, DBRoom::class.java, "_database_multi_master")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()

            }
        return START_NOT_STICKY
    }

    fun getNotificationforService(): Notification {
        val contentView = RemoteViews("com.bitwis3.gaine.multitextnogroupPRO", R.layout.custom_notification)
        contentView.setImageViewResource(R.id.image, R.mipmap.icon)
        contentView.setTextViewText(R.id.notificationtext, "Multi-Text is fetching location and Sending the Preset texts now!")

        val builder = Notification.Builder(this)

        builder.setSmallIcon(R.drawable.ic_message_black_24dp)

        builder.setContent(contentView)
        builder.setContentTitle("\"Multi-Text is fetching location and Sending the Preset texts now!")
        builder.setAutoCancel(true)
        builder.setPriority(Notification.PRIORITY_LOW)
        builder.setDefaults(Notification.DEFAULT_VIBRATE)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "default use"
            val description = "get reminders from this app"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel("ChannelID", name, importance)
            mChannel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = this.getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
            builder.setChannelId("ChannelID")
        }

        return builder.build()
    }


    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    fun registerUpdater(){
        @Suppress
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0f, locationListener)
    }


    internal inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            ++iNum
            if(iNum>8){
                iNum = 1
            try{
                sendMessagesWith(code1,listIn)
            }catch (e: Exception){
            }
            }

            sourceLatitude = location.latitude.toString()
            sourceLongitude = location.longitude.toString()


            Log.i("JOSHnew", "" + iNum)
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String,
                                     status: Int, extras: Bundle) {
        }
    }

    private fun sendMessage(message: String, i: Int, listIn: List<Contact>) {
        var con = Contact()
        con.timeInMillis = System.currentTimeMillis()
        con.name = listIn.get(i).name
        con.number = listIn.get(i).number
        con.message = message

        try {
            handler.postDelayed(Runnable {
                if (listIn[i].getNumber() != null) {
                    if (message.length > 155) {
                        val parts = smsManager.divideMessage(message)
                        smsManager.sendMultipartTextMessage(listIn[i].getNumber(), null,
                                parts, null, null)
                    } else {
                        smsManager.sendTextMessage(listIn[i].getNumber(), null,
                                message, null, null)
                    }

                    if (i < listIn.size - 1) {
                        sendMessage(message, i + 1, listIn)
                    } else {


                        return@Runnable
                    }
                }
            }, 100)
            con.typeEntry = "EMERGENCY_TEXT_SENT"
            db.multiDOA().insertAll(con)
        }catch (e: Exception){
            con.typeEntry = "EMERGENCY_TEXT_EXCEPTION"
            db.multiDOA().insertAll(con)
        }
    }

    private fun sendMessagesWith( code1: Int, listIn: List<Contact>){

        var b= StringBuilder()

        b.append("Current Location\n")
b.append("Latitude: $sourceLatitude")
b.append("\n")
b.append("Longitude: $sourceLongitude")
b.append("\n")
b.append("http://www.google.com/maps/place/$sourceLatitude,$sourceLongitude")
b.append("\n\n")

        val stringToSend = b.toString()
        if(Seed.isTelephonyMobileConnected(this@MyServiceToSendLocation)){

            sendMessage(stringToSend,0,listIn)
            locationManager.removeUpdates(locationListener)
            stopForeground(true)
            stopSelf()
            onDestroy()
            FabToast.makeText(this@MyServiceToSendLocation, "Success!", FabToast.LENGTH_LONG,
                    FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show()


    }


}}
