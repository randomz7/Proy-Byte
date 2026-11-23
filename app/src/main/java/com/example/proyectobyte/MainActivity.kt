package com.example.proyectobyte

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.proyectobyte.ui.theme.AppTheme
import com.example.proyectobyte.ui.theme.ProyectoByteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoByteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onStartClick = {
                            startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                        },
                        onTerminosClick = {
                            startActivity(Intent(this@MainActivity, TerminosActivity::class.java))
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun MainScreen(
    onStartClick: () -> Unit,
    onTerminosClick: () -> Unit,
){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.byte_logo),
            contentDescription = stringResource(R.string.logo),
            modifier = Modifier.size(AppTheme.sizes.logoSizeLarge))
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.displayLarge)

        Button(onClick = onStartClick) {
            Text(stringResource(R.string.empezar),
                style = MaterialTheme.typography.titleMedium)
        }

    }
    Column(modifier = Modifier.fillMaxSize()
        .navigationBarsPadding()
        .padding(bottom = AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {
        Text(stringResource(R.string.derechos))
    }

    Column(modifier = Modifier.fillMaxSize().padding(top = AppTheme.spacing.large, end = AppTheme.spacing.medium),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top) {
        IconButton(
            onClick = onTerminosClick,
            modifier = Modifier.size(AppTheme.sizes.logoSizeSmall)
        ) {
            Image(
                painter = painterResource(R.drawable.terminos_logo),
                contentDescription = stringResource(R.string.terminos_condiciones),
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            stringResource(R.string.terminos),
            style = MaterialTheme.typography.titleMedium)
    }
}
