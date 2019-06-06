package com.example.conferencerommapp.Helper

//import android.annotation.SuppressLint
//import android.content.Context
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CheckBox
//import android.widget.Toast
//import androidx.annotation.Nullable
//import androidx.recyclerview.widget.RecyclerView
//import butterknife.BindView
//import butterknife.ButterKnife
//import butterknife.Optional
//import com.example.conferencerommapp.Model.EmployeeList
//import com.example.conferencerommapp.R
//
//
//class CheckBoxAdapter(
//    var employee: ArrayList<EmployeeList>,
//    var checkedEmployee: ArrayList<EmployeeList>,
//    var context: Context
//) : RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>() {
//
//    companion object {
//        var count = 0
//    }
//
//    /**
//     * attach a view for the recyclerview items
//     */
//    @SuppressLint("InflateParams")
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_alertdialog, null)
//        return ViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val employee = employee[position]
//        holder.myCheckBox.text = employee.name
//        holder.myCheckBox.isChecked = employee.isSelected!!
//        if (employee.isSelected!! && !checkedEmployee.contains(employee)) {
//            checkedEmployee.add(employee)
//        }
//
//        /**
//         * get the selected employee by user
//         */
//        holder.setItemClickListener(object : ViewHolder.ItemClickListener {
//            override fun onItemClick(v: View, pos: Int) {
//                val myCheckBox = v as CheckBox
//                val currentEmployee = this@CheckBoxAdapter.employee[pos]
//                if (myCheckBox.isChecked) {
//                    if(count < 5) {
//                        currentEmployee.isSelected = true
//                        checkedEmployee.add(currentEmployee)
//                    }
//                    else {
//                        Toast.makeText(context, "you can not select more than capacity", Toast.LENGTH_SHORT).show()
//                    }
//                } else if (!myCheckBox.isChecked) {
//                    currentEmployee.isSelected = false
//                    checkedEmployee.remove(currentEmployee)
//                }
//            }
//        })
//    }
//
//    /**
//     * function will return the number of items present in the recyclerview
//     */
//    override fun getItemCount(): Int {
//        return employee.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//
//        lateinit var myItemClickListener: ItemClickListener
//
//        init {
//            ButterKnife.bind(this, itemView)
//            myCheckBox.setOnClickListener(this)
//        }
//        @Nullable
//        @BindView(R.id.checkBox)
//        lateinit var myCheckBox: CheckBox
//
//
//        fun setItemClickListener(ic: ItemClickListener) {
//            this.myItemClickListener = ic
//        }
//
//        override fun onClick(v: View) {
//            this.myItemClickListener.onItemClick(v, layoutPosition)
//        }
//
//        interface ItemClickListener {
//            fun onItemClick(v: View, pos: Int)
//        }
//    }
//
//    /**
//     * function will update recycler view items according to the filtered data
//     */
//    fun filterList(filteredNames: ArrayList<EmployeeList>) {
//        this.employee = filteredNames
//        notifyDataSetChanged()
//    }
//
//    /**
//     * return the checkedEmployees list selected by the user
//     */
//    fun getList(): ArrayList<EmployeeList> {
//        return checkedEmployee
//    }
//}
