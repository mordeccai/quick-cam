package com.mordeccai.quickcam.activities.product

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.MenuItem
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.activities.BaseActivity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.PersistableBundle
import com.mordeccai.quickcam.activities.home.HomeActivity
import com.mordeccai.quickcam.data.AppDatabase
import com.mordeccai.quickcam.databinding.ActivityHomeBinding
import com.mordeccai.quickcam.databinding.ActivityProductBinding


class ProductActivity : BaseActivity(){
    lateinit var presenter: ProductPresenter
    lateinit var binding: ActivityProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProductPresenter(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        binding.presenter = presenter
        binding.presenter?.onInit()
        val actionBar = getSupportActionBar();
        actionBar?.title = "Add Product"
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                val intent = Intent(this@ProductActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK
                && requestCode == presenter.TAKE_PHOTO_REQUEST) {
            presenter.processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
