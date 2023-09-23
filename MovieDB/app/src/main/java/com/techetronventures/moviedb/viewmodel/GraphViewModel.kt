package com.techetronventures.moviedb.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.techetronventures.moviedb.R
import com.techetronventures.moviedb.data.Repository
import com.techetronventures.moviedb.data.model.StockData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GraphViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun readCSVFile(context: Context): List<StockData> {

        val stockDataList = mutableListOf<StockData>()
        val dateFormat = SimpleDateFormat("yyyy-MM-d H:mm:ss", Locale.getDefault())

        try {
            // Open the CSV file
            val inputStream: InputStream = context.resources.openRawResource(R.raw.stock)
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
}
