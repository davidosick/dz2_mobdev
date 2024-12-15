package com.oaomegalul.dz2

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.concurrent.CompletableFuture

class AddTransactionActivity(private val navController: NavHostController, private val type: String) {
    var realType = if(type == "income") arrayOf("доходы", "Доходы") else arrayOf("расходы", "Расходы")
    var isIncome = if(type == "income") true else false
    @Composable
    fun AddTransactionScreen() {
        var amount by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var comment by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = realType[1],
                fontSize = 25.sp
            )
            TextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сумма") },
                modifier = Modifier.fillMaxWidth()
            )
            /*TextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth()
            )*/
            CategoryDropdown(
                isIncome = isIncome,
                category = category,
                onCategorySelected = { selectedCategory -> category = selectedCategory }
            )
            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Дата (DD-MM-YYYY)") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Комментарий (опционально)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    sendDataToServer(isIncome, amount, category, date, comment)
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Сохранить $type")
            }
        }
    }

    @Composable
    fun CategoryDropdown(
        isIncome: Boolean,
        category: String,
        onCategorySelected: (String) -> Unit
    ) {
        val predefinedCategories = if (isIncome) listOf(
            "Заработная плата", "Премия", "Подработка", "Донат", "Кредит", "Прочее"
        ) else listOf(
            "Продукты", "Транспорт", "Онлайн-сервисы", "Кредиты", "Обучение", "Ремонт", "Отпуск", "Хобби, развлечения"
        )
        var expanded by remember { mutableStateOf(false) }

        Row {
            TextField(
                value = category,
                onValueChange = onCategorySelected,
                label = { Text("Категория") },
                //modifier = Modifier.fillMaxWidth(),
                readOnly = false
            )
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Список",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                predefinedCategories.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            onCategorySelected(item)
                            expanded = false
                        },
                        text = {
                            Text(text = item)
                        }
                    )
                }
            }
        }
    }


    private fun sendDataToServer(isIncome: Boolean, amount: String, category: String, date: String, comment: String) {
        val amountInt = amount.toInt()
        val api = APISender()
        Log.w("!!!!", "$isIncome, $amount, $category, $date, $comment")
        val responseFuture: CompletableFuture<String> = api.post("/CreateTransaction?isIncome=${isIncome}&category=${category}&summ=${amount}&transactionDate=${date}&comment=${comment}")
        Log.w("!!!!", "$responseFuture")
    }

}