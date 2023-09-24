package com.example.myprofile.utils

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
 * The `GoogleButton` class is a custom button that displays text and an icon with a Google-style appearance.
 * It allows using custom styles for the text, text size, text color, font family, button background, and icon.
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

        // Set up custom attributes for the button
        setButtonText(typedArray)
        setButtonTextSize(typedArray)
        setButtonTextColor(typedArray)
        setButtonTextFontFamily(typedArray)
        setButtonBackground(typedArray)
        setButtonIcon(typedArray)
        setButtonIconPadding(typedArray)

        // Recycle the resources used while obtaining the set of attributes
        typedArray.recycle()
    }

    /**
     * Method that sets the text for the button from the `text` attribute in the layout file.
     */
    private fun setButtonText(typedArray: TypedArray) {
        text = typedArray.getString(R.styleable.GoogleButton_text)
    }

    /**
     * Method that sets the text size for the button from the `android:textSize` attribute in the layout file.
     */
    private fun setButtonTextSize(typedArray: TypedArray) {
        val customTextSize = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_android_textSize,
            defaultTextSize
        )
        textSize = (customTextSize / 2).toFloat()
    }

    /**
     * Method that sets the text color for the button from the `android:textColor` attribute in the layout file.
     */
    private fun setButtonTextColor(typedArray: TypedArray) {
        val customTextColor = typedArray.getColor(
            R.styleable.GoogleButton_android_textColor,
            defaultTextColor
        )
        setTextColor(customTextColor)
    }

    /**
     * Method that sets the font family for the button text from the `textFontFamily` attribute in the layout file.
     */
    private fun setButtonTextFontFamily(typedArray: TypedArray) {
        val fontFamily = typedArray.getResourceId(R.styleable.GoogleButton_textFontFamily, 0)
        if (fontFamily > 0) {
            typeface = ResourcesCompat.getFont(context, fontFamily)
        }
    }

    /**
     * Method that sets the background for the button from the `buttonBackground` attribute in the layout file.
     */
    private fun setButtonBackground(typedArray: TypedArray) {
        val customBackgroundResId = typedArray.getResourceId(
            R.styleable.GoogleButton_buttonBackground, R.drawable.frame_button_google
        )
        val customBackground: Drawable? = ContextCompat.getDrawable(context, customBackgroundResId)
        background = customBackground
    }

    /**
     * Method that sets the icon for the button from the `icon` attribute in the layout file.
     */
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

    /**
     * Method that sets the distance between the button's icon and text from the `iconTextSpacing` attribute in the layout file.
     */
    private fun setButtonIconPadding(typedArray: TypedArray) {
        // Get the distance between the button's icon and text
        val iconTextSpacing = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_iconTextSpacing,
            0
        )

        // Set the distance between the button icon and text
        compoundDrawablePadding = iconPadding + iconTextSpacing
    }
}