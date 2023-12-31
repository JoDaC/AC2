package eu.krzdabrowski.starter.basicfeature.presentation.composable

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eu.krzdabrowski.starter.basicfeature.R
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable
import eu.krzdabrowski.starter.core.design.Typography
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@Composable
fun SwipeableCard(rocket: RocketDisplayable, onRocketClick: () -> Unit, scale: Float? = null) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .clickable { onRocketClick() }
        .clip(RoundedCornerShape(16.dp))
        .graphicsLayer {
            scaleX = scale ?: 1f
            scaleY = scale ?: 1f
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = rocket.imageUrl,
                contentDescription = "article image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillWidth
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Title", style = MaterialTheme.typography.titleLarge)
//                Text(text = "Subtitle", style = MaterialTheme.typography.bodyMedium)
            }

        }
    }
}

@Composable
fun ExpandableCard(rocket: RocketDisplayable, title: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                expanded = !expanded
            }
    ) {
        Column(
            modifier = Modifier.animateContentSize()
        ) {
            AsyncImage(
                model = rocket.imageUrl,
                contentDescription = "article image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.FillWidth
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Text(text = rocket.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        alignment = Alignment.CenterEnd,
                        painter = painterResource(id = if (expanded) R.drawable.baseline_arrow_upward_24 else R.drawable.baseline_arrow_downward_24),
                        contentDescription = "star",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
//                Text(text = "Subtitle", style = MaterialTheme.typography.bodyMedium)
            }
//            Text(
//                text = title,
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier.padding(8.dp)
//            )
            if (expanded) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun ExpandableCardPreview() {
//    val rocket = RocketDisplayable(
//        id = "1",
//        name = "Falcon 1",
//        costPerLaunchInMillions = 5,
//        firstFlightDate = "2006-03-24",
//        heightInMeters = 22,
//        weightInTonnes = 1,
//        wikiUrl = "https://en.wikipedia.org/wiki/Falcon_1",
//        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5d/Falcon_1_Flight_4_liftoff.jpg/1920px-Falcon_1_Flight_4_liftoff.jpg"
//    )
//    ExpandableCard(rocket = rocket, title = rocket.name)
//}