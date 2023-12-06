package com.example.inventory.common

import java.util.ArrayList

class SnackBarMessage {
    var resId = 0
        private set
    var message: String? = null
        private set
    val formattedMessages = ArrayList<String>()

    constructor(resId: Int) {
        this.resId = resId
    }

    constructor(message: String?) {
        this.message = message
    }

    fun addFormattedMessage(formattedMessage: String) {
        formattedMessages.add(formattedMessage)
    }
}