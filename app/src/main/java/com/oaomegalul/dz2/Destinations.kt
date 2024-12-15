package com.oaomegalul.dz2

sealed class Destinations(
    val route: String,
    val title: String? = null)
{
    object HomeScreen : Destinations(
        route = "home_screen",
        title = "Заметки"
    )
    object AddTransactionScreen : Destinations(
        route = "add_transaction/{action}",
        title = "Расходы"
    )
    object ReportsScreen : Destinations(
        route = "reports_screen",
        title = "Отчёты"
    )
    object TranslistScreen : Destinations(
        route = "translist_screen",
        title = "Список транзакций"
    )
    object EditScreen : Destinations(
        route = "edit_screen",
        title = "Корретирование"
    )
}
