package com.mordeccai.quickcam.activities.product

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.databinding.InverseBindingAdapter
import android.databinding.ObservableInt
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AlertDialog
import android.view.View
import com.mordeccai.quickcam.activities.BasePresenter
import com.mordeccai.quickcam.data.models.Product
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.Spinner
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mordeccai.quickcam.R
import com.mordeccai.quickcam.data.AppDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Serializable
import java.time.LocalDate
import java.util.*


class ProductPresenter(private val ctx: ProductActivity): BasePresenter(ctx), Serializable {
    var product = Product()
    var currentPhotoPath: String? = null
    var currentImageUri: Uri = Uri.EMPTY;
    var sideOptions: List<String> = listOf("Front", "Back", "Side A", "Side B")
    var imagesCaptured: MutableList<MutableMap<String,Any>> = mutableListOf()
    var currentImageIndex = 0
    val TAKE_PHOTO_REQUEST = 101
    var imageSide: Int = 0;
    var isLongPressed = false;


    fun saveProduct(){
        if(product.name !="" && imagesCaptured.size > 0){
            product.createdAt = DateTime.now().toString()
            isLongPressed = true
            db.productDao().insert(product)
            alert("Product saved!", "${product.name} and its ${if(imagesCaptured.size > 1) "${imagesCaptured.size} images were" else "image was"} saved successfuly.", onOk = {
                val data: Intent = Intent()
                val text: String = "Result to be returned...."
                data.setData(Uri.parse(text))
                ctx.setResult(Activity.RESULT_OK, data)
                ctx.finish()
            })
        } else {
            alert("Unsuccessful", if(product.name == "") "The Product name cannot be empty" else "The product does not have any images")
        }
    }

    fun captureImage(){
        if(imagesCaptured.filter { it -> it["id"] == imageSide }.toList().size > 0){
            confirm("Replace image?", "The old ${sideOptions[imageSide]} image will be replaced by a new image. ", onOk = {
                    launchCamera()
            })
        }else{
                launchCamera()
        }
    }

    fun nextImage(){
        if(nextEnabled()){
            isLongPressed = false
            currentImageIndex += 1
            currentImageUri = imagesCaptured[currentImageIndex]["uri"] as Uri
            notifyChange()
        }
    }

    fun prevEnabled(): Boolean{
        return currentImageIndex + 1 > 1
    }

    fun nextEnabled(): Boolean{
        return currentImageIndex + 1 < imagesCaptured.size
    }

    fun cancelDeletion(){
        isLongPressed = false;
        notifyChange()
    }

    fun onImageLongPress(view:View): Boolean{
        isLongPressed = true;
        notifyChange()
        return false;
    }

    fun prevImage(){
        if(prevEnabled()){
            currentImageIndex -= 1;
            isLongPressed = false;
            currentImageUri = imagesCaptured[currentImageIndex]["uri"] as Uri
            notifyChange()
        }
    }

    fun deleteImage(){
        isLongPressed = false
        imagesCaptured.removeAt(currentImageIndex)
        notifyChange()
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        product.name = s.toString()
        Log.w("tag", "onTextChanged $s")
    }


    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = ctx.contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(ctx.packageManager) != null) {
            currentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            ctx.startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    fun processCapturedPhoto() {
        if(currentPhotoPath != null){
            val cursor = ctx.contentResolver.query(Uri.parse(currentPhotoPath),
                    Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
                    null, null, null)
            cursor?.moveToFirst()
            val photoPath = cursor.getString(0)
            cursor?.close()
            val file = File(photoPath)
            val uri = Uri.fromFile(file)
            currentImageUri = uri
            val imageData: MutableMap<String, Any> = mutableMapOf(
                    "id" to imageSide,
                    "name" to  sideOptions[imageSide],
                    "path" to "$currentPhotoPath",
                    "uri" to currentImageUri
            )
            when(imageSide){
                0 -> product.front = "$currentPhotoPath"
                1 -> product.back = "$currentPhotoPath"
                2 -> product.sideA = "$currentPhotoPath"
                3 -> product.sideB = "$currentPhotoPath"
            }
            imagesCaptured = imagesCaptured.filter{it -> it["id"] != imageSide}.toMutableList();
            imagesCaptured.add(imageData)
            nextImage();
            notifyChange()
        }
    }

    fun onSideSelected(parent: AdapterView<*>, position: Int): Unit{
        showToast("The position is $position")
    }


    fun onItemSelected() = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            imageSide = position
                //alert("The Position is: $position")
            //showToast("Spinner1: position=$position id=$id")
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            showToast("Spinner1: unselected")
        }
    }

    override fun onInit() {
        var count: Int? = null
    }
}