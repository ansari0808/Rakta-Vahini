package com.raktavahini.ui.ai

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raktavahini.R
import com.raktavahini.databinding.ItemChatMessageBinding

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChatViewHolder(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.text
            
            val params = binding.cardMessage.layoutParams as LinearLayout.LayoutParams
            if (message.isUser) {
                params.gravity = Gravity.END
                binding.cardMessage.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.red_primary))
                binding.tvMessage.setTextColor(android.graphics.Color.WHITE)
            } else {
                params.gravity = Gravity.START
                val isDark = (binding.root.context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
                binding.cardMessage.setCardBackgroundColor(if (isDark) android.graphics.Color.parseColor("#2A2D3E") else android.graphics.Color.parseColor("#F0F0F0"))
                binding.tvMessage.setTextColor(if (isDark) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
            }
            binding.cardMessage.layoutParams = params
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }
}
