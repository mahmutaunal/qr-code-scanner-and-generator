package com.mahmutalperenunal.qrcodescannerandgenerator

import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.text.TextUtils
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.mahmutalperenunal.qrcodescannerandgenerator.databinding.ActivityGeneratorBinding

class GeneratorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneratorBinding

    private lateinit var bitmap: Bitmap

    private lateinit var qrEncoder: QRGEncoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup toolbar
        binding.generatorToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.generatorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.generateButton.setOnClickListener { generateQrCode() }
    }

    private fun generateQrCode() {
        if (TextUtils.isEmpty(binding.enterInfoEditText.text.toString())) {
            binding.shareButton.visibility = View.GONE
            binding.enterInfoEditTextLayout.error = ""
            Toast.makeText(applicationContext, R.string.fill_blank_text, Toast.LENGTH_SHORT).show()
        } else {
            binding.shareButton.visibility = View.VISIBLE

            // on below line we are getting service for window manager
            val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            // on below line we are initializing a
            // variable for our default display
            val display: Display = windowManager.defaultDisplay

            // on below line we are creating a variable
            // for point which is use to display in qr code
            val point: Point = Point()
            display.getSize(point)

            // on below line we are getting
            // height and width of our point
            val width = point.x
            val height = point.y

            // on below line we are generating
            // dimensions for width and height
            var dimen = if (width < height) width else height
            dimen = dimen * 3 / 4

            // on below line we are initializing our qr encoder
            qrEncoder = QRGEncoder(binding.enterInfoEditText.text.toString(), null, QRGContents.Type.TEXT, dimen)

            // on below line we are running a try
            // and catch block for initializing our bitmap
            try {
                // on below line we are
                // initializing our bitmap
                bitmap = qrEncoder.bitmap

                // on below line we are setting
                // this bitmap to our image view
                binding.generatorQrcodeImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // on below line we
                // are handling exception
                e.printStackTrace()
            }
        }
    }
}