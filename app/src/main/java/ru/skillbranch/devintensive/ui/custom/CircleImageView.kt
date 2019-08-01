package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatImageView
import ru.skillbranch.devintensive.R
import kotlin.math.min
import android.graphics.BitmapShader
import android.graphics.Bitmap




class CircleImageView @JvmOverloads constructor(
        context : Context,
        attrs : AttributeSet? = null,
        defStyleAttr : Int = 0
) : AppCompatImageView(context,attrs,defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    // Properties
    private val paint: Paint = Paint().apply { isAntiAlias = true }
    private val paintBorder: Paint = Paint().apply { isAntiAlias = true }
    private val paintBackground: Paint = Paint().apply { isAntiAlias = true }
    private var circleCenter = 0f
    private var heightCircle: Int = 0
    //region Attributes
    private var circleColor: Int = Color.DKGRAY


    private var borderWidth: Float = DEFAULT_BORDER_WIDTH * context.resources.displayMetrics.density
    private var borderColor: Int = DEFAULT_BORDER_COLOR
    //endregion

    // Object used to draw
    private var civImage: Bitmap? = null

    init {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        // Load the styled attributes and set their properties
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0)

            // Init Background Color

            val defaultBorderSize = DEFAULT_BORDER_WIDTH * context.resources.displayMetrics.density
            borderWidth = attributes.getDimension(R.styleable.CircleImageView_cv_borderWidth, defaultBorderSize)
            borderColor = attributes.getColor(R.styleable.CircleImageView_cv_borderColor, Color.WHITE)
            circleColor = borderColor

            attributes.recycle()

            updateBitmap()
        }
    }
    //endregion

    private fun updateBitmap(){
        civImage = drawableToBitmap(drawable)
        if (civImage == null || paintBorder == null || paint == null) return
        civImage?.also {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            val usableWidth = width - (paddingLeft + paddingRight)
            val usableHeight = height - (paddingTop + paddingBottom)

            heightCircle = min(usableWidth, usableHeight)

            circleCenter = (heightCircle - borderWidth * 2) / 2
            paintBorder.color = if (borderWidth == 0f) circleColor else borderColor

            // Center Image in Shader
            val scale: Float
            val dx: Float
            val dy: Float

            when (scaleType) {
                ScaleType.CENTER_CROP -> if (it.width * height > width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                ScaleType.CENTER_INSIDE -> if (it.width * height < width * it.height) {
                    scale = height / it.height.toFloat()
                    dx = (width - it.width * scale) * 0.5f
                    dy = 0f
                } else {
                    scale = width / it.width.toFloat()
                    dx = 0f
                    dy = (height - it.height * scale) * 0.5f
                }
                else -> {
                    scale = 0f
                    dx = 0f
                    dy = 0f
                }
            }

            shader.setLocalMatrix(Matrix().apply {
                setScale(scale, scale)
                postTranslate(dx, dy)
            })

            // Set Shader in Paint
            paint.shader = shader
        }
        invalidate()

    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateBitmap()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        updateBitmap()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updateBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateBitmap()
    }

    override fun getScaleType(): ScaleType =
            super.getScaleType().let { if (it == null || it != ScaleType.CENTER_INSIDE) ScaleType.CENTER_CROP else it }

    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType != ScaleType.CENTER_CROP && scaleType != ScaleType.CENTER_INSIDE) {
            throw IllegalArgumentException(String.format("ScaleType %s not supported. " + "Just ScaleType.CENTER_CROP & ScaleType.CENTER_INSIDE are available for this library.", scaleType))
        } else {
            super.setScaleType(scaleType)
        }
    }
    //endregion

    //region Draw Method
    override fun onDraw(canvas: Canvas) {
        // Check if civImage isn't null
        if (civImage == null) return


        val circleCenterWithBorder = circleCenter + borderWidth

        // Draw Border
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenterWithBorder, paintBorder)
        // Draw Circle background
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenter, paintBackground)
        // Draw CircularImageView
        canvas.drawCircle(circleCenterWithBorder, circleCenterWithBorder, circleCenter, paint)

    }

//    private fun updateShader() {
//        civImage?.also {
//            // Create Shader
//            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//
//            // Center Image in Shader
//            val scale: Float
//            val dx: Float
//            val dy: Float
//
//            when (scaleType) {
//                ScaleType.CENTER_CROP -> if (it.width * height > width * it.height) {
//                    scale = height / it.height.toFloat()
//                    dx = (width - it.width * scale) * 0.5f
//                    dy = 0f
//                } else {
//                    scale = width / it.width.toFloat()
//                    dx = 0f
//                    dy = (height - it.height * scale) * 0.5f
//                }
//                ScaleType.CENTER_INSIDE -> if (it.width * height < width * it.height) {
//                    scale = height / it.height.toFloat()
//                    dx = (width - it.width * scale) * 0.5f
//                    dy = 0f
//                } else {
//                    scale = width / it.width.toFloat()
//                    dx = 0f
//                    dy = (height - it.height * scale) * 0.5f
//                }
//                else -> {
//                    scale = 0f
//                    dx = 0f
//                    dy = 0f
//                }
//            }
//
//            shader.setLocalMatrix(Matrix().apply {
//                setScale(scale, scale)
//                postTranslate(dx, dy)
//            })
//
//            // Set Shader in Paint
//            paint.shader = shader
//
//            // Apply colorFilter
//            paint.colorFilter = civColorFilter
//        }
//
//    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? =
            when (drawable) {
                null -> null
                is BitmapDrawable -> drawable.bitmap
                else -> try {
                    // Create Bitmap object out of the drawable
                    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
    //endregion

    //region Measure Method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measure(widthMeasureSpec)
        val height = measure(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measure(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize // The parent has determined an exact size for the child.
            MeasureSpec.AT_MOST -> specSize // The child can be as large as it wants up to the specified size.
            else -> heightCircle // The parent has not imposed any constraint on the child.
        }
    }
    //endregion
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateBitmap()
    }

    //region getters and setters

    fun getBorderWidth(): Int = (borderWidth / context.resources.displayMetrics.density).toInt()

    fun setBorderWidth(@Dimension dp:Int){
        borderWidth = dp * context.resources.displayMetrics.density
        updateBitmap()
    }

    fun getBorderColor():Int = borderColor

    fun setBorderColor(hex:String){
        borderColor = Color.parseColor(hex)
        circleColor = borderColor
        updateBitmap()
    }

    fun setBorderColor(@ColorRes colorId: Int){
        borderColor = resources.getColor(colorId, context.theme)
        circleColor = borderColor
        updateBitmap()
    }

    //endregion


}