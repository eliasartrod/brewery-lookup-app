package com.example.inventory.common

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    fun setActionBarTitle(resId: Int) {
        (activity as BaseActivity?)?.setActionBarTitle(resId)
    }

    fun setActionBarTitle(title: String?) {
        if (title != null) {
            (activity as BaseActivity?)?.setActionBarTitle(title)
        }
    }

    fun showSnackBar(event: Event<SnackBarMessage>?) {
        val message = event?.contentIfNotHandled
        message?.let { showSnackBar(it) }
    }

    fun showSnackBar(message: SnackBarMessage) {
        val snkMessage = if (message.message != null) {
            message.message
        } else {
            getString(message.resId, *message.formattedMessages.toTypedArray())
        }
        val snackbar = Snackbar.make(getRoot()!!, snkMessage!!, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    abstract fun getRoot(): View?
}