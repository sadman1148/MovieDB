package com.techetronventures.moviedb.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.model.StockData
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale

class GraphFragment : Fragment() {

    private lateinit var stockDataList: List<StockData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return SpikeGraphView(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stockDataList = readCSVFile()
    }

    private fun readCSVFile(): List<StockData> {

        val stockDataList = mutableListOf<StockData>()
        val dateFormat = SimpleDateFormat("yyyy-MM-d H:mm:ss",Locale.getDefault())

        try {
            // Open the CSV file
            val inputStream: InputStream = resources.openRawResource(R.raw.stock)
            val reader = BufferedReader(InputStreamReader(inputStream))
            
            // Skip the header row
            reader.readLine()

            var line: String? = reader.readLine()

            // Read and parse the CSV data
            while (line != null) {
                val values = line.split(",")
                if (values.size == 6) {
                    val timestamp = dateFormat.parse(values[0].trim())
                    val open = values[1].trim().toFloat()
                    val high = values[2].trim().toFloat()
                    val low = values[3].trim().toFloat()
                    val close = values[4].trim().toFloat()
                    val volume = values[5].trim().toFloat()
                    stockDataList.add(StockData(timestamp!!, open, high, low, close, volume))
                }
                line = reader.readLine()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stockDataList
    }

    inner class SpikeGraphView(context: Context) : View(context) {

        private val margin = 100
        private val paddingTop = 100
        private val paddingBottom = 100
        private val paint = Paint()

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

            paint.color = Color.GRAY
            paint.strokeWidth = 2f

            // Draw the x-axis
            canvas.drawLine(margin.toFloat(), height - paddingBottom, width - margin, height - paddingBottom, paint)

            // Draw the y-axis
            canvas.drawLine(margin.toFloat(), paddingTop.toFloat(), margin.toFloat(), height - paddingBottom, paint)

            // Label the X-axis as "timestamp"
            paint.textSize = 30f // Adjust the text size as needed
            canvas.drawText("Timestamp", width / 2, height - paddingBottom + 50, paint)

            // Label the Y-axis as "close"
            canvas.save()
            canvas.rotate(-90f, (margin / 2).toFloat(), height / 2)
            canvas.drawText("Close", (margin / 2).toFloat(), height / 2, paint)
            canvas.restore()

            paint.color = Color.YELLOW
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

            paint.color = Color.RED
            paint.strokeWidth = 4f

            /**
             * This bit only plots the points in the graph
             */
            for (stockData in stockDataList) {
                val x = margin + (stockData.timestamp.time - minTimestamp) * xScale
                val y = height - paddingBottom - (stockData.close - minClose) * yScale

                // Draw a point at the data position
                canvas.drawPoint(x, y, paint)
            }
        }
    }
}