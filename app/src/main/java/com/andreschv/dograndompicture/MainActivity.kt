package com.andreschv.dograndompicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.api.load
import com.andreschv.dograndompicture.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGetImage.setOnClickListener {
            // Use launch and pass Dispatchers.Main to tell that
            // the result of this Coroutine is expected on the main thread.
            launch(Dispatchers.Main) {
                // Try catch block to handle exceptions when calling the API.
                try {
                    val response = ApiAdapter.apiClient.getRandomDogImage()
                    // Check if response was successful.
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        // Check for null
                        data.message?.let { imageUrl ->
                            // Load URL into the ImageView using Coil.
                            binding.ivDogImage.load(imageUrl)
                        }
                    } else {
                        // Show API error.
                        Toast.makeText(
                                this@MainActivity,
                                "Error Occurred: ${response.message()}",
                                Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    // Show API error. This is the error raised by the client.
                    Toast.makeText(this@MainActivity,
                            "Error Occurred: ${e.message}",
                            Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}