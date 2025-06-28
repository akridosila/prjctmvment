package com.example.prjct_movmnt
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.prjct_movmnt.ui.theme.HomeScreen
import androidx.compose.material3.MaterialTheme
import androidx.activity.compose.setContent
import android.content.Intent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AuthScreen(
                    onLoginClick = {
                        // Navigate to LoginActivity
                        val intent = Intent(this@MainActivity, Login::class.java)
                        startActivity(intent)
                    },
                    onRegisterClick = {
                        // Navigate to RegisterActivity
                        val intent = Intent(this@MainActivity, Register::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}