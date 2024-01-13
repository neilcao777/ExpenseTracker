package com.example.mobileproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExpenseIncomeActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    lateinit var expenseIncomeList: ArrayList<ExpenseIncome>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenseincome)
        db = Firebase.firestore

        recyclerView = findViewById(R.id.recyclerView_ExpenseIncome)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the expenseIncomeList
        expenseIncomeList = ArrayList()
        val incomeButton = findViewById<Button>(R.id.incomeButton_ViewTransaction)
        val expenseButton = findViewById<Button>(R.id.ExpensesButton_ViewTransaction)
        incomeButton.setOnClickListener {
            getIncome()
        }
        expenseButton.setOnClickListener {
            getExpense()
        }
        // this is for loading initial data
        getExpense()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    fun setUpRecyclerView(expenseIncomeList : ArrayList<ExpenseIncome>){
        recyclerView.adapter = ExpenseIncomeAdapter(expenseIncomeList)
    }

    fun getIncome(){
        db.collection("transaction")
            .whereGreaterThan("amount", 0) // Query documents where amount > 0
            .get()
            .addOnSuccessListener { result ->
                Log.d("Get Income", "Number of documents: ${result.size()}")
                expenseIncomeList.clear()
                for (document in result) {
                    Log.d("Get Income", "${document.id} => ${document.data}")
                    val newExpenseIncome = ExpenseIncome(document.data["date"].toString(), document.data["name"].toString(),document.data["category"].toString(), document.data["amount"].toString())
                    expenseIncomeList.add(newExpenseIncome)
                }
                setUpRecyclerView(expenseIncomeList)
            }
            .addOnFailureListener {exception ->
                Log.w("GET Income", "Error getting documents.", exception)
            }
    }

    fun getExpense(){
        db.collection("transaction")
            .whereLessThan("amount", 0) // Query documents where amount < 0
            .get()
            .addOnSuccessListener { result ->
                Log.d("Get Expense", "Number of documents: ${result.size()}")
                expenseIncomeList.clear()
                for (document in result) {
                    Log.d("Get Expense", "${document.id} => ${document.data}")
                    val newExpenseIncome = ExpenseIncome(document.data["date"].toString(), document.data["name"].toString(),document.data["category"].toString(), document.data["amount"].toString())
                    expenseIncomeList.add(newExpenseIncome)
                }
                setUpRecyclerView(expenseIncomeList)
            }
            .addOnFailureListener {exception ->
                Log.w("GET Expense", "Error getting documents.", exception)
            }
    }
}
