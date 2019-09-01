package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.extensions.truncate
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel(){
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()){chats->
        return@map chats
                .filter { !it.isArchived }
                .map{ it.toChatItem() }
                .sortedBy { it.id.toInt() }
    }
    private val archive = Transformations.map(chatRepository.loadChats()){chats->
        val archChats  = chats.filter { it.isArchived }
        val archMessages = archChats.flatMap { chat -> chat.messages }

        return@map if (archChats.isEmpty()) {
            listOf()
        }else {
            val messageCount = archMessages.filter { !it.isReaded }.size
            val lastMessage = archMessages.maxBy { it.id.toInt() }
            listOf(ChatItem(
                    "-999999",
                    null,
                    "",
                    App.applicationContext().getString(R.string.archive_chat_title),
                    when(lastMessage){
                        is TextMessage -> lastMessage.text!!
                        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото"
                        else -> ""
                    },
                    messageCount,
                    lastMessage?.date?.shortFormat(),
                    false,
                    ChatType.ARCHIVE,
                    lastMessage?.from?.firstName
            ))
        }
    }

    fun getChatData() : LiveData<List<ChatItem>>{

        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chatList = chats.value!!
            val archList = archive.value?: listOf()

            val merged = arrayListOf<ChatItem>()
            merged.addAll(archList)
            merged.addAll(chatList)

            result.value = if(queryStr.isEmpty()) merged
            else merged.filter { it.title.contains(queryStr, true) }
        }
        result.addSource(chats){filterF.invoke()}
        result.addSource(archive){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }


    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}