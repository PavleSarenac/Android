package rs.ac.bg.etf.jokes.ui.elements.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job

@Composable
fun CheckBoxWithText(
    text: String = "Default Text",
    updateList: (Boolean, String) -> Job
) {
    val (checkedState, onStateChange) = remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .toggleable(
                value = checkedState,
                onValueChange = {
                    onStateChange(!checkedState)
                    updateList(it, text)
                },
                role = Role.Checkbox
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = null // null recommended for accessibility with screenreaders
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}