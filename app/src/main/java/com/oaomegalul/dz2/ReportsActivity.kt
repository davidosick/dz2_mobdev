package com.oaomegalul.dz2

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.json.JSONArray



class ReportsActivity(private val navController: NavHostController) {

    @Composable
    fun ReportsScreen() {
        var selectedFilter by remember { mutableStateOf("Дата") }
        var _date by remember { mutableStateOf("") }
        var startDate by remember { mutableStateOf("") }
        var endDate by remember { mutableStateOf("") }
        var reportsData by remember { mutableStateOf("Нет данных") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Фильтры отчетов", fontSize = 20.sp)

            DropdownMenuFilter(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            //if (selectedFilter == "Дата" || selectedFilter == "Произвольный период") {
            TextField(
                value = _date,
                onValueChange = { _date = it },
                label = {
                    var selectedFilterText: String
                    if(selectedFilter == "Произвольный период")
                        selectedFilterText= "Начальная дата (DD-MM-YYYY)"
                    else if(selectedFilter == "Дата")
                        selectedFilterText= "Конкретная дата (DD-MM-YYYY)"
                    else if(selectedFilter == "Месяц")
                        selectedFilterText= "Номер месяца и год (MM-YYYY)"
                    else if (selectedFilter == "Год")
                        selectedFilterText = "Номер года (YYYY)"
                    else selectedFilterText = ""
                    Text(selectedFilterText)
                        },
                modifier = Modifier.fillMaxWidth()
            )
            //}
            if (selectedFilter == "Произвольный период") {
                TextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Конечная дата (DD-MM-YYYY)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    if(selectedFilter == "Дата") {
                        startDate = _date
                        endDate = _date
                    }
                    else if(selectedFilter == "Год") {
                        startDate = "01-01-" + _date
                        endDate = "31-12-" + _date
                    }
                    else if(selectedFilter == "Месяц") {
                        startDate = "01-" + _date
                        endDate = "31-" + _date
                    }
                    else if(selectedFilter == "Произвольный период")
                        startDate = _date
                    reportsData = getReportsData(selectedFilter, startDate, endDate)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Показать отчет")
            }

            Text(text = "Результаты: $reportsData", fontSize = 16.sp)
        }
    }

    @Composable
    fun DropdownMenuFilter(selectedFilter: String, onFilterSelected: (String) -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        val filters = listOf("Дата", "Месяц", "Год", "Произвольный период")

        Box {
            Button(onClick = { expanded = true }) {
                Text(selectedFilter)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filters.forEach { filter ->
                    DropdownMenuItem(
                        onClick = {
                            onFilterSelected(filter)
                            expanded = false
                        },
                        text = { Text(text = filter) }
                    )
                }
            }
        }
    }

    private fun getReportsData(filter: String, startDate: String, endDate: String): String {
        val api = APISender()
        val response = api.post("/GetReports?filter=$filter&startDate=$startDate&endDate=$endDate")
        return processReportsData(response.join())
    }

    fun processReportsData(jsonResponse: String): String {
        val transactions = JSONArray(jsonResponse)
        var totalIncome = 0
        var totalExpense = 0
        var maxIncome = 0
        var maxIncomeID = -1
        var maxExpense = 0
        var maxExpenseID = -1

        for (i in 0 until transactions.length()) {
            val transaction = transactions.getJSONObject(i)
            val isIncome = transaction.getBoolean("isIncome")
            val amount = transaction.getInt("summ")

            if (isIncome) {
                totalIncome += amount
                if (amount > maxIncome) maxIncome = amount; maxIncomeID = i;
            } else {
                totalExpense += amount
                if (amount > maxExpense) maxExpense = amount; maxExpenseID = i;
            }
        }

        val netResult = totalIncome - totalExpense

        return """
        За месяц потрачено $totalExpense рублей,
        заработано $totalIncome рублей,
        максимальная прибыль $maxIncome рублей (${if(maxIncomeID != -1) transactions.getJSONObject(maxIncomeID).getString("category") else ""}),
        максимальная трата $maxExpense рублей (${if(maxExpenseID != -1) transactions.getJSONObject(maxExpenseID).getString("category") else ""}),
        отношение з/п к тратам $netResult рублей
    """.trimIndent()
    }
}
