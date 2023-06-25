package com.example.myprofile.ui.auth

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.myprofile.R

class GoogleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val defaultTextSize: Int = 15
    private val defaultTextColor = Color.BLACK
    private var iconPadding: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoogleButton)

        setButtonText(typedArray)
        setButtonTextSize(typedArray)
        setButtonTextColor(typedArray)
        setButtonTextFontFamily(typedArray)
        setButtonBackground(typedArray)
        setButtonIcon(typedArray)
        setButtonIconPadding(typedArray)

        typedArray.recycle()
    }

    private fun setButtonText(typedArray: TypedArray) {
        text = typedArray.getString(R.styleable.GoogleButton_text)
    }

    private fun setButtonTextSize(typedArray: TypedArray) {
        val customTextSize = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_android_textSize,
            defaultTextSize
        )
        textSize = (customTextSize / 2).toFloat()
    }

    private fun setButtonTextColor(typedArray: TypedArray) {
        val customTextColor = typedArray.getColor(
            R.styleable.GoogleButton_android_textColor,
            defaultTextColor
        )
        setTextColor(customTextColor)
    }

    private fun setButtonTextFontFamily(typedArray: TypedArray) {
        val fontFamily = typedArray.getResourceId(R.styleable.GoogleButton_textFontFamily, 0)
        if (fontFamily > 0) {
            typeface = ResourcesCompat.getFont(context, fontFamily)
        }
    }

    private fun setButtonBackground(typedArray: TypedArray) {
        val customBackgroundResId = typedArray.getResourceId(
            R.styleable.GoogleButton_buttonBackground, R.drawable.frame_button_google
        )
        val customBackground: Drawable? = ContextCompat.getDrawable(context, customBackgroundResId)
        background = customBackground
    }

    private fun setButtonIcon(typedArray: TypedArray) {
        val customImageResId = typedArray.getResourceId(
            R.styleable.GoogleButton_icon,
            R.drawable.frame_button_google
        )
        val customImage = ContextCompat.getDrawable(context, customImageResId)
        iconPadding =
            typedArray.getDimensionPixelSize(R.styleable.GoogleButton_iconPaddingLeft, 0)
        val insetDrawable = InsetDrawable(customImage, iconPadding, 0, 0, 0)

        setCompoundDrawablesRelativeWithIntrinsicBounds(insetDrawable, null, null, null)
    }

    private fun setButtonIconPadding(typedArray: TypedArray) {
        // Getting the distance between the button's icon and text
        val iconTextSpacing = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_iconTextSpacing,
            0
        )

        // Setting the distance between the button icon and text
        compoundDrawablePadding = iconPadding + iconTextSpacing
    }
}