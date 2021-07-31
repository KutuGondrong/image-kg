package com.kutugondrong.imagekglibrary

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.kutugondrong.imagekglibrary.databinding.ZoomImageViewShowBinding

/**
 * KG KutuGondrong
 * This class using ZoomImageView to Zoom View
 * @see ZoomImageKGView
 */
class ZoomImageKGViewShowFromURL : AppCompatActivity() {

    companion object {
        const val URL_IMAGE = "URL_IMAGE"
    }

    private lateinit var binding: ZoomImageViewShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window: Window = this.window
        supportActionBar?.hide()
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        binding = ZoomImageViewShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            ImageKG.dslImageHelper {
                context = this@ZoomImageKGViewShowFromURL
                urlImage = this@ZoomImageKGViewShowFromURL.intent.getStringExtra(URL_IMAGE)
                this.imageView = this@with.imageView
            }.process()
        }
    }
}