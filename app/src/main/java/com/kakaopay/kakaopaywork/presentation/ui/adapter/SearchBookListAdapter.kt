package com.kakaopay.kakaopaywork.presentation.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kakaopay.kakaopaywork.R
import com.kakaopay.kakaopaywork.data.entity.ClickData
import com.kakaopay.kakaopaywork.data.entity.DocumentsEntity
import com.kakaopay.kakaopaywork.databinding.ViewItemSearchBookListBinding

class SearchBookListAdapter(
    private val onClick: (ClickData) -> Unit
) : PagingDataAdapter<DocumentsEntity, SearchBookListAdapter.ListHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<DocumentsEntity>() {
            override fun areItemsTheSame(
                oldItem: DocumentsEntity,
                newItem: DocumentsEntity
            ): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(
                oldItem: DocumentsEntity,
                newItem: DocumentsEntity
            ): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.contents == newItem.contents &&
                        oldItem.datetime == newItem.datetime &&
                        oldItem.publisher == newItem.publisher
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val binding = ViewItemSearchBookListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListHolder(binding)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        getItem(position)?.let {
            holder.setData(it, position)
        }
    }

    inner class ListHolder(private val binding: ViewItemSearchBookListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal fun setData(item: DocumentsEntity, position: Int) {
            binding.item = item

            if (item.salePrice <= 0) {
                binding.tvPrice.run {
                    paintFlags = 0
                    setTextColor(ContextCompat.getColor(binding.root.context, R.color.teal_700))
                    textSize = 16f
                }
                binding.tvSalePrice.isVisible = false
            } else {
                binding.tvPrice.run {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setTextColor(ContextCompat.getColor(binding.root.context, R.color.charcoal))
                    textSize = 14f
                }
                binding.tvSalePrice.isVisible = true
            }

            binding.root.setOnClickListener {
                val clickData = ClickData(binding.ivItem, item, position)
                onClick.invoke(clickData)
            }
        }
    }

    fun setItemChanged(position: Int) {
        notifyItemChanged(position)
    }
}