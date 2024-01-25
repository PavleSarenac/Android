package rs.ac.bg.etf.todolist.ui.elements

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rs.ac.bg.etf.todolist.ui.elements.screens.FirstScreen
import rs.ac.bg.etf.todolist.ui.elements.screens.SecondScreen
import rs.ac.bg.etf.todolist.ui.elements.screens.ThirdScreen
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel

val ROUTES = listOf("FirstScreen", "SecondScreen", "ThirdScreen")

@Composable
fun ToDoApp(
    obligationViewModel: ObligationViewModel
) {
    val navController = rememberNavController()
    obligationViewModel.turnPeriodicSyncOn()

    NavHost(
        navController = navController,
        startDestination = ROUTES[0]
    ) {
        composable(route = ROUTES[0]) {
            FirstScreen(
                onClickFab = { navController.navigate(ROUTES[2]) },
                obligationViewModel = obligationViewModel,
                onClickObligation = { obligation ->
                    navController.navigate("${ROUTES[1]}/${obligation.id}")
                }
            )
        }
        composable(
            route = "${ROUTES[1]}/{obligationId}",
            arguments = listOf(
                navArgument("obligationId") {
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val obligationId = navBackStackEntry.arguments?.getInt("obligationId") ?: 1
            obligationViewModel.getObligationFromLocalDatabase(obligationId)
            obligationViewModel.getObligationFromKtorServer(obligationId)
            SecondScreen(
                obligationViewModel = obligationViewModel,
                onClickBack = { navController.popBackStack() }
            )
        }
        composable(route = ROUTES[2]) {
            ThirdScreen(
                onClickBack = { navController.popBackStack() },
                obligationViewModel = obligationViewModel
            )
        }
    }

}