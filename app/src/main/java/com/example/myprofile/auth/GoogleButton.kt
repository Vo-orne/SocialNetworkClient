package com.example.myprofile.auth

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.myprofile.R

class GoogleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr) {
    private var iconGravity: Int = 0
    private var iconSize: Int = 0
    private var icon: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.GoogleButton, defStyleAttr, 0
        )

        val buttonColor = typedArray.getColor(
            R.styleable.GoogleButton_buttonColor, ContextCompat.getColor(context, R.color.white)
        )
        icon = typedArray.getDrawable(
            R.styleable.GoogleButton_icon
        )
        val text = typedArray.getString(
            R.styleable.GoogleButton_text
        )
        val textSize = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_android_textSize, 0
        ).toFloat()
        val textAllCaps = typedArray.getBoolean(
            R.styleable.GoogleButton_android_textAllCaps, false
        )
        val textColor = typedArray.getColor(
            R.styleable.GoogleButton_android_textColor, ContextCompat.getColor(context, R.color.black)
        )
        val buttonBackground = typedArray.getResourceId(
            R.styleable.GoogleButton_buttonBackground, R.drawable.frame_button_google
        )
        iconGravity = typedArray.getInt(
            R.styleable.GoogleButton_iconGravityX, 0
        )
        iconSize = typedArray.getDimensionPixelSize(
            R.styleable.GoogleButton_iconSizeX, 0
        )

        typedArray.recycle()

        setBackgroundColor(buttonColor)
        setBackgroundResource(buttonBackground)
        this.text = text
        setTextSize(textSize)
        isAllCaps = textAllCaps
        setTextColor(textColor)

        updateIconGravity(icon, text)
    }

    private fun updateIconGravity(icon: Drawable?, text: CharSequence?) {
        val compoundDrawables = compoundDrawablesRelative
        val iconDrawable = icon?.let { DrawableCompat.wrap(it).mutate() }

        val textBounds = Rect()
        paint.getTextBounds(text.toString(), 0, text.toString().length, textBounds)
        val textWidth = textBounds.width()

        when (iconGravity) {
            GRAVITY_START -> setCompoundDrawablesRelativeWithIntrinsicBounds(
                iconDrawable, null, null, null
            )
            GRAVITY_END -> setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, null, iconDrawable, null
            )
            GRAVITY_TEXT_START -> {
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    iconDrawable, null, null, null
                )
                setPadding(iconSize, 0, 0, 0)
            }
            GRAVITY_TEXT_END -> {
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, iconDrawable, null
                )
                setPadding(0, 0, iconSize, 0)
            }
        }
        compoundDrawablePadding = iconSize + (textWidth / 2)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth + compoundDrawablePadding + iconSize
        setMeasuredDimension(width, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!TextUtils.isEmpty(text)) {
            val textBounds = Rect()
            paint.getTextBounds(text.toString(), 0, text.toString().length, textBounds)
            val textWidth = textBounds.width()

            val compoundDrawables = compoundDrawablesRelative
            val iconDrawable = icon?.let { DrawableCompat.wrap(it).mutate() }
            val iconPadding = compoundDrawablePadding

            val totalDrawableWidth = if (iconDrawable != null) {
                iconDrawable.intrinsicWidth + iconPadding
            } else {
                0
            }

            val textX = (width - textWidth - totalDrawableWidth) / 2
            val textY = (height + textBounds.height()) / 2
            canvas.drawText(text.toString(), textX.toFloat(), textY.toFloat(), paint)
        }
    }

    companion object {
        private const val GRAVITY_START = 0
        private const val GRAVITY_END = 1
        private const val GRAVITY_TEXT_START = 2
        private const val GRAVITY_TEXT_END = 3
    }
}