package com.mordeccai.quickcam.activities.splash

import android.content.Intent
import android.os.Handler
import com.mordeccai.quickcam.activities.BasePresenter
import com.mordeccai.quickcam.activities.home.HomeActivity
import com.mordeccai.quickcam.activities.intro.IntroActivity
import android.preference.PreferenceManager
import android.content.SharedPreferences



class SplashPresenter(val ctx: SplashActivity) : BasePresenter(ctx) {

    override fun onInit() {
        super.onInit()
        val handler = Handler()
        handler.postDelayed({ navigateToHomeActivity() }, 500)
    }

    fun navigateToHomeActivity(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        lateinit var intent: Intent
        if (prefs.getBoolean("firstTimeIntro", true)) {
            intent = Intent(ctx, IntroActivity::class.java)
        }else{
            intent = Intent(ctx, HomeActivity::class.java)
        }
        ctx.startActivity(intent)
        ctx.finish()
    }

}