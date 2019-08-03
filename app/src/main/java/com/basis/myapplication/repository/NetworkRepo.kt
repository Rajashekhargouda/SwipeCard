package com.basis.myapplication.repository

import com.basis.myapplication.model.CardData
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection



class NetworkRepo {

    // fetches the data from the server
    fun getData(url:String):ArrayList<CardData>?{
        try {
            val URL = URL(url)
            val httpsURLConnection:HttpsURLConnection = URL.openConnection() as HttpsURLConnection
            httpsURLConnection.requestMethod = "GET"
            httpsURLConnection.setRequestProperty("Content-Type", "application/json")
            httpsURLConnection.connect()
            if (httpsURLConnection.responseCode == 200){
                val inputStream = httpsURLConnection.inputStream
                val stringBuilder = StringBuilder()
                if (inputStream == null)
                    return null
                var inLine:String?

                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                inLine = bufferedReader.readLine()

                //read the data till last line
                while (inLine!= null){
                    stringBuilder.append(inLine)
                    inLine = bufferedReader.readLine()

                }
                return  deserializeData(stringBuilder.toString())
            }
            return null
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    //converts the data to Card Data
    private fun deserializeData(response: String):ArrayList<CardData>? {
        try {
            val dataList = ArrayList<CardData>()
            val abc:String = response.substring(1)
            val jsonResponseObject = JSONObject(abc)
            val jsonArray = jsonResponseObject.getJSONArray("data")
            val arrayLength = jsonArray.length()
            for (i in 0 until arrayLength) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("id")
                val text = jsonObj.getString("text")
                dataList.add(CardData(id,text))
            }
            return dataList
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}