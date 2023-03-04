package com.mahmutalperenunal.qrcodescannerandgenerator

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.mahmutalperenunal.qrcodescannerandgenerator.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set up action bar
        binding.mainToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding.scanButton.setOnClickListener {

            AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle(R.string.app_name)
                .setMessage(R.string.message_text)
                .setPositiveButton(R.string.gallery_text) {
                        dialog, _ ->

                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 100)

                    dialog.dismiss()
                }
                .setNegativeButton(R.string.camera_text) {
                        dialog, _ ->

                    val intent = Intent(applicationContext, ScannerActivity::class.java)
                    startActivity(intent)

                    dialog.dismiss()
                }
                .create()
                .show()

        }

        binding.generateButton.setOnClickListener {
            val intent = Intent(applicationContext, GeneratorActivity::class.java)
            startActivity(intent)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            try {
                val imageUri: Uri? = data!!.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)

                try {
                    val contents: String?
                    val intArray = IntArray(selectedImage.width * selectedImage.height)
                    selectedImage.getPixels(
                        intArray,
                        0,
                        selectedImage.width,
                        0,
                        0,
                        selectedImage.width,
                        selectedImage.height
                    )
                    val source: LuminanceSource = RGBLuminanceSource(
                        selectedImage.width,
                        selectedImage.height,
                        intArray
                    )
                    val bitmap = BinaryBitmap(HybridBinarizer(source))
                    val reader: Reader = MultiFormatReader()
                    val result = reader.decode(bitmap)

                    contents = result.text

                    if (contents.toString().contains("https://") || contents.toString().contains("http://")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contents.toString()))
                        startActivity(intent)
                    } else {
                        AlertDialog.Builder(this, R.style.CustomAlertDialog)
                            .setTitle(R.string.title_text)
                            .setMessage(contents.toString())
                            .setPositiveButton(R.string.copy_text) {
                                    dialog, _ ->

                                val clipboard: ClipboardManager =
                                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip: ClipData = ClipData.newPlainText(R.string.title_text.toString(), contents.toString())
                                clipboard.setPrimaryClip(clip)

                                Toast.makeText(applicationContext, R.string.copied_text, Toast.LENGTH_SHORT).show()

                                dialog.dismiss()
                            }
                            .setNegativeButton(R.string.ok_text) {
                                    dialog, _ ->
                                onStop()
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, R.string.failed_text, Toast.LENGTH_LONG).show()
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, R.string.failed_text, Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, R.string.not_choose_image_text, Toast.LENGTH_LONG).show()
        }

    }


    //exit application with double click
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intentMainExit = Intent(Intent.ACTION_MAIN)
        intentMainExit.addCategory(Intent.CATEGORY_HOME)
        intentMainExit.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainExit)
        finish()
    }
}