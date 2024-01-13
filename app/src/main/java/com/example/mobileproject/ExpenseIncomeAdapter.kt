package com.example.mobileproject

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.w3c.dom.Text

class ExpenseIncomeAdapter(private val mList: List<ExpenseIncome>) :
    RecyclerView.Adapter<ExpenseIncomeAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){
        val date :TextView = itemView.findViewById(R.id.textView_date_view_Transaction)
        val name:TextView = itemView.findViewById(R.id.textView_name_view_Transaction)
        val category :TextView = itemView.findViewById(R.id.textView_category_view_Transaction)
        val amount :TextView = itemView.findViewById(R.id.textView_amount_view_Transaction)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.incomexpense_list,
        parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.setText(mList[position].date)
        holder.name.setText(mList[position].name)
        holder.category.setText(mList[position].category)
        holder.amount.setText(mList[position].amount)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}