package com.example.mobileproject

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


class TransactionAdapter(private val transaction: ArrayList<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    // Holds the views
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val name= itemView.findViewById<TextView>(R.id.textView_transaction_name)
        val category = itemView.findViewById<TextView>(R.id.textView_transaction_category)
        val amount = itemView.findViewById<TextView>(R.id.textView_transaction_amount)
        val date = itemView.findViewById<TextView>(R.id.textView_transaction_date)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_transaction, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val transaction = transaction[position]
        val context = holder.amount.context

        if (transaction.amount >= 0) {
            holder.amount.text = "+ $%.2f".format(transaction.amount)
            holder.amount.setTextColor(context.getColor(R.color.green))
        } else {
            holder.amount.text = "- $%.2f".format(abs(transaction.amount))
            holder.amount.setTextColor(context.getColor(R.color.red))
        }

        holder.name.text = transaction.name
        holder.category.text = transaction.category
        holder.date.text = transaction.date
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return transaction.size
    }

}