package com.example.toptendownloaderapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.log10

class MainActivity : AppCompatActivity() {
    private val TAG = "MAIN_ACTIVITY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")

    }

    companion object {
        private val TAG = "DOWNLOAD_DATA"

        private class DownloadData : AsyncTask<String, Void, String>() {

            override fun doInBackground(vararg url: String?): String {

                val rssFeed = downloadXML(url[0]!!);
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error in download")
                }

                return rssFeed;
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute: ${result}")
            }

        }

        private fun downloadXML(urlPath: String): String {
            val xmlResult = StringBuilder()
            try {
                val url = URL(urlPath)
                val connection = url.openConnection() as HttpURLConnection
                val response = connection.responseCode;
                Log.d("downloadXml", "downloadXML: ${response}")

//            val inputStream = connection.inputStream
//            val inputStreamReader = InputStreamReader(inputStream)
//            val reader = BufferedReader(inputStreamReader)

                val reader = BufferedReader(InputStreamReader(connection.inputStream));
                val inputBuffer = CharArray(500);
                var charsRead = 0;
                while(charsRead >= 0){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead > 0){
                        xmlResult.append(String(inputBuffer,0,charsRead));
                    }
                }

                reader.close()

                return xmlResult.toString()
            } catch (e: MalformedURLException){
                Log.e(TAG, "downloadXML: Invalid url ${e.message}" )
            } catch (e: IOException){
                Log.e(TAG, "downloadXML: IO EXception reading data ${e.message}", )
            } catch(e: SecurityException){
                Log.e(TAG, "Security Exception ${e.message}", )
            }

            catch (e: Exception){
                Log.e(TAG, "downloadXML: unknown err", )
            }

            return ""
        }



    }


}