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
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.data.models.Product
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import java.time.LocalDate
import java.util.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import com.mordeccai.quickcam.activities.view_product.ViewProductActivity


class ProductViewModel(val product: Product, val ctx: HomeActivity) : BaseViewModel(ctx){
    fun deleteProduct(view: View){
        confirm("Delete product?", "This will delete ${product.name} along with its images.", onOk = {
            print("The resilt is $it")
            val index = ctx.presenter.products.indexOf(this)
            ctx.presenter.db.productDao().delete(product)
            ctx.presenter.products.removeAt(index)
            ctx.presenter.notifyChange();
        });
    }

    fun showPhotos(){
        val intent = Intent(ctx, ViewProductActivity::class.java)
        intent.putExtra("productId", product.id)
        intent.putExtra("productName", product.name)
        ctx.startActivity(intent)
    }

    fun formatedDate(dateString: String): String {
        val todayTime = DateTime.now()
        val date = DateTime.parse(dateString)
        val interval = Interval(date, todayTime)
        var result  = ""

        if(interval.toPeriod() == Period.days(1)){
            result =  "YESTERDAY"
        }

        if(interval.toPeriod().hours < 24){
            val formatter = DateTimeFormat.forPattern("h:mm a")
            return date.toString(formatter)
        }

        if(interval.toPeriod().days > 1){
            val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
            result = date.toString(formatter)
        }
        return result
    }
}