package com.bitwis3.gaine.multitextnogroupPRO

import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_import_export2.*
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast

class ImportExport2 : AppCompatActivity() {

    lateinit var db: DBRoom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_import_export2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        db =  Room.databaseBuilder(applicationContext,
                DBRoom::class.java, "_database_multi_master")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        val seed = Seed()
        val gson = Gson()
        val typeOfSource = object : TypeToken<List<Contact>>() {
        }.type
        exportButton.setOnClickListener{
            val all = db.multiDOA().allDataInList
            val jsonContacts = gson.toJson(all, typeOfSource)
            seed.copyToClipTray(this,"Export Multi Text Data", jsonContacts)
            Log.i("JOSHjson", jsonContacts)
            FabToast.makeText(ImportExport@this, "Success! Go to the other app and import now!", Toast.LENGTH_LONG,
                    FabToast.SUCCESS, FabToast.POSITION_CENTER).show()

            finish()
        }
        importButton.setOnClickListener {

            val importString = etImport.text.toString().trim()
            if (importString.length > 0) {
                try {
                    var list: List<Contact> = gson.fromJson(importString, typeOfSource)
                    var id = db.multiDOA().lastId
                    for(c in list){
                        c.id = ++id
                        db.multiDOA().insertAll(c)
                    }

                    finish()
                } catch (e: Exception) {
                    Log.i("JOSHjson", e.message)
                    FabToast.makeText(ImportExport@this, "Unexpected text, Please try again", Toast.LENGTH_SHORT,
                            FabToast.ERROR, FabToast.POSITION_CENTER).show()
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_emergency, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {

                Bungee.slideRight(this)
                this.finish()

                return true
            }
            R.id.emergencytoolbar-> {
                startActivity(Intent(this, Emergency::class.java))
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
