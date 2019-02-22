package com.mordeccai.quickcam.viewmodels

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.adapters.ProductsAdapter
import com.mordeccai.quickcam.data.models.Product
import java.util.*

open class BaseViewModel(val context: Context) : BaseObservable(){
    fun showToast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun alert(title: String ="Alert", body: String ="", icon: Int = 0, isMaterial: Boolean = false){
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isMaterial) {
            builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(context)
        }
        builder.setTitle(title)
                .setMessage(body)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    // continue with delete
                }
                .setIcon(icon)
                .show()
    }

    fun confirm(title: String ="Confirm", body: String ="", onOk: (isChecked: Boolean) -> Unit, icon: Int = 0, checkBoxText: CharSequence="",  hasCheckBox: Boolean= false ,isMaterial: Boolean = false){
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isMaterial) {
            builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(context)
        }

        val parent = LinearLayout(context)
        parent.setPadding( 40, 0, 0, 0)
        parent.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        parent.orientation = LinearLayout.HORIZONTAL
        val checkBox = CheckBox(context)
        val checkBoxTextView = TextView(context)
        checkBoxTextView.text = checkBoxText
        checkBoxTextView.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
        }
        parent.addView(checkBox)
        parent.addView(checkBoxTextView)

        builder.setTitle(title)
                .setMessage(body)
                .setView(if (hasCheckBox) parent else null)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    // continue with delete
                    if(hasCheckBox && checkBox.isChecked()){
                        onOk(true)
                        //onOk(checkBoxTextView.isActivated)
                    } else{
                        onOk(false)
                        //onOk(false)
                    }
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // do nothing
                }
                .setIcon(icon)
                .show()
    }

    fun pushNote(title: String ="Notification", body: String= "", smallIcon: Int = R.drawable.ic_launcher_foreground, autoCancel: Boolean= true){
        fun ClosedRange<Int>.random() =
                Random().nextInt((endInclusive + 1) - start) +  start

        val randomNumber: Int = (10..100).random()
        val mBuilder = NotificationCompat.Builder(context, "pushNotify$randomNumber")
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(autoCancel);
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(randomNumber, mBuilder.build())
    }
}