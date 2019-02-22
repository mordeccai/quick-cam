package com.mordeccai.quickcam.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.mordeccai.quickcam.R
import com.facebook.drawee.view.SimpleDraweeView
import android.databinding.BindingAdapter
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.bumptech.glide.Glide
import com.mordeccai.quickcam.adapters.BaseRecyclerAdapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.mordeccai.quickcam.data.AppDatabase
import java.io.File


open class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenter = BasePresenter(this)
        presenter.onInit()
    }

    fun getImageName(url: String?): String{
        if(url == "" || url == null) return ""
        val cursor = contentResolver.query(Uri.parse(url),
                Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
                null, null, null)
        cursor?.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor?.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)
        return uri.toString().split('/').last()
    }

    companion object {
        @JvmStatic()
        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String) {
            val context = imageView.getContext()
            Glide.with(context)
                    .load(url)
                    .into(imageView)
        }
        @JvmStatic()
        @BindingAdapter("imageUri")
        fun setImageUri(imageView: ImageView, uri: Uri) {
            val context = imageView.getContext()
            Glide.with(context)
                    .load(File(uri.path))
                    .into(imageView)
        }
        @JvmStatic()
        @BindingAdapter("adapter")
        fun setAdapter(recyclerView: RecyclerView?, adapter: BaseRecyclerAdapter) {
            recyclerView?.adapter = adapter
        }
        @JvmStatic()
        @BindingAdapter("onItemSelected")
        fun setOnItemSeleted(spinner: Spinner, listener: OnItemSelectedListener){
            spinner.setOnItemSelectedListener(listener)
        }
        @JvmStatic()
        @BindingAdapter("show")
        fun isVisible(view: View, isVisible: Boolean){
            if(isVisible){
                view.visibility = View.VISIBLE
            }else{
                view.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}