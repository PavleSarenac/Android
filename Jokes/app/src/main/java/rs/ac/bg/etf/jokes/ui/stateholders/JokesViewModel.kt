package rs.ac.bg.etf.jokes.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.ac.bg.etf.jokes.CATEGORIES
import rs.ac.bg.etf.jokes.FLAGS
import rs.ac.bg.etf.jokes.JOKE_TYPES
import rs.ac.bg.etf.jokes.data.JokesRepository
import rs.ac.bg.etf.jokes.data.retrofit.models.Joke
import javax.inject.Inject

data class UiState(
    val checkedCategories: List<String> = listOf(),
    val selectedLanguage: String = "en",
    val checkedFlags: List<String> = listOf(),
    val checkedJokeTypes: List<String> = listOf(),
    val jokeSearchString: String = "",
    val amountOfJokes: String = "1",
    val jokeShowingFrequencyInSeconds: String = "5",
    val jokes: List<Joke> = listOf()
)

@HiltViewModel
class JokesViewModel @Inject constructor(
    private val jokesRepository: JokesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    suspend fun getJokes() = coroutineScope {
        val jokes: Deferred<List<Joke>> = if (_uiState.value.amountOfJokes.toInt() <= 1) {
            async(Dispatchers.IO) { getNewOneJoke() }
        } else {
            async(Dispatchers.IO) { getNewMultipleJokes() }
        }
        return@coroutineScope jokes.await()
    }

    private suspend fun getNewOneJoke(): List<Joke> {
        val url = createUrl()
        val joke = jokesRepository.getOneJoke(url)
        _uiState.update {
            it.copy(
                jokes = listOf(joke)
            )
        }
        return listOf(joke)
    }

    private suspend fun getNewMultipleJokes(): List<Joke> {
        val url = createUrl()
        val multipleJokes = jokesRepository.getMultipleJokes(url)
        _uiState.update {
            it.copy(
                jokes = multipleJokes.jokes
            )
        }
        return multipleJokes.jokes
    }

    private fun createUrl(): String {
        var url = ""
        var isFirstQueryParameter = true

        if (_uiState.value.checkedCategories.isEmpty()) {
            url += "Any"
        } else {
            CATEGORIES.forEach {
                if (_uiState.value.checkedCategories.contains(it)) {
                    url += "${it},"
                }
            }
            url = url.substring(0, url.length - 1)
        }

        if (_uiState.value.selectedLanguage != "en") {
            isFirstQueryParameter = false
            url += "?lang=${_uiState.value.selectedLanguage}"
        }

        if (_uiState.value.checkedFlags.isNotEmpty()) {
            if (isFirstQueryParameter) {
                url += "?"
                isFirstQueryParameter = false
            } else {
                url += "&"
            }
            url += "blacklistFlags="
            FLAGS.forEach {
                if (_uiState.value.checkedFlags.contains(it)) {
                    url += "${it},"
                }
            }
            url = url.substring(0, url.length - 1)
        }

        if (_uiState.value.checkedJokeTypes.size == 1) {
            if (isFirstQueryParameter) {
                url += "?"
                isFirstQueryParameter = false
            } else {
                url += "&"
            }
            url += "type="
            JOKE_TYPES.forEach {
                if (_uiState.value.checkedJokeTypes.contains(it)) {
                    url += "${it},"
                }
            }
            url = url.substring(0, url.length - 1)
        }

        if (_uiState.value.jokeSearchString != "") {
            if (isFirstQueryParameter) {
                url += "?"
                isFirstQueryParameter = false
            } else {
                url += "&"
            }
            url += "contains=${_uiState.value.jokeSearchString}"
        }

        if (_uiState.value.amountOfJokes != "1") {
            url += if (isFirstQueryParameter) "?" else "&"
            url += "amount=${_uiState.value.amountOfJokes}"
        }

        return url
    }

    fun updateJokeShowingFrequencyInSeconds(
        jokeShowingFrequencyInSeconds: String
    ) = viewModelScope.launch(Dispatchers.Main) {
        _uiState.update {
            it.copy(
                jokeShowingFrequencyInSeconds = jokeShowingFrequencyInSeconds
            )
        }
    }

    fun updateAmountOfJokes(amountOfJokes: String) = viewModelScope.launch(Dispatchers.Main) {
        _uiState.update {
            it.copy(
                amountOfJokes = amountOfJokes
            )
        }
    }

    fun updateJokeSearchString(jokeSearchString: String) = viewModelScope.launch(Dispatchers.Main) {
        _uiState.update {
            it.copy(
                jokeSearchString = jokeSearchString
            )
        }
    }

    fun updateSelectedLanguage(language: String) = viewModelScope.launch(Dispatchers.Main) {
        _uiState.update {
            it.copy(
                selectedLanguage = language
            )
        }
    }

    fun updateCheckedCategories(
        checked: Boolean,
        category: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        val updatedCheckedCategories: List<String> = if (checked) {
            _uiState.value.checkedCategories.plusElement(category)
        } else {
            _uiState.value.checkedCategories.minusElement(category)
        }

        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(
                    checkedCategories = updatedCheckedCategories
                )
            }
        }
    }

    fun updateCheckedFlags(
        checked: Boolean,
        flag: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        val updatedCheckedFlags: List<String> = if (checked) {
            _uiState.value.checkedFlags.plusElement(flag)
        } else {
            _uiState.value.checkedFlags.minusElement(flag)
        }

        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(
                    checkedFlags = updatedCheckedFlags
                )
            }
        }

    }

    fun updateCheckedJokeTypes(
        checked: Boolean,
        jokeType: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        val updatedCheckedJokeTypes: List<String> = if (checked) {
            _uiState.value.checkedJokeTypes.plusElement(jokeType)
        } else {
            _uiState.value.checkedJokeTypes.minusElement(jokeType)
        }

        withContext(Dispatchers.Main) {
            _uiState.update {
                it.copy(
                    checkedJokeTypes = updatedCheckedJokeTypes
                )
            }
        }

    }

}