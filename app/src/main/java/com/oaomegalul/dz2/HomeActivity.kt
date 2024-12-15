package com.oaomegalul.dz2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

class HomeActivity(private val navController: NavHostController) {
    @Composable
    fun HomeActivityScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Добро пожаловать в семейный калькулятор!",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Button(
                onClick = { navController.navigate("add_transaction/income") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Добавить доход")
            }
            Button(
                onClick = { navController.navigate("add_transaction/expense") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Добавить расход")
            }
            Button(
                onClick = { navController.navigate("translist_screen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Скорректировать данные")
            }
            Button(
                onClick = { navController.navigate("reports_screen") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Просмотреть отчеты")
            }
        }
    }

}