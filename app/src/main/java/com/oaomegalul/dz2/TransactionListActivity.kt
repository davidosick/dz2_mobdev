package com.oaomegalul.dz2

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.util.concurrent.CompletableFuture

class TransactionListActivity(private val navController: NavHostController) {
    @Composable
    fun TransactionListScreen() {
        val transactions = remember { mutableStateListOf<EditTransactionActivity.TransactionData>() }

        LaunchedEffect(Unit) {
            val apiTransactions = loadAllTransactionsFromAPI()
            transactions.addAll(apiTransactions)
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = "Выберите транзакцию для редактирования",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transactions) { transaction ->
                    TransactionItem(transaction) {
                        onTransactionSelected(transaction.id)
                    }
                }
            }
        }
    }

    private fun onTransactionSelected(id: String) {

    }

    private fun loadAllTransactionsFromAPI(): List<EditTransactionActivity.TransactionData> {
        val api = APISender()
        val response = api.get("/").get()

        val transactions = mutableListOf<EditTransactionActivity.TransactionData>()

        try {
            val responseBody = response.body?.string() ?: throw IllegalStateException("Response body is null")

            //Log.d("111", responseBody)

            val jsonData = JSONArray(responseBody)

            for (i in 0 until jsonData.length()) {
                val item = jsonData.getJSONObject(i)
                transactions.add(
                    EditTransactionActivity.TransactionData(
                        id = item.getString("id"),
                        isIncome = item.getBoolean("isIncome"),
                        amount = item.getInt("summ"),
                        category = item.getString("category"),
                        transactionDate = item.getString("transactionDate"),
                        comment = item.getString("comment")
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("111", "Error ", e)
        }

        return transactions
    }






    @Composable
    fun TransactionItem(transaction: EditTransactionActivity.TransactionData, onClick: () -> Unit) {
        Card(
            //elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    navController.navigate("edit_screen/${transaction.id}")
                })
                .padding(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Категория: ${transaction.category}", fontWeight = FontWeight.Bold)
                Text(text = "Сумма: ${if(transaction.isIncome) "+" else "-"}${transaction.amount}")
                Text(text = "Дата: ${transaction.transactionDate}")
                transaction.comment.takeIf { it.isNotBlank() }?.let {
                    Text(text = "Комментарий: $it")
                }
            }
        }

    }
}