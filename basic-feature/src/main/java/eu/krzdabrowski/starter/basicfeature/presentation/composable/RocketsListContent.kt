package eu.krzdabrowski.starter.basicfeature.presentation.composable

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.wajahatkarim.flippable.FlipAnimationType
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable
import kotlin.math.absoluteValue

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RocketsListContent(
    rocketList: List<RocketDisplayable>,
    modifier: Modifier = Modifier,
    onRocketClick: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()
//    val center = LocalDensity.current.run { (LocalConfiguration.current.screenHeightDp.div(2)).toDp().toPx() }
    val metrics = LocalContext.current.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val screenHeight = metrics.currentWindowMetrics.bounds.height().dp
    val center = LocalDensity.current.run { (screenHeight / 2).toPx() }

    LazyColumn(state = scrollState, modifier = modifier.padding(horizontal = 16.dp)) {
        itemsIndexed(
            items = rocketList,
            key = { _, rocket -> rocket.id },
        ) { index, item ->
            val currentItemInfo = scrollState.layoutInfo.visibleItemsInfo.find { it.index == index }
            val itemCenter = currentItemInfo?.let { it.offset + it.size / 2 }?.toFloat() ?: Float.MAX_VALUE
            val distanceFromCenter = (center - itemCenter).absoluteValue
            val scale = lerp(0.7f, 1f, 1f - (distanceFromCenter / center).coerceIn(0f, 1f))

            val controller = rememberFlipController()

            Flippable(
                frontSide = {
//                    SwipeableCard(rocket = item, onRocketClick = {
//                        controller.flip()
////                        onRocketClick(item.wikiUrl)
//                    }, scale = null)
                    ExpandableCard(rocket = item,title = item.name)
                },
                backSide = {
                },
                flipController = controller,
                flipAnimationType = FlipAnimationType.HORIZONTAL_CLOCKWISE
            )


            if (index < rocketList.lastIndex) {
                Divider(modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG))
            }
        }
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float = (1 - fraction) * start + fraction * stop