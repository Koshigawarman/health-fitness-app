package com.example.fitsync

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTaskName: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var btnSaveTask: Button
    private var taskDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Set status bar color
        window.setStatusBarColor(resources.getColor(R.color.custom_color_primary, null))

        // Initialize views
        etTaskName = findViewById(R.id.etTaskName)
        etTaskDate = findViewById(R.id.etTaskDate)
        btnSaveTask = findViewById(R.id.btnSaveTask)

        // Check if editing an existing task
        val existingTask = intent.getStringExtra("task")
        val existingDate = intent.getStringExtra("date")
        if (existingTask != null) {
            etTaskName.setText(existingTask)
        }
        if (existingDate != null) {
            etTaskDate.setText(existingDate)
        }

        // Set a click listener on the due date EditText to open the DatePickerDialog
        etTaskDate.setOnClickListener {
            showDatePickerDialog()
        }

        // Save task button click listener
        btnSaveTask.setOnClickListener {
            val taskName = etTaskName.text.toString()
            val taskDateText = etTaskDate.text.toString()
            if (taskName.isNotEmpty() && taskDateText.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("task", taskName)
                resultIntent.putExtra("date", taskDateText)
                setResult(RESULT_OK, resultIntent)
                finish()  // Close the activity and return the result
            }
        }
    }

    // Function to show the DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // Format the selected date and set it in the EditText
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                taskDate = dateFormat.format(calendar.time)
                etTaskDate.setText(taskDate)
            },
            // Set the current date in the DatePickerDialog
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }
}
