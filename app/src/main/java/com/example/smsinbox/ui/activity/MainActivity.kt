package com.example.smsinbox.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smsinbox.databinding.ActivityMainBinding
import com.example.smsinbox.roomDB.Dao.smsRepository
import com.example.smsinbox.roomDB.Dao.smsRoomDatabase
import com.example.smsinbox.roomDB.model.SmsReader
import com.example.smsinbox.roomDB.model.SmsSaver
import com.example.smsinbox.ui.adapter.recycler_view_adapter
import com.example.smsinbox.ui.viewmodel.SmsViewModel
import com.example.smsinbox.ui.viewmodel.SmsViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SmsViewModel
    private lateinit var adapter: recycler_view_adapter

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


        val database = smsRoomDatabase.getDatabase(applicationContext)
        val repository = smsRepository(database.sms_dao())
        val viewModelFactory = SmsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SmsViewModel::class.java]

        adapter = recycler_view_adapter(emptyList())
        binding.smsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.smsListRecyclerView.adapter = adapter

        viewModel.allSms.observe(this) { smsList ->
            adapter.setData(smsList.map { smsSaver ->
                SmsReader(
                    info = smsSaver.info,
                    date = smsSaver.date,
                    time = smsSaver.time,
                    title = smsSaver.sender,
                    sender = smsSaver.sender,
                    smsDescription = smsSaver.smsDescription
                )
            })
        }

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

    private fun fetchAndDisplaySms() {
        val smsData = fetchSmsData()
        smsData.forEach { smsReader ->
            val smsSaver = SmsSaver(
                id = 0,
                info = smsReader.info,
                date = smsReader.date,
                time = smsReader.time,
                sender = smsReader.sender,
                smsDescription = smsReader.smsDescription
            )
            viewModel.insertSms(smsSaver)
        }
    }

    private fun fetchSmsData(): List<SmsReader> {
        val smsList = mutableListOf<SmsReader>()
        val uri = Uri.parse("content://sms/inbox")
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val info = it.getString(it.getColumnIndexOrThrow("address"))
                    val dateMillis = it.getLong(it.getColumnIndexOrThrow("date"))
                    val formattedDate =
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateMillis))
                    val formattedTime =
                        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(dateMillis))

                    val title = it.getString(it.getColumnIndexOrThrow("address")) // Sender name
                    val sender = info
                    val smsDescription = it.getString(it.getColumnIndexOrThrow("body"))

                    smsList.add(
                        SmsReader(
                            info = info,
                            date = formattedDate,
                            time = formattedTime,
                            title = title,
                            sender = sender,
                            smsDescription = smsDescription
                        )
                    )
                } while (it.moveToNext())
            }
        }

        return smsList
    }
}
