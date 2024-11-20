package ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smsinbox.databinding.ItemRecyclerViewBinding
import roomDB.model.SmsReader

class recycler_view_adapter(private var itemList: List<SmsReader>) :
    RecyclerView.Adapter<recycler_view_adapter.ModelViewHolder>() {

    fun setData(newList: List<SmsReader>) {
        itemList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val sms = itemList[position]
        with(holder.binding) {
            tvInfo.text = sms.info
            tvDate.text = sms.date
            tvTime.text = sms.time
            tvTitle.text = sms.title
            tvSender.text = sms.sender
            tvMessage.text = sms.smsDescription
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ModelViewHolder(val binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
