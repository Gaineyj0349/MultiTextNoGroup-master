@file:JvmName("HomeStatic")
package com.bitwis3.gaine.multitextnogroupPRO

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.ads.consent.ConsentForm
import io.supercharge.shimmerlayout.ShimmerLayout
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import net.frederico.showtipsview.ShowTipsBuilder
import net.frederico.showtipsview.ShowTipsView
import net.frederico.showtipsview.ShowTipsViewInterface
import org.michaelbel.bottomsheet.BottomSheet
import org.michaelbel.bottomsheet.BottomSheetCallback
import osmandroid.project_basics.Task
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast





class Home : AppCompatActivity() {

    internal lateinit var db: DBRoom;
    private var form: ConsentForm? = null


    lateinit var shimmerText1: ShimmerLayout
    lateinit var shimmerText2: ShimmerLayout
    lateinit var shimmerText3: ShimmerLayout
    lateinit var shimmerText5: ShimmerLayout
    lateinit var shimmerText4: ShimmerLayout
    lateinit var shimmerText6: ShimmerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        Log.i("JOSHyo", "oncreate")


        setFonts()
        val pref = getSharedPreferences("AUTO_PREF", MODE_PRIVATE)
        var showTip = pref.getBoolean("showTip", true);

        if(showTip){
            initTheAnims()
            showTip()
        }else{
            initTheAnims()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.EMERGENCY) {
            startActivity(Intent(this, Emergency::class.java))
            return true
        }

        if (id == R.id.test2) {
            startActivity(Intent(this, Credits::class.java))
            Bungee.card(this)
            return true
        }
        if (id == R.id.importexport) {
            startActivity(Intent(this, ImportExport2::class.java))
            Bungee.card(this)
            return true
        }
        if (id == R.id.share_app) {
            Task.ShareApp(this, "com.bitwis3.gaine.multitextnogroupPRO",
                    "Multi-Text",
                    "A great tool for useful texting features!")

            return true
        }

        if (id == R.id.action_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            Bungee.card(this)
            return true
        }

        if (id == R.id.rate_app) {
            Task.RateApp(this, "com.bitwis3.gaine.multitextnogroupPRO")
            return true
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()
       startAnims()
        if(infoOn()){
            showInfos()
        }else{
            hideInfos()
        }


        val prefs = getSharedPreferences("AUTO_PREF", Context.MODE_PRIVATE)


    }




    fun infoClicked(v: View){
        when(v.id){
            R.id.card1info ->{initNewBuilder(resources.getString(R.string.card1info))}
            R.id.card2info ->{initNewBuilder(resources.getString(R.string.card2info))}
            R.id.card3info ->{initNewBuilder(resources.getString(R.string.card3info))}
            R.id.card4info ->{initNewBuilder(resources.getString(R.string.card4info))}
            R.id.card5info ->{initNewBuilder(resources.getString(R.string.card5info))}
            R.id.card6info ->{initNewBuilder(resources.getString(R.string.card6info))}
        }
    }

    fun initNewBuilder(message: String) {
        val builder: BottomSheet.Builder
        builder = BottomSheet.Builder(Home@this)


        builder.setWindowDimming(80)
                .setTitleMultiline(true)
                .setBackgroundColor(Color.parseColor("#313131"))
                .setTitleTextColor(Color.parseColor("#ffffff"))


        builder.setOnShowListener { }
        builder.setOnDismissListener { }

        builder.setCallback(object : BottomSheetCallback {
            override fun onShown() {}

            override fun onDismissed() {}
        })

        builder.setTitle(message)
        builder.show()
    }

    fun infoOn(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
       return sharedPreferences.getBoolean("info", true)
    }

    fun showInfos(){
       card1info.visibility = View.VISIBLE
        card2info.visibility = View.VISIBLE
        card3info.visibility = View.VISIBLE
        card4info.visibility = View.VISIBLE
        card5info.visibility = View.VISIBLE
        card6info.visibility = View.VISIBLE
    }
    fun hideInfos(){
        card1info.visibility = View.GONE
        card2info.visibility = View.GONE
        card3info.visibility = View.GONE
        card4info.visibility = View.GONE
        card5info.visibility = View.GONE
        card6info.visibility = View.GONE
    }

fun goToGroupManage(v: View){
    val intent = Intent(this@Home, CreateContactList::class.java)
    startActivity(intent)
    Bungee.slideLeft(this)
}

    fun goToLog(v: View){
        val intent = Intent(this@Home, ActivityLog::class.java)
        startActivity(intent)
        Bungee.slideLeft(this)
    }

