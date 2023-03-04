package com.mahmutalperenunal.qrcodescannerandgenerator

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import com.mahmutalperenunal.qrcodescannerandgenerator.databinding.ActivityScannerBinding
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var scannerView: ZXingScannerView? = null

    private lateinit var binding: ActivityScannerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        //setup toolbar
        binding.scannerToolbar.title = resources.getString(R.string.app_name)
        setSupportActionBar(binding.scannerToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setPermission()
    }

    override fun handleResult(p0: Result?) {

        if (p0.toString().contains("https://") || p0.toString().contains("http://")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(p0.toString()))
            startActivity(intent)
        } else {
            AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setTitle(R.string.title_text)
                .setMessage(p0.toString())
                .setPositiveButton(R.string.copy_text) {
                        dialog, _ ->

                    val clipboard: ClipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip: ClipData = ClipData.newPlainText(R.string.title_text.toString(), p0.toString())
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

    }

    override fun onResume() {
        super.onResume()

        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
    }

    override fun onStop() {
        super.onStop()
        scannerView?.stopCamera()
        onBackPressed()
    }

    private fun setPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        applicationContext,
                        R.string.camera_permission_required_text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}