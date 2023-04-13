package com.example.myapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


public class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                val navController = rememberNavController()
                SetupNavGraph(navController = navController)

        }
    }

    @Composable
    fun SetupNavGraph(navController: NavHostController) {
        Log.e("H","EEEE");
        NavHost(
            navController = navController,
            startDestination = Screens.Splash.route
        ) {
            composable(route = Screens.Splash.route) {

                SplashScreen(navController = navController)
            }
            composable(route = Screens.Home.route) {
                MovieActivity()
            }
        }

    }

    @Composable
    fun SplashScreen(navController: NavHostController) {
        LaunchedEffect(key1 = true) {
            delay(3000L)
            navController.navigate("main_screen")
        }

        // Image
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.mipmap.ic_icon),
                contentDescription = "Logo")
            Text("MoviesApp")
        }

    }
}