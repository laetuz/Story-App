package com.neotica.storyapp.design

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.neotica.storyapp.R

class PasswordCustomView : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearImage: Drawable
    private lateinit var passwordIcon: Drawable
    private lateinit var background: Drawable
    private lateinit var warning: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun showWarning() {
        setButtonDrawables(endOfTheText = clearImage, bottomOfTheText = warning)
    }

    private fun hideWarning() {
        setButtonDrawables(endOfTheText = clearImage)
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = passwordIcon,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event?.x!! < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearImage.intrinsicWidth).toFloat()
                when {
                    event?.x!! > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        clearImage =
                            ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun init() {
        clearImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.selector_password) as Drawable
        background = ContextCompat.getDrawable(context, R.drawable.selector_input) as Drawable
        warning = ContextCompat.getDrawable(context, R.drawable.warning_password) as Drawable
        var passValid: Boolean = false
        setOnTouchListener(this)

        setButtonDrawables(startOfTheText = passwordIcon)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    showClearButton()
                    passValid = if (s.toString().length < 8) {
                        showWarning()
                        false
                    } else {
                        hideWarning()
                        true
                    }
                } else hideClearButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}