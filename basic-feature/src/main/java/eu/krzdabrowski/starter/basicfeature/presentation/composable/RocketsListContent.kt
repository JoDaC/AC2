package eu.krzdabrowski.starter.basicfeature.presentation.composable

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.MutableLiveData
import com.wajahatkarim.flippable.FlipAnimationType
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable
import kotlin.math.absoluteValue

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@RequiresApi(Build.VERSION_CODES.R)
//@Composable
//fun RocketsListContent(
//    rocketList: List<RocketDisplayable>,
//    modifier: Modifier = Modifier,
//    onRocketClick: (String) -> Unit,
//) {
//    val scrollState = rememberLazyListState()
//    LazyColumn(state = scrollState, modifier = modifier.animateContentSize()) {
//        itemsIndexed(
//            items = rocketList,
//            key = { _, rocket -> rocket.id },
//        ) { index, item ->
//            val controller = rememberFlipController()
//            Flippable(
//                frontSide = {
//                    ExpandableCard(rocket = item,title = item.name)
//                },
//                backSide = {
//                },
//                flipController = controller,
//                flipAnimationType = FlipAnimationType.HORIZONTAL_CLOCKWISE
//            )
//
//
//            if (index < rocketList.lastIndex) {
//                Divider(modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG))
//            }
//        }
//    }
//}

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

    Box {
        LazyColumn(state = rememberLazyListState(), modifier = modifier.animateContentSize()) {
            itemsIndexed(
                items = orderedRocketListState, // Use the derived state here
                key = { _, rocket -> rocket.id },
            ) { index, item ->
                ExpandableCard(rocket = item,
                    cardResize = cardResize, onRocketClick = {
                        selectedRocket = item
                        // When a card is clicked, move the rocket to the top of the list
                        orderedRocketList = listOf(item) + orderedRocketList.filter { it != item }
                    })
                if (index < orderedRocketListState.lastIndex) {
                    Divider(modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG))
                }
            }
        }

        AnimatedVisibility(
            visible = selectedRocket != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
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