    fun goToEmergency(v: View){
        val intent = Intent(this@Home, Emergency::class.java)
        startActivity(intent)
        Bungee.slideLeft(this)

    }
    fun goToMainAct(v: View){
        val intent = Intent(this@Home, MainActivity::class.java)
        startActivity(intent)
        Bungee.slideLeft(this)
    }


  fun  setFonts(){
      val font = Typeface.createFromAsset(this.getAssets(), "Acme-Regular.ttf")

    }

fun initTheAnims(){
    shimmerText2 = findViewById(R.id.shimmer_text_card2) as ShimmerLayout
     shimmerText5 = findViewById(R.id.shimmer_text_card5) as ShimmerLayout
    shimmerText1 = findViewById(R.id.shimmer_text_card1) as ShimmerLayout
    shimmerText3 = findViewById(R.id.shimmer_text_card3) as ShimmerLayout
    shimmerText4 = findViewById(R.id.shimmer_text_card4) as ShimmerLayout

    shimmerText6 = findViewById(R.id.shimmer_text_card6) as ShimmerLayout

    }

    fun startAnims(){
        var handler = Handler()
        handler.postDelayed({stopAnims()},5000)
        shimmerText2.startShimmerAnimation()
        shimmerText5.startShimmerAnimation()
        shimmerText1.startShimmerAnimation()
        shimmerText3.startShimmerAnimation()
        shimmerText4.startShimmerAnimation()
        shimmerText6.startShimmerAnimation()
    }

    fun stopAnims(){
        shimmerText2.stopShimmerAnimation()
        shimmerText5.stopShimmerAnimation()
        shimmerText1.stopShimmerAnimation()
        shimmerText3.stopShimmerAnimation()
        shimmerText4.stopShimmerAnimation()
        shimmerText6.stopShimmerAnimation()
    }


fun goToTimedText(v: View){
    val intent = Intent(this@Home, MainActivity::class.java)
    intent.putExtra("timed", "timed")
    startActivity(intent)
    Bungee.slideLeft(this)
}

    fun goToAuto(v: View){
        val intent = Intent(this@Home, AutoAct::class.java)
        startActivity(intent)
        Bungee.slideLeft(this)
    }

    fun showTip() {
        var showtips: ShowTipsView = ShowTipsBuilder(Home@this)
                .setTitle("Feature Explanation")
                .setDescription("Click these little icons to see an overview of what each tab does, These can be hidden in the settings once you are familiar with this app.")
                .setDelay(1500)
                .setTarget(card2info)
                .build()

        showtips.show(Home@this)
        showtips.setCallback(object : ShowTipsViewInterface {
            override fun gotItClicked() {
                val editor = getSharedPreferences("AUTO_PREF", MODE_PRIVATE).edit()
                editor.putBoolean("showTip", false)
                editor.apply()
                initTheAnims()

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("JOSHd", "onDestroy")
    }

    internal var double_backpressed = false
    override fun onBackPressed() {

        var prefs = getSharedPreferences("AUTO_PREF", Context.MODE_PRIVATE)
        var show = prefs.getBoolean("showRateDialog", true)

        if(show){

        var dialog = Dialog(Home@this)
        var view = layoutInflater.inflate(R.layout.rate_buy_exit, null)

        var llrate = view.findViewById<LinearLayout>(R.id.llrate);
        var llnever = view.findViewById<LinearLayout>(R.id.llnever);
        var lllater = view.findViewById<LinearLayout>(R.id.lllater);


        llrate.setOnClickListener({Task.RateApp(Home@this, "com.bitwis3.gaine.multitextnogroupPRO")
            val editor = getSharedPreferences("AUTO_PREF", MODE_PRIVATE).edit()
            editor.putBoolean("showRateDialog", false)
            editor.apply()})
        llnever.setOnClickListener({
            val editor = getSharedPreferences("AUTO_PREF", MODE_PRIVATE).edit()
            editor.putBoolean("showRateDialog", false)
            editor.apply()
            dialog.dismiss()
            super.onBackPressed()
        })
        lllater.setOnClickListener({
       dialog.dismiss()
            super.onBackPressed()

        })
        dialog.setContentView(view)
        dialog.show()

        }else {


        if (double_backpressed) {
            super.onBackPressed()
            return
        }
        this.double_backpressed = true
        FabToast.makeText(Home@this,
                "Click back again to exit.", Toast.LENGTH_SHORT, FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()

        Handler().postDelayed({ double_backpressed = false }, 2000)
        }
    }

}

