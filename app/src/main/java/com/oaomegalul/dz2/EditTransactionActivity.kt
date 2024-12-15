package com.oaomegalul.dz2

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.json.JSONObject

class EditTransactionActivity(private val navController: NavHostController) {
    private var transactionData: TransactionData? = null

    /*init {
        transactionData = loadTransactionData(transactionId)
    }*/

    @Composable
    fun EditTransactionScreen(transactionId: String) {
        transactionData = loadTransactionData(transactionId)
        Log.d("111", transactionData.toString())

        transactionData?.let { data ->
            var amount by remember { mutableStateOf(data.amount.toString()) }
            var category by remember { mutableStateOf(data.category) }
            var date by remember { mutableStateOf(data.transactionDate) }
            var comment by remember { mutableStateOf(data.comment) }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Редактировать транзакцию",
                    fontSize = 25.sp
                )
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    modifier = Modifier.fillMaxWidth()
                )
                CategoryDropdown(
                    isIncome = data.isIncome,
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
                        updateTransaction(transactionId, amount, category, date, comment)
                        navController.navigateUp()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Сохранить изменения")
                }
            }
        } ?: run {
            Text(text = "Ошибка загрузки данных транзакции", color = Color.Red)
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

    private fun loadTransactionData(transactionId: String): TransactionData? {
        val api = APISender()
        val response = api.get("/GetTransactionById?id=$transactionId").get()
        val responseBody = response.body?.string()

        val jsonData = responseBody?.let { JSONObject(it) }
        Log.d("sss", jsonData.toString())
        if (jsonData != null) {
            return TransactionData(
                id = jsonData.getString("id"),
                isIncome = jsonData.getBoolean("isIncome"),
                amount = jsonData.getInt("summ"),
                category = jsonData.getString("category"),
                transactionDate = jsonData.getString("transactionDate"),
                comment = jsonData.getString("comment")
            )
        } else return null
    }


    private fun updateTransaction(transactionId: String, amount: String, category: String, date: String, comment: String) {
        val amountInt = amount.toInt()
        val api = APISender()
        Log.w("!!!!", "Обновление: $transactionId, $amountInt, $category, $date, $comment")
        api.post("/UpdateTransaction?transactionId=${transactionId}&amount=${amountInt}&category=${category}&transactionDate=${date}&comment=${comment}")
    }

    data class TransactionData(
        val id: String,
        val isIncome: Boolean,
        val amount: Int,
        val category: String,
        val transactionDate: String,
        val comment: String
    )
}
