package com.oaomegalul.dz2

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.oaomegalul.dz2.ui.theme.Dz2Theme

class MainActivity : ComponentActivity() {
    private val isAddTransactionScreen = mutableStateOf(false)
    private val isHomeInitialize = mutableStateOf(false)
    private val isReportsScreen = mutableStateOf(false)
    private val isEditTransactionScreen = mutableStateOf(false)
    private val isTransactionList = mutableStateOf(false)

    private lateinit var home : HomeActivity
    private lateinit var transaction : AddTransactionActivity
    private lateinit var reports : ReportsActivity
    private lateinit var translist : TransactionListActivity
    private lateinit var edit : EditTransactionActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dz2Theme() {
                val navController: NavHostController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background)
                {
                    Box(modifier = Modifier.background(Color.White))
                    {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavigationGraph(navController: NavHostController)
    {
        NavHost(navController, startDestination = Destinations.HomeScreen.route)
        {
            composable(Destinations.HomeScreen.route)
            {
                isTransactionList.value = false
                isAddTransactionScreen.value = false
                isReportsScreen.value = false
                isEditTransactionScreen.value = false
                if(!isHomeInitialize.value)
                {
                    isHomeInitialize.value = true
                    home = HomeActivity(navController)
                }
                home.HomeActivityScreen()
            }
            composable(Destinations.ReportsScreen.route)
            {
                isTransactionList.value = false
                isAddTransactionScreen.value = false
                isHomeInitialize.value = false
                isEditTransactionScreen.value = false
                if(!isReportsScreen.value)
                {
                    isReportsScreen.value = true
                    reports = ReportsActivity(navController)
                }
                reports.ReportsScreen()
            }
            composable(Destinations.TranslistScreen.route)
            {
                isAddTransactionScreen.value = false
                isHomeInitialize.value = false
                isEditTransactionScreen.value = false
                isReportsScreen.value = false
                if(!isTransactionList.value)
                {
                    isTransactionList.value = true
                    translist = TransactionListActivity(navController)
                }
                translist.TransactionListScreen()
            }
            composable(
                "edit_screen/{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")
                edit = EditTransactionActivity(navController)
                if (transactionId != null) {
                    edit.EditTransactionScreen(transactionId)
                }
            }
            composable(
                route = Destinations.AddTransactionScreen.route,
                arguments = listOf(navArgument("action") { type = NavType.StringType })
            ) { backStackEntry ->
                val action = backStackEntry.arguments?.getString("action")
                isHomeInitialize.value = false
                isReportsScreen.value = false
                if (action != null) {
                    transaction = AddTransactionActivity(navController, action)
                    transaction.AddTransactionScreen()
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    HomeActivity(navController).HomeActivityScreen()
}