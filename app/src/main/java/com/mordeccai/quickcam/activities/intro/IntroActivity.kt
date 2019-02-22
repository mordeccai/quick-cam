package com.mordeccai.quickcam.activities.intro

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codemybrainsout.onboarder.AhoyOnboarderActivity
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation .IntegerRes
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.codemybrainsout.onboarder.AhoyOnboarderCard
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mordeccai.quickcam.R
import java.util.ArrayList
import android.R.id.edit
import android.content.Intent
import android.preference.PreferenceManager
import android.content.SharedPreferences
import android.content.res.Configuration
import com.mordeccai.quickcam.activities.home.HomeActivity
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_NORMAL
import android.content.res.Configuration.SCREENLAYOUT_SIZE_SMALL
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import android.os.Build
import android.util.DisplayMetrics


class IntroActivity : AhoyOnboarderActivity(){
    var grantedPermissions = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ahoyOnboarderCard1 = AhoyOnboarderCard("Take pictures", "Specify the product and save its images for future reference.", R.drawable.products)
        val ahoyOnboarderCard2 = AhoyOnboarderCard("Download csv", "After identifying your products and saving their images you can export the data of all your products as csv.", R.drawable.csv_red)

        ahoyOnboarderCard1.setBackgroundColor(R.color.white)
        ahoyOnboarderCard2.setBackgroundColor(R.color.white)
        if(getDeviceDimens()["width"]!!.compareTo(550) <= 0 || getDeviceDimens()["height"]!!.compareTo(800) <= 0){
            ahoyOnboarderCard1.setIconLayoutParams(200, 200, 5, 5, 5,5)
            ahoyOnboarderCard2.setIconLayoutParams(120, 120, 5, 5, 5,5)
        }
        val pages = ArrayList<AhoyOnboarderCard>()

        pages.add(ahoyOnboarderCard1)
        pages.add(ahoyOnboarderCard2)

        for (page in pages) {
            page.setTitleColor(R.color.black)
            page.setDescriptionColor(R.color.grey_600)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setFinishButtonTitle("Grant Permissions")
        } else {
            setFinishButtonTitle("Finish")
        }

        //setFinishButtonDrawableStyle(resources.getDrawable(R.drawable.permissions_button))
        showNavigationControls(false)

        val colorList = ArrayList<Int>()
        colorList.add(R.color.colorPrimary)
        colorList.add(R.color.colorAccent)

        setColorBackground(colorList)

        setOnboardPages(pages)
    }


    fun grantPermissions(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(
                            response: PermissionGrantedResponse?) {
                        grantedPermissions = true;
                        setFinishButtonTitle("Finish")
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?) {
                        AlertDialog.Builder(this@IntroActivity)
                                .setTitle(
                                        R.string.storage_permission_rationale_title)
                                .setMessage(
                                        R.string.storage_permition_rationale_message)
                                .setNegativeButton(
                                        android.R.string.cancel,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.cancelPermissionRequest()
                                        })
                                .setPositiveButton(android.R.string.ok,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.continuePermissionRequest()
                                        })
                                .setOnDismissListener({
                                    token?.cancelPermissionRequest() })
                                .show()
                    }

                    override fun onPermissionDenied(
                            response: PermissionDeniedResponse?) {
                        Toast.makeText(this@IntroActivity, "Permissions not granted", Toast.LENGTH_LONG).show()
                    }
                })
                .check()
    }

    fun saveAppStatusAndNavigate(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.putBoolean("firstTimeIntro", false)
        editor.apply()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFinishButtonPressed() {
        if(grantedPermissions || (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)){
            saveAppStatusAndNavigate()
        } else {
            grantPermissions()
        }
    }

    fun getDeviceDimens(): Map<String, Int>{
        val displayMetrics: DisplayMetrics =  DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels
        return mapOf("height" to height, "width" to width)
    }


    private fun getScreenSizeName(): String {
        var screenLayout = getResources().getConfiguration().screenLayout
        screenLayout = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        when (screenLayout) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> return "small"
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> return "normal"
            Configuration.SCREENLAYOUT_SIZE_LARGE -> return "large"
            4 // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
            -> return "xlarge"
            else -> return "undefined"
        }
    }
}