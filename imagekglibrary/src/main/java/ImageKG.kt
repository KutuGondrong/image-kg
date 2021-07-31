package com.kutugondrong.imagekglibrary

import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import android.net.Uri
import android.widget.ImageView
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest


/**
 * KG KutuGondrong
 * This is Main class For Load Image from url
 * <pre><code>
 * ImageKG.dslImageHelper {
 *              context = itemView.context
 *              urlImage = item.user.image.medium
 *              this.imageView = this@ViewHolderList.imageView
 *          }.process()
 * @see process
 */

class ImageKG private constructor(builder: Builder) {

    private var context: Context? = null
    private var imageViewReference: WeakReference<ImageView>? = null
    private var urlImage: String? = null

    init {
        context = builder.context
        imageViewReference = WeakReference(builder.imageView)
        urlImage = builder.urlImage
    }
    companion object {
        inline fun dslImageHelper(block: Builder.() -> Unit) =
            Builder().apply(block)
                .build()
    }

    class Builder{
        var context: Context? = null
        var imageView: ImageView? = null
        var urlImage: String? = null
        fun build() = ImageKG(this)
    }

    /**
     * This main function for load image from network
     * and Image caching
     */
    fun process() {
        if (!validation()) return
        val file = getFile()
        if (file.exists()) {
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
            imageViewReference!!.get()?.setImageBitmap(myBitmap)
        } else {
            val urlImageNet = URL(urlImage)
            if (urlImage != null && imageViewReference!= null && context!= null) {
                GlobalScope.launch {
                    delay(1000)
                    getImage( urlImageNet)
                }
            }
        }
    }

    private suspend fun getImage(urlImageNet: URL) {
        try {
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                val file = getFile()
                if (file.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
                    imageViewReference!!.get()?.setImageBitmap(myBitmap)
                } else {
                    val result: Deferred<Bitmap?> = GlobalScope.async {
                        urlImageNet.toBitmap()
                    }
                    val bitmap : Bitmap? = result.await()
                    bitmap?.apply {
                        val savedUri : Uri? = saveToInternalStorage()
                        if (imageViewReference != null) {
                            imageViewReference!!.get()?.setImageURI(savedUri)
                        }
                    }
                }
            }
        } catch (e: Exception) {

        }

    }
    private fun URL.toBitmap(): Bitmap?{
        return try {
            BitmapFactory.decodeStream(openStream())
        }catch (e:IOException){
            null
        }
    }

    private fun Bitmap.saveToInternalStorage(): Uri?{
        val file = getFile()

        return try {
            val stream: OutputStream = FileOutputStream(file)
            compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Uri.parse(file.absolutePath)
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    private fun getFile() : File {
        val wrapper = ContextWrapper(context)
        val file = wrapper.getDir("images", Context.MODE_PRIVATE)
        return File(file, "${md5(urlImage!!)}.jpg")
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun validation() : Boolean{
        if (context == null || imageViewReference == null || urlImage.isNullOrEmpty()) {
            return false
        }
        return true
    }
}