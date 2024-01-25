package rs.ac.bg.etf.jokes

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import rs.ac.bg.etf.jokes.services.JokeFetchingService
import rs.ac.bg.etf.jokes.ui.elements.composables.CheckBoxWithText
import rs.ac.bg.etf.jokes.ui.elements.composables.Spinner
import rs.ac.bg.etf.jokes.ui.elements.theme.JokesTheme
import rs.ac.bg.etf.jokes.ui.stateholders.JokesViewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var jokeFetchingService: JokeFetchingService
    private var isJokeFetchingServiceBoundByBinder = false
    private val connectionToJokeFetchingService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as JokeFetchingService.LocalBinder
            jokeFetchingService = binder.getService()
            isJokeFetchingServiceBoundByBinder = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            isJokeFetchingServiceBoundByBinder = false
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }

        setContent {
            JokesTheme {
                val columnScrollState = rememberScrollState()

                val jokesViewModel: JokesViewModel = viewModel()
                val uiState by jokesViewModel.uiState.collectAsState()

                var shouldFetchJokes by rememberSaveable { mutableStateOf(true) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "JokesApp") },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(columnScrollState)
                    ) {
                        Text(text = "Select category/categories:")
                        CATEGORIES.forEach { CheckBoxWithText(it, jokesViewModel::updateCheckedCategories) }

                        Text(text = "Select language:")
                        Spinner(LANGUAGES, jokesViewModel)

                        Text(text = "Select flags to blacklist:")
                        FLAGS.forEach { CheckBoxWithText(it, jokesViewModel::updateCheckedFlags) }

                        Text(text = "Select at least one joke type:")
                        JOKE_TYPES.forEach { CheckBoxWithText(it, jokesViewModel::updateCheckedJokeTypes) }

                        Text(text = "Search for a joke that contains this search string:")
                        TextField(
                            value = uiState.jokeSearchString,
                            onValueChange = { jokesViewModel.updateJokeSearchString(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter your search string here...") },
                            singleLine = true
                        )

                        Text(text = "Amount of jokes:")
                        TextField(
                            value = uiState.amountOfJokes,
                            onValueChange = { jokesViewModel.updateAmountOfJokes(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter the amount of jokes here...") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )

                        Text(text = "Enter the frequency of showing new jokes:")
                        TextField(
                            value = uiState.jokeShowingFrequencyInSeconds,
                            onValueChange = { jokesViewModel.updateJokeShowingFrequencyInSeconds(it)},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter the frequency here (in seconds)...") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        Button(
                            onClick = {
                                if (shouldFetchJokes)
                                    jokeFetchingService.startFetchingJokes(jokesViewModel, uiState)
                                else
                                    jokeFetchingService.stopFetchingJokes()
                                shouldFetchJokes = !shouldFetchJokes
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Start/Stop showing jokes")
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, JokeFetchingService::class.java).also { intent ->
            bindService(intent, connectionToJokeFetchingService, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isJokeFetchingServiceBoundByBinder) {
            unbindService(connectionToJokeFetchingService)
            isJokeFetchingServiceBoundByBinder = false
        }
    }
}