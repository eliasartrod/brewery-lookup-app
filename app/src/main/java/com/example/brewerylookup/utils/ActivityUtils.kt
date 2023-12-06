package com.example.inventory.utils

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.animation.LayoutAnimationController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

object ActivityUtils {
    fun addFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment?,
        frameId: Int
    ) {
        val ft = fragmentManager.beginTransaction()
        ft.add(frameId, fragment!!)
        ft.commit()
    }

    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment?,
        frameId: Int
    ) {
        val ft = fragmentManager.beginTransaction()
        ft.replace(frameId, fragment!!)
        ft.commit()
    }

    /**
     * Creates a fresh stack of fragments using tag as the "name" of the root fragment
     *
     * @param fragmentManager
     * @param fragment        that will be the "root" of the stack
     * @param frameId
     * @param tag             root name
     */
    fun addFragmentWithBackStack(
        fragmentManager: FragmentManager,
        fragment: Fragment?,
        frameId: Int,
        tag: String?
    ) {
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.beginTransaction()
            .replace(frameId, fragment!!)
            .addToBackStack(tag)
            .commit()
    }

    /**
     * Use in conjunction of ActivityUtils.addFragmentWithBackStack with a current
     * root fragment, you can add more fragments to the stack with this
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    fun addFragmentOnTop(
        fragmentManager: FragmentManager,
        fragment: Fragment?,
        frameId: Int
    ) {
        fragmentManager.beginTransaction()
            .replace(frameId, fragment!!)
            .addToBackStack(null)
            .commit()
    }


    fun runLayoutAnimation(recyclerView: RecyclerView, controller: LayoutAnimationController?) {
        val context = recyclerView.context
        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    fun showKeyboard(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    fun hideKeyboard(activity: Activity) {
        if (activity.currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } else {
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    fun showKeyboard(activity: Activity, view: View?) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun uppercaseEditText(target: EditText, text: String, watcher: TextWatcher?) {
        val selection = target.selectionStart
        target.removeTextChangedListener(watcher)
        target.setText(text.uppercase(Locale.getDefault()))
        target.setSelection(selection)
        target.addTextChangedListener(watcher)
    }

    fun fadeView(view: View, show: Boolean) {
        val startAlpha = if (show) 0f else 1f
        val endAlpha = if (show) 1f else 0f
        val animationDuration: Long = 500
        var listener: Animator.AnimatorListener? = null
        if (show) {
            view.visibility = View.VISIBLE
        } else {
            listener = object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    view.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            }
        }
        view.alpha = startAlpha
        view.animate()
            .alpha(endAlpha)
            .setDuration(animationDuration)
            .setListener(listener)
    }

}