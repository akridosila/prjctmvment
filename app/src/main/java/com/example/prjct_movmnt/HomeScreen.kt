package com.example.prjct_movmnt.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.prjct_movmnt.R 

@Composable
fun HomeScreen(onStartTestClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder 
            val appIconPainter: Painter = painterResource(id = R.drawable.ic_launcher_foreground) 
            Image(
                painter = appIconPainter,
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(120.dp) 
                    .padding(bottom = 32.dp),
                contentScale = ContentScale.Fit
            )

            Button(onClick = onStartTestClick) {
                Text("Start test")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onStartTestClick = {})
}
