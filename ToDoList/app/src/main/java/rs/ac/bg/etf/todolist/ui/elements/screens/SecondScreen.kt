package rs.ac.bg.etf.todolist.ui.elements.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    obligationViewModel: ObligationViewModel,
    onClickBack: () -> Unit
) {
    val uiState by obligationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Selected obligation details")
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Go back to first screen"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Obligation informal name: " +
                                (uiState.obligationFromLocalDatabase?.informalName ?: "ERROR")
                    )
                    Text(
                        "Obligation time: " +
                                (uiState.obligationFromLocalDatabase?.dateTime ?: "ERROR")
                    )
                    Text(
                        "Obligation description: " +
                                (uiState.obligationFromKtorServer?.description ?: "ERROR")
                    )
                }
            }
            Button(
                onClick = {
                    obligationViewModel.markObligationAsDone(uiState.obligationFromLocalDatabase?.id ?: 1)
                },
                enabled = !(uiState.obligationFromLocalDatabase?.isDone ?: false),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Mark selected obligation as done")
            }
        }
    }
}