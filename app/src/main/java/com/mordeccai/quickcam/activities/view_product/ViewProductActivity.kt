package com.mordeccai.quickcam.activities.view_product

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.activities.BaseActivity
import com.mordeccai.quickcam.activities.home.HomeActivity
import com.mordeccai.quickcam.databinding.ActivityViewProductBinding

class ViewProductActivity : BaseActivity() {
    lateinit var presenter: ViewProductPresenter
    lateinit var binding: ActivityViewProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ViewProductPresenter(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_product)
        binding.presenter = presenter
        presenter?.onInit()
        val actionBar = getSupportActionBar()
        actionBar?.title = getIntent().getStringExtra("productName")
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                val intent = Intent(this@ViewProductActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
