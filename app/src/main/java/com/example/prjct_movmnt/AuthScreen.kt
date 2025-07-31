package com.example.prjct_movmnt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prjct_movmnt.R 

@Composable
fun AuthScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp), 
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val appIconPainter: Painter = painterResource(id = R.drawable.ic_launcher_foreground) 
            Image(
                painter = appIconPainter,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp) 
                    .padding(bottom = 48.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Get started by logging in or creating an account.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Login", fontSize = 16.sp)
            }

            OutlinedButton( 
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Register", fontSize = 16.sp)
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun AuthScreenPreview() {

    AuthScreen(onLoginClick = {}, onRegisterClick = {})
}


