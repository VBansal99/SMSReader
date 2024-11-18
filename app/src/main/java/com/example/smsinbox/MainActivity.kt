package com.example.smsinbox

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smsinbox.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val requestSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fetchAndDisplaySms()
            } else {
                Toast.makeText(this, "SMS Permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchAndDisplaySms()
        } else {
            requestSmsPermissionLauncher.launch(Manifest.permission.READ_SMS)
        }
    }

    // Function to fetch and display SMS data
    private fun fetchAndDisplaySms() {
        val smsData = fetchSmsData()
        val adapter = recycler_view_adapter(smsData) // Set data to adapter
        binding.smsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.smsListRecyclerView.adapter = adapter
    }

    // Fetch SMS data from the SMS provider (content://sms/inbox)
    private fun fetchSmsData(): List<SmsReader> {
        val smsList = mutableListOf<SmsReader>()
        val uri = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val info = it.getString(it.getColumnIndexOrThrow("address"))
                    val dateMillis = it.getLong(it.getColumnIndexOrThrow("date"))
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateMillis))
                    val formattedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(dateMillis))

                    val title = it.getString(it.getColumnIndexOrThrow("address")) // Static title, can be modified if needed
                    val sender = info
                    val smsDescription = it.getString(it.getColumnIndexOrThrow("body"))   // SMS body content, can be modified if needed

                    smsList.add(SmsReader(info, formattedDate, formattedTime, title, sender, smsDescription))
                } while (it.moveToNext())
            }
        }

        return smsList
    }


}
