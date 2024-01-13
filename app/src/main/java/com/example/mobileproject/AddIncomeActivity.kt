package com.example.mobileproject

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_income.*
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddIncomeActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        db = Firebase.firestore

        val incomeCategoryList = resources.getStringArray(R.array.income_category_list)

        category_auto_complete_add_income.setAdapter(
            ArrayAdapter(
                this,
                R.layout.list_category,
                incomeCategoryList
            )
        )


        name_input_add_income.addTextChangedListener {
            if (it!!.isNotEmpty())
                name_layout_add_income.error = null
        }

        amount_input_add_income.addTextChangedListener {
            if (it!!.isNotEmpty())
                amount_layout_add_income.error = null
        }

        date_input_add_income.addTextChangedListener {
            if (it!!.isNotEmpty())
                date_layout_add_income.error = null
        }

        category_auto_complete_add_income.addTextChangedListener {
            if (it!!.isNotEmpty())
                category_layout_add_income.error = null
        }


        date_input_add_income.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val monthString = (monthOfYear + 1).toString().padStart(2, '0')
                    val dayString = dayOfMonth.toString().padStart(2, '0')
                    val date = "$year-$monthString-$dayString"
                    date_input_add_income.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        button_add_income.setOnClickListener {
            val name = name_input_add_income.text.toString()
            val amount = amount_input_add_income.text.toString().toDoubleOrNull()
            val date = date_input_add_income.text.toString()
            val category = category_auto_complete_add_income.text.toString()

            var isValid = true

            if(name.isEmpty()){
                name_layout_add_income.error = "Name cannot be empty"
                isValid = false
            }

            if(amount == null || amount <= 0){
                amount_layout_add_income.error = "Amount must be greater than 0"
                isValid = false
            }

            if(date.isEmpty()){
                date_layout_add_income.error = "Date cannot be empty"
                isValid = false
            }

            if(category.isEmpty()){
                category_layout_add_income.error = "Category cannot be empty"
                isValid = false
            }

            if(isValid){
                if (amount != null) {
                    insertTransaction(name, amount, date, category)
                }
                finish()
            }
        }

        close_button_add_income.setOnClickListener {
            finish()
        }}

    // create a insertTransaction function here
    // for storing the transaction into the firebase database

    private fun insertTransaction(name: String, amount: Double, date: String, category: String) {

        val transaction = hashMapOf(
            "name" to name,
            "amount" to amount,
            "date" to date,
            "category" to category
        )

        db.collection("transaction")
            .add(transaction)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

}