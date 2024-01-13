package com.example.mobileproject

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_add_expense.*
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        db = Firebase.firestore

        val expenseCategoryList = resources.getStringArray(R.array.expense_category_list)

        category_auto_complete_add_expense.setAdapter(
            ArrayAdapter(
                this,
                R.layout.list_category,
                expenseCategoryList
            )
        )


        name_input_addTransaction.addTextChangedListener {
            if (it!!.isNotEmpty())
                name_layout_addTransaction.error = null
        }

        amount_input_addTransaction.addTextChangedListener {
            if (it!!.isNotEmpty())
                amount_layout_addTransaction.error = null
        }

        date_input_addTransaction.addTextChangedListener {
            if (it!!.isNotEmpty())
                date_layout_addTransaction.error = null
        }

        category_auto_complete_add_expense.addTextChangedListener {
            if (it!!.isNotEmpty())
                category_layout_addTransaction.error = null
        }


        date_input_addTransaction.setOnClickListener {
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
                    date_input_addTransaction.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        button_add_transaction.setOnClickListener {
            val name = name_input_addTransaction.text.toString()
            val amount = amount_input_addTransaction.text.toString().toDoubleOrNull()
            val date = date_input_addTransaction.text.toString()
            val category = category_auto_complete_add_expense.text.toString()

            var isValid = true

            if(name.isEmpty()){
                name_layout_addTransaction.error = "Name cannot be empty"
                isValid = false
            }

            if(amount == null || amount <= 0){
                amount_layout_addTransaction.error = "Amount must be greater than 0"
                isValid = false
            }

            if(date.isEmpty()){
                date_layout_addTransaction.error = "Date cannot be empty"
                isValid = false
            }

            if(category.isEmpty()){
                category_layout_addTransaction.error = "Category cannot be empty"
                isValid = false
            }

            if(isValid){
                if (amount != null) {
                    insertTransaction(name, amount * -1, date, category)
                }
                finish()
            }
        }

        close_button_addTransaction.setOnClickListener {
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