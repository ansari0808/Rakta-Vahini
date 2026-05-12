package com.raktavahini.ui.ai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raktavahini.R
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class AiViewModel(application: Application) : AndroidViewModel(application) {

    private val _messages = MutableLiveData<List<ChatMessage>>(listOf(
        ChatMessage(getApplication<Application>().getString(R.string.ai_welcome), false)
    ))
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Force use of v1 stable API to avoid v1beta 404 errors
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "YOUR_API_KEY",
        requestOptions = RequestOptions(apiVersion = "v1")
    )

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("You are a helpful assistant for a blood donation app called Rakta-Vahini. Answer questions related to blood donation and health.") },
            content(role = "model") { text("Understood. I will help users with blood donation and health-related questions.") }
        )
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = ChatMessage(text, true)
        val currentList = _messages.value.orEmpty().toMutableList()
        currentList.add(userMessage)
        _messages.value = currentList

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = chat.sendMessage(text)
                val aiMessage = ChatMessage(response.text ?: getApplication<Application>().getString(R.string.ai_error), false)
                
                val updatedList = _messages.value.orEmpty().toMutableList()
                updatedList.add(aiMessage)
                _messages.value = updatedList
            } catch (e: Exception) {
                val errorText = when {
                    e.message?.contains("404") == true -> "Error: Model 'gemini-1.5-flash' not found. Please ensure the 'Generative Language API' is ENABLED in your Google Cloud Console for this project."
                    e.message?.contains("403") == true -> "Error: Access denied. This key might not have permission for Gemini. Try creating a NEW key at aistudio.google.com."
                    else -> getApplication<Application>().getString(R.string.ai_error)
                }
                val errorMessage = ChatMessage(errorText, false)
                val updatedList = _messages.value.orEmpty().toMutableList()
                updatedList.add(errorMessage)
                _messages.value = updatedList
            } finally {
                _isLoading.value = false
            }
        }
    }
}
