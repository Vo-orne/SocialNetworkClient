package com.example.myprofile.presentation.utils

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

/**
 * The `GoogleButton` class is a custom button with a Google-style appearance,
 * allowing customization of text, text size, text color, font family, button background, and icon.
 *
 * @param context The application context.
 * @param attrs The set of attributes specified in the layout file.
 * @param defStyleAttr The default style attribute for the button.
 */
class GoogleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val defaultTextSize: Int = 15
    private val defaultTextColor = Color.BLACK
    private var iconPadding: Int = 0

    init {
        // Obtain the set of attributes for the custom button
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoogleButton)

        // Apply custom attributes to the button
        setButtonText(typedArray)
        setButtonTextSize(typedArray)
        setButtonTextColor(typedArray)
        setButtonTextFontFamily(typedArray)
        setButtonBackground(typedArray)
        setButtonIcon(typedArray)
        setButtonIconPadding(typedArray)

        // Recycle the attributes to free up resources
        typedArray.recycle()
    }

    /**
     * Sets the text of the button using the `text` attribute from the layout file.
     */
    private fun setButtonText(typedArray: TypedArray) {
        text = typedArray.getString(R.styleable.GoogleButton_text)
    }

    /**
     * Sets the text size of the button using the `android:textSize` attribute from the layout file.
     */
    private fun setButtonTextSize(typedArray: TypedArray) {
        val customTextSize = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_android_textSize,
            defaultTextSize
        )
        textSize = (customTextSize / 2).toFloat() // Adjust text size
    }

    /**
     * Sets the text color of the button using the `android:textColor` attribute
     * from the layout file.
     */
    private fun setButtonTextColor(typedArray: TypedArray) {
        val customTextColor = typedArray.getColor(
            R.styleable.GoogleButton_android_textColor,
            defaultTextColor
        )
        setTextColor(customTextColor)
    }

    /**
     * Sets the font family of the button text using the `textFontFamily` attribute
     * from the layout file.
     */
    private fun setButtonTextFontFamily(typedArray: TypedArray) {
        val fontFamily = typedArray.getResourceId(R.styleable.GoogleButton_textFontFamily, 0)
        if (fontFamily > 0) {
            typeface = ResourcesCompat.getFont(context, fontFamily)
        }
    }

    /**
     * Sets the background drawable of
     * the button using the `buttonBackground` attribute from the layout file.
     */
    private fun setButtonBackground(typedArray: TypedArray) {
        val customBackgroundResId = typedArray.getResourceId(
            R.styleable.GoogleButton_buttonBackground, R.drawable.frame_button_google
        )
        val customBackground: Drawable? = ContextCompat.getDrawable(context, customBackgroundResId)
        background = customBackground
    }

    /**
     * Sets the icon of the button using the `icon` attribute from the layout file.
     */
    private fun setButtonIcon(typedArray: TypedArray) {
        val customImageResId = typedArray.getResourceId(
            R.styleable.GoogleButton_icon,
            R.drawable.frame_button_google
        )
        val customImage = ContextCompat.getDrawable(context, customImageResId)
        iconPadding = typedArray.getDimensionPixelSize(R.styleable.GoogleButton_iconPaddingLeft, 0)
        val insetDrawable = InsetDrawable(customImage, iconPadding, 0, 0, 0)

        setCompoundDrawablesRelativeWithIntrinsicBounds(insetDrawable, null, null, null)
    }

    /**
     * Sets the padding between the button's icon
     * and text using the `iconTextSpacing` attribute from the layout file.
     */
    private fun setButtonIconPadding(typedArray: TypedArray) {
        val iconTextSpacing = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_iconTextSpacing,
            0
        )
        compoundDrawablePadding = iconPadding + iconTextSpacing
    }
}
