package com.techetronventures.moviedb.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.techetronventures.moviedb.data.model.StockData
import com.techetronventures.moviedb.ui.MainActivity
import com.techetronventures.moviedb.utils.Constants
import com.techetronventures.moviedb.viewmodel.GraphViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GraphFragment : Fragment() {

    private lateinit var stockDataList: List<StockData>
    private val graphViewModel: GraphViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return SpikeGraphView(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stockDataList = graphViewModel.readCSVFile(requireContext())
    }

    inner class SpikeGraphView(context: Context) : View(context) {

        private val margin = 100
        private val paddingTop = 100
        private val paddingBottom = 100
        private val paint = Paint()
        private val sharedPreferences = requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)

            if (canvas == null) return

            val width = width.toFloat()
            val height = height.toFloat()

            // Calculating the maximum and minimum values for scaling
            val maxClose = stockDataList.map { it.close }.maxOrNull() ?: 0f
            val minClose = stockDataList.map { it.close }.minOrNull() ?: 0f

            // Calculating scaling factors
            val minTimestamp = stockDataList.map { it.timestamp.time }.minOrNull() ?: 0L
            val maxTimestamp = stockDataList.map { it.timestamp.time }.maxOrNull() ?: 0L
            val xScale = (width - 2 * margin) / (maxTimestamp - minTimestamp)
            val yScale = (height - paddingTop - paddingBottom) / (maxClose - minClose)

            paint.color = if (sharedPreferences.getBoolean(Constants.KEY_IS_DARK_THEME, false)) Color.WHITE else Color.BLACK
            paint.strokeWidth = 2f

            // Draw the x-axis
            canvas.drawLine(margin.toFloat(), height - paddingBottom, width - margin, height - paddingBottom, paint)

            // Draw the y-axis
            canvas.drawLine(margin.toFloat(), paddingTop.toFloat(), margin.toFloat(), height - paddingBottom, paint)

            // Label the X-axis as "timestamp"
            paint.textSize = 30f // Adjust the text size as needed
            canvas.drawText("Timestamp", (width / 2.3).toFloat(), height - paddingBottom + 50, paint)

            // Label the Y-axis as "close"
            canvas.save()
            canvas.rotate(-90f, (margin / 2).toFloat(), height / 2)
            canvas.drawText("Close", (margin / 2).toFloat(), height / 2, paint)
            canvas.restore()

            val shader = LinearGradient(
                margin.toFloat(),
                paddingTop.toFloat(),
                margin.toFloat(),
                height - paddingBottom,
                Color.RED,
                Color.YELLOW,
                Shader.TileMode.CLAMP
            )

            paint.shader = shader
            paint.strokeWidth = 4f

            // Initialize variables for the starting point of the line
            var startX: Float = 0f
            var startY: Float = 0f

            // Plotting the graph using "timestamp" and "close" values and connecting the points with lines
            for ((index, stockData) in stockDataList.withIndex()) {
                val x = margin + (stockData.timestamp.time - minTimestamp) * xScale
                val y = height - paddingBottom - (stockData.close - minClose) * yScale

                if (index > 0) {
                    // Drawing a line to connect the current and previous data points
                    canvas.drawLine(startX, startY, x, y, paint)
                }

                // Set the current point as the starting point for the next line segment
                startX = x
                startY = y
            }

            /**
             * This bit will plot the points in the graph too.
             * But I am leaving it out to show a good gradient colored path
             */
//            paint.shader = null
//            paint.color = if (sharedPreferences.getBoolean(Constants.KEY_IS_DARK_THEME, false)) Color.WHITE else Color.BLACK
//            paint.strokeWidth = 4f
//            for (stockData in stockDataList) {
//                val x = margin + (stockData.timestamp.time - minTimestamp) * xScale
//                val y = height - paddingBottom - (stockData.close - minClose) * yScale
//
//                // Draw a point at the data position
//                canvas.drawPoint(x, y, paint)
//            }
        }
    }
}