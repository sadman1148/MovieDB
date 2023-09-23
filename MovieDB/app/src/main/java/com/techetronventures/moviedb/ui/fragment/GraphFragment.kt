package com.techetronventures.moviedb.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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

    fun readCSVFile(): List<StockData> {

        val stockDataList = mutableListOf<StockData>()

        try {
            // Open the CSV file
            val inputStream: InputStream = resources.openRawResource(R.raw.stock)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            // Read and parse the CSV data
            while (reader.readLine().also { line = it } != null) {
                val tokens = line!!.split(",")
                if (tokens.size == 6) {
                    Log.d("SADMAN", "tokensize: ${tokens.size}")
                    Log.d("SADMAN", "StockData: ${tokens[1]}, ${tokens[2]}, ${tokens[3]}, ${tokens[4]}, ${tokens[5]}, ${tokens[6]}")
                    val timestamp =
                        SimpleDateFormat("M/d/yyyy h:mm:ss a", Locale.getDefault()).parse(tokens[0])
                    val open = tokens[1].toFloat()
                    val high = tokens[2].toFloat()
                    val low = tokens[3].toFloat()
                    val close = tokens[4].toFloat()
                    val volume = tokens[5].toFloat()

                    val stockData = StockData(timestamp, open, high, low, close, volume)
                    Log.d("SADMAN", "StockData: $timestamp, $open, $high, $low, $close, $volume")
                    stockDataList.add(stockData)
                }
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stockDataList
    }

    inner class SpikeGraphView(context: Context) : View(context) {

        private val margin = 50
        private val paddingTop = 100
        private val paddingBottom = 100
        private val paint = Paint()

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)

            if (canvas == null) return

            val width = width.toFloat()
            val height = height.toFloat()

            // Calculate the maximum and minimum values for scaling
            val maxClose = stockDataList.map { it.close }.maxOrNull() ?: 0f
            val minClose = stockDataList.map { it.close }.minOrNull() ?: 0f

            // Calculate scaling factors
            val xScale = (width - 2 * margin) / stockDataList.size
            val yScale = (height - paddingTop - paddingBottom) / (maxClose - minClose)

            paint.color = Color.BLACK
            paint.strokeWidth = 2f

            // Draw the x-axis
            canvas.drawLine(margin.toFloat(), height - paddingBottom, width - margin, height - paddingBottom, paint)

            // Draw the y-axis
            canvas.drawLine(margin.toFloat(), paddingTop.toFloat(), margin.toFloat(), height - paddingBottom, paint)

            paint.color = Color.BLUE
            paint.strokeWidth = 4f

            // Plot the spike graph
            for ((index, stockData) in stockDataList.withIndex()) {
                val x = margin + index * xScale
                val y = height - paddingBottom - (stockData.close - minClose) * yScale
                canvas.drawPoint(x, y, paint)
            }
        }
    }
}
