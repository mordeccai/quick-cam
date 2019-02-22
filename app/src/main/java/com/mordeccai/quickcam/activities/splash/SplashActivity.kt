package com.mordeccai.quickcam.activities.splash

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.activities.BaseActivity
import com.mordeccai.quickcam.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.presenter = SplashPresenter(this)
        binding.presenter?.onInit()
    }
}
