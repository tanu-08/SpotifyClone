package com.tanu.spotifymusic.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tanu.spotifymusic.common.AppConstants
import com.tanu.spotifymusic.databinding.ActivityMainBinding
import com.tanu.spotifymusic.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthActivityViewModel by viewModels()
    private val REDIRECT_URI = "com.tanu.spotifymusic://auth"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.data?.let { uri ->
            val code = uri.getQueryParameter("code")
            if (code != null) {
                authViewModel.authenticateUser(code, REDIRECT_URI)
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
            }
        }
        binding.loginBtn.setOnClickListener {
            val authUrl = "https://accounts.spotify.com/authorize" +
                    "?client_id=${AppConstants.CLIENT_ID}" +
                    "&response_type=code" +
                    "&redirect_uri=$REDIRECT_URI" +
                    "&scope=user-read-private%20user-read-email"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
            startActivity(intent)
        }

        lifecycleScope.launch {
            authViewModel.authResponse.collect { response ->
                response?.let {
                    Toast.makeText(this@MainActivity, "Access Token: ${it.access_token}", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                    finish()
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.errorMessage.collect { error ->
                error?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}