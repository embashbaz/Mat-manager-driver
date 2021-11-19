package com.example.matatumanageruser.ui.other

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.matatumanageruser.data.Expense
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.databinding.GeneralRecyclerItemBinding

class DefaultRecyclerAdapter (onClick: (Any) -> Unit) :
    RecyclerView.Adapter<DefaultRecyclerAdapter.ViewHolder>() {

    val mOnclick = onClick
    val allItems = ArrayList<Any>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, mOnclick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allItems.get(position)

        holder.bind(item)

    }

    override fun getItemCount() = allItems.size


    fun setData(items: ArrayList<Any>) {
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()

    }

    class ViewHolder(val binding: GeneralRecyclerItemBinding, onClick: (Any) -> Unit):
        RecyclerView.ViewHolder(binding.root){

        var item : Any? = null

        init {
            binding.root.setOnClickListener {
                onClick(item!!)
            }
        }

        fun bind(item: Any) {
            if (item is Expense){
                this.item = item

                binding.itemPrimaryTextList.text = item.reason
                binding.itemSecondaryTextList.text = item.date
                binding.itemTerciaryTextList.text = item.amount.toString()

            }else if (item is Issue){
                this.item = item

                binding.itemPrimaryTextList.text = item.reason
                binding.itemSecondaryTextList.text = item.busPlate

            }else if(item is Trip){

                this.item = item

                binding.itemPrimaryTextList.text = item.busPlate
                binding.itemSecondaryTextList.text = item.date
                binding.itemTerciaryTextList.text = item.moneyCollected.toString()

            }else if(item is Statistics){
                this.item = item

                binding.itemPrimaryTextList.text = item.busPlate
                binding.itemSecondaryTextList.text = item.timeStarted
            }
        }

        companion object {
            fun from(parent: ViewGroup, onClick: (Any ) -> Unit) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)

                return  ViewHolder(GeneralRecyclerItemBinding.inflate(layoutInflater, parent, false), onClick)
            }
        }
    }
}