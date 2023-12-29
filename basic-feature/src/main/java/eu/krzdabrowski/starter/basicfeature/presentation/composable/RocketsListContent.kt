package eu.krzdabrowski.starter.basicfeature.presentation.composable

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RocketsListContent(
    rocketList: List<RocketDisplayable>,
    modifier: Modifier = Modifier,
    onRocketClick: (String) -> Unit,
) {
    var selectedRocket by remember { mutableStateOf<RocketDisplayable?>(null) }
    val cardResize = remember { MutableLiveData<Boolean?>(false) }

    // Maintain a separate list for the order of the rockets
    var orderedRocketList by remember { mutableStateOf(rocketList) }

    // Create a derived state that depends on orderedRocketList
    val orderedRocketListState by remember { derivedStateOf { orderedRocketList } }

    // Create a state for expanded state of each card
    val expandedListState = remember { mutableStateListOf(*Array(rocketList.size) { false }) }

    // Create a state for the visibility of the LazyColumn
    var isLazyColumnVisible by remember { mutableStateOf(true) }

    Box {
        Column {
            Button(modifier = Modifier.padding(16.dp), colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary ,containerColor = MaterialTheme.colorScheme.surface),onClick = {
                val tempList = expandedListState.toList().toMutableList() // Create a temporary list
                tempList.forEachIndexed { index, _ ->
                    tempList[index] = false // Set all elements to false
                }
                expandedListState.clear() // Clear the original list
                expandedListState.addAll(tempList) // Add all elements from the temporary list
                isLazyColumnVisible = false
            }) {
                Text("Toggle Expand")
            }

            LaunchedEffect(isLazyColumnVisible) {
                if (!isLazyColumnVisible) {
                    // Delay to allow the LazyColumn to disappear
                    kotlinx.coroutines.delay(410)
                    isLazyColumnVisible = true
                }
            }

            AnimatedVisibility(
                visible = isLazyColumnVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 200)) + scaleIn(animationSpec = tween(durationMillis = 200)),
                exit = fadeOut(animationSpec = tween(durationMillis = 200)) + scaleOut(animationSpec = tween(durationMillis = 200))
            ) {
                LazyColumn(state = rememberLazyListState(), modifier = modifier.animateContentSize()) {
                    itemsIndexed(
                        items = orderedRocketListState, // Use the derived state here
                        key = { _, rocket -> rocket.id },
                    ) { index, item ->
                        // Use the index to access and modify the expanded state of each card
                        ExpandableCard(rocket = item,
                            cardResize = cardResize, initialState = expandedListState[index],
                            onRocketClick = {
                                selectedRocket = item
                                // When a card is clicked, move the rocket to the top of the list
                                orderedRocketList = listOf(item) + orderedRocketList.filter { it != item }
                            },
                            onExpandChange = { expanded -> expandedListState[index] = expanded }) // Update the expandedState when it changes
                        if (index < orderedRocketListState.lastIndex) {
                            Divider(modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG).padding(horizontal = 16.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selectedRocket != null,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            selectedRocket?.let { rocket ->
                FullScreenRocketView(rocket, cardResize, onDismiss = {
                    selectedRocket = null
                    // When the BackHandler is called, restore the original order of the list
                    orderedRocketList = rocketList
                })
            }
        }
    }
}