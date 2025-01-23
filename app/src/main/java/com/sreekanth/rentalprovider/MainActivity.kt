package com.sreekanth.rentalprovider

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sreekanth.rentalprovider.ui.theme.RentalProviderTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Can be moved to a proper place where the tracking has to be started
        val serviceIntent = Intent(this, MonitoringService::class.java)
        startService(serviceIntent)
        viewModel.setRepo(MonitoringService.provideRepo())
        enableEdgeToEdge()
        setContent {
            RentalProviderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val textState = viewModel.text.observeAsState().value
            val loginStatusState = viewModel.loginStatus.observeAsState().value

            Text(
                text = textState!!,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            if (loginStatusState!!) {
                Button(onClick = { viewModel.login() }) {
                    Text(text = "Login")
                }
            } else {
                Button(onClick = { viewModel.logout() }) {
                    Text(text = "Logout")
                }
            }
        }
    }

    @Preview(showBackground = true, widthDp = 1280, heightDp = 1488)
    @Composable
    fun GreetingPreview() {
        RentalProviderTheme {
            Greeting("Android")
        }
    }
}
