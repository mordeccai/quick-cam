package com.mordeccai.quickcam.viewmodels

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import com.mordeccai.quickcam.activities.home.HomeActivity
import com.mordeccai.quickcam.activities.home.HomePresenter
import com.mordeccai.quickcam.activities.view_product.ViewProductActivity
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.data.models.Photo
import com.mordeccai.quickcam.data.models.Product
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.time.LocalDate
import java.util.*

class PhotoViewModel(val photo: Photo, val ctx: ViewProductActivity) : BaseViewModel(ctx){
    fun getImageName(): String{
        return ctx.getImageName(photo.path)
    }
}