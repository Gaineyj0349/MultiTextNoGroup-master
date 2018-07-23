package com.bitwis3.gaine.multitextnogroupPRO

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_contact_remove.*
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast

class ContactRemove : AppCompatActivity() {

    lateinit var group: String
    lateinit var db: DBRoom
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_remove)
        db= Room.databaseBuilder(applicationContext,
                DBRoom::class.java, "_database_multi_master")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val layoutManager =  LinearLayoutManager(this);
        rvcont.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(rvcont.getContext(),
                layoutManager.getOrientation())
        rvcont.addItemDecoration(dividerItemDecoration)

         group = intent.getStringExtra("group")
        supportActionBar?.title = "Group: $group"
        supportActionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.gradient4))
    initAdapter()


    }

    private fun initAdapter() {
        val groupList = db.multiDOA().getAllInGroupList(group)
        val adapter = AdapterRemove(groupList, ContactRemove@this, db)
        rvcont.adapter = adapter
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

    fun addIn(v: View){
      if(addet1.text.toString().isEmpty() || addet2.text.toString().isEmpty()
      ||addet1.text.toString().isBlank() || addet2.text.toString().isBlank()){
          FabToast.makeText(this,"Fill in neccessary criteria", FabToast.LENGTH_LONG, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
      }else{
          val c = Contact()
          c.name = addet1.text.toString()
          c.number = addet2.text.toString()
          c.group = group
          c.typeEntry = "group"
          db.multiDOA().insertAll(c)
          addet1.setText("")
          addet2.setText("")
          initAdapter()
          hideSoftKeyboard(this)

      }


    }

    //hides the keyboard
    fun hideSoftKeyboard(activity: Activity){
        val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus()!!.getWindowToken(), 0)
    }
}
