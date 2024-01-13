package com.example.mobileproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_expense.*

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    lateinit var db: FirebaseFirestore

    private lateinit var transactions: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db= Firebase.firestore
        drawerLayout = findViewById(R.id.drawerLayout)
        val drawerLayout:DrawerLayout = findViewById(R.id.drawerLayout)
        val navView:NavigationView = findViewById(R.id.navView)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener{
            when(it.itemId){
//                R.id.Home-> replaceFragment(ViewTransactionFragment(), it.title.toString())
                R.id.ViewTransaction->  goToExpenseIncomeActivity()
//                R.id.AddTransaction-> replaceFragment(ViewTransactionFragment(), it.title.toString())
//                R.id.Report-> replaceFragment(ReportFragment(), it.title.toString())
            }
            true
        }

        transactions = arrayListOf()

        getTransactions()
        updateDashboard()

        button_main_add_transaction.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_add_transaction -> {
                        val intent = Intent(this, AddExpenseActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_add_income -> {
                        val intent = Intent(this, AddIncomeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        textView_main_viewall.setOnClickListener {
            val intent = Intent(this, ExpenseIncomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getTransactions() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_transactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        db.collection("transaction")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("MainActivity", "Listen failed", error)
                    return@addSnapshotListener
                }

                transactions.clear()

                for (document in snapshot!!) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                    val name = document.data["name"].toString()
                    val amount = document.data["amount"].toString().toDouble()
                    val date = document.data["date"].toString()
                    val category = document.data["category"].toString()
                    val transaction = Transaction(name, amount, date, category)
                    transactions.add(transaction)
                }

                recyclerView.adapter = TransactionAdapter(transactions)
                transactionAdapter = TransactionAdapter(transactions)
                transactionLayoutManager = LinearLayoutManager(this)

                updateDashboard()
            }
    }




    private fun goToExpenseIncomeActivity(){
        val intent = Intent(this, ExpenseIncomeActivity::class.java)
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("SetTextI18n")
    private fun updateDashboard() {
        val totalAmount = transactions.sumOf { it.amount }
        val incomeAmount = transactions.filter { it.amount >= 0 }.sumOf { it.amount }
        val expenseAmount = transactions.filter { it.amount < 0 }.sumOf { it.amount } * -1

        val textViewTotalAmount = findViewById<TextView>(R.id.textView_main_balance_placeholder)
        val textViewIncomeAmount = findViewById<TextView>(R.id.textView_main_income_placeholder)
        val textViewExpenseAmount = findViewById<TextView>(R.id.textView_main_expense_placeholder)

        textViewTotalAmount.text = "$%.2f".format(totalAmount)
        textViewIncomeAmount.text = "$%.2f".format(incomeAmount)
        textViewExpenseAmount.text = "$%.2f".format(expenseAmount)
    }
}
