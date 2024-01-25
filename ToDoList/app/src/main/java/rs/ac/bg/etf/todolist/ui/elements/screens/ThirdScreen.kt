package rs.ac.bg.etf.todolist.ui.elements.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rs.ac.bg.etf.todolist.data.retrofit.models.ObligationModelRetrofit
import rs.ac.bg.etf.todolist.ui.stateholders.ObligationViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val NUMBER_OF_SECONDS_IN_ONE_HOUR = 3600
const val NUMBER_OF_SECONDS_IN_ONE_MINUTE = 60
const val NUMBER_OF_MILLISECONDS_IN_ONE_SECOND = 1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(
    onClickBack: () -> Unit,
    obligationViewModel: ObligationViewModel
) {
    val columnScrollState = rememberScrollState()

    var obligationInformalName by rememberSaveable { mutableStateOf("") }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val timePickerState = rememberTimePickerState(is24Hour = true)
    var obligationDescription by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Add new obligation")
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
                .verticalScroll(columnScrollState)
        ) {
            Text(
                text = "Enter obligation informal name:",
                fontSize = 16.sp,
                fontWeight = FontWeight(800),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                label = { Text(text = "Obligation informal name...") },
                value = obligationInformalName,
                onValueChange = { obligationInformalName = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            HorizontalDivider()
            Text(
                text = "Enter obligation date:",
                fontSize = 16.sp,
                fontWeight = FontWeight(800),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            DatePicker(
                state = datePickerState,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            HorizontalDivider()
            Text(
                text = "Enter obligation time:",
                fontSize = 16.sp,
                fontWeight = FontWeight(800),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            TimePicker(
                state = timePickerState,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            HorizontalDivider()
            Text(
                text = "Enter obligation description:",
                fontSize = 16.sp,
                fontWeight = FontWeight(800),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                label = { Text(text = "Obligation description...") },
                value = obligationDescription,
                onValueChange = { obligationDescription = it },
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(128.dp)
            )
            HorizontalDivider()
            Button(
                onClick = {
                    obligationViewModel.addNewObligation(
                        ObligationModelRetrofit(
                            id = 1,
                            isDone = false,
                            informalName = obligationInformalName,
                            dateTime = getDateTimeString(datePickerState, timePickerState),
                            description = obligationDescription
                        )
                    )
                    onClickBack()
                },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Add new obligation")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun getDateTimeString(
    datePickerState: DatePickerState,
    timePickerState: TimePickerState
): String {
    val dateInMillis = datePickerState.selectedDateMillis ?: 0
    val timeInMillis = getTimeInMillis(timePickerState)
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.US)
    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return simpleDateFormat.format(Date(dateInMillis + timeInMillis))
}

@OptIn(ExperimentalMaterial3Api::class)
fun getTimeInMillis(timePickerState: TimePickerState): Int {
    val hoursInMillis = timePickerState.hour * NUMBER_OF_SECONDS_IN_ONE_HOUR * NUMBER_OF_MILLISECONDS_IN_ONE_SECOND
    val minutesInMillis = timePickerState.minute * NUMBER_OF_SECONDS_IN_ONE_MINUTE * NUMBER_OF_MILLISECONDS_IN_ONE_SECOND
    return hoursInMillis + minutesInMillis
}