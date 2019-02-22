package com.mordeccai.quickcam.activities.home

import android.app.Activity
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.activities.BaseActivity
import com.mordeccai.quickcam.databinding.ActivityHomeBinding
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.data.AppDatabase
import com.mordeccai.quickcam.data.models.Product
import com.getkeepsafe.taptargetview.TapTargetView
import android.graphics.drawable.Drawable
import android.graphics.Typeface
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import com.getkeepsafe.taptargetview.TapTarget




class HomeActivity : BaseActivity() {

    lateinit var presenter: HomePresenter
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        presenter = HomePresenter(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.presenter = presenter
        presenter.onInit()
        if(shouldShowTarget() && presenter.products.size < 1){
            val handler = Handler()
            handler.postDelayed({ setTaptarget() }, 2000)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.download -> presenter.downloadDataAsCsv()
            R.id.about -> presenter.openAppAbout()
            R.id.deleteAll -> presenter.deleteAllRecords()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == presenter.NEW_PRODUCT) {
            presenter.loadData()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    fun saveTargetStatus(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.putBoolean("firstTimeTarget", false)
        editor.apply()
    }

    fun shouldShowTarget(): Boolean{
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getBoolean("firstTimeTarget", true)
    }

    fun setTaptarget(){
        TapTargetView.showFor(this, // `this` is an Activity
                TapTarget.forView(findViewById<View>(R.id.add_product_btn), "Add a product", "Tap on the plus button to add a new product and its images.")
                        // All options below are optional
                        .outerCircleColor(R.color.colorPrimary)
                        .outerCircleAlpha(0.96f)
                        .targetCircleColor(R.color.white)
                        .titleTextSize(20)
                        .titleTextColor(R.color.white)
                        .descriptionTextSize(15)
                        .descriptionTextColor(R.color.white)
                        .textColor(R.color.white)
                        .textTypeface(Typeface.SANS_SERIF)
                        .dimColor(R.color.black)
                        .drawShadow(true)
                        .cancelable(true)
                        .tintTarget(true)
                        .transparentTarget(true)
                        .targetRadius(60),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView) {
                        super.onTargetClick(view)
                        presenter.addProduct()
                    }

                    override fun onTargetDismissed(view: TapTargetView?, userInitiated: Boolean) {
                        super.onTargetDismissed(view, userInitiated)
                        saveTargetStatus()
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
