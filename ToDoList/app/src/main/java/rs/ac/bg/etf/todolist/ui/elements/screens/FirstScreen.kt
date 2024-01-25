package rs.ac.bg.etf.todolist.ui.elements.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import rs.ac.bg.etf.todolist.data.room.ObligationModelRoom
import rs.ac.bg.etf.todolist.ui.elements.ROUTES
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel
import java.util.Locale
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstScreen(
    onClickFab: () -> Unit,
    obligationViewModel: ObligationViewModel,
    onClickObligation: (ObligationModelRoom) -> Unit
) {
    var isSortingNeeded by rememberSaveable { mutableStateOf(false) }

    val allObligations by obligationViewModel.allObligations.collectAsState(initial = listOf())

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("ToDoList")
                },
                actions = {
                    if (isSortingNeeded) {
                        IconButton(onClick = { isSortingNeeded = false }) {
                            Icon(
                                imageVector = Icons.Default.ToggleOn,
                                contentDescription = "Sort obligations",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        IconButton(onClick = { isSortingNeeded = true }) {
                            Icon(
                                imageVector = Icons.Default.ToggleOff,
                                contentDescription = "Don't sort obligations",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFab) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new obligation"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.US)
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    if (isSortingNeeded)
                        allObligations.sortedBy { simpleDateFormat.parse(it.dateTime)?.time ?: 0 }
                    else
                        allObligations
                ) { obligation ->
                    val obligationDate = simpleDateFormat.parse(obligation.dateTime)
                    val obligationDateInMillis = obligationDate?.time ?: 0
                    val currentDateInMillis = System.currentTimeMillis()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable {
                                onClickObligation(obligation)
                            },
                        colors =
                            if (currentDateInMillis < obligationDateInMillis)
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                )
                            else
                                CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                )
                    ) {
                        Text("${obligation.informalName}; ${obligation.dateTime}")
                    }
                }
            }
        }
    }
}