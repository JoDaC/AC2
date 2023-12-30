package eu.krzdabrowski.starter.basicfeature.presentation.composable

import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eu.krzdabrowski.starter.basicfeature.R
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExpandableArticleCard(rocket: RocketDisplayable, cardResize: MutableLiveData<Boolean?>, initialState: Boolean, onRocketClick: () -> Unit, onExpandChange: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(initialState) }
    var imagevisibility by remember { mutableStateOf(false) }

    Card(
//        shape = RoundedCornerShape(16.dp),
        shape = if (cardResize.value == false) RoundedCornerShape(16.dp) else RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                expanded = !expanded
                onExpandChange(expanded)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.animateContentSize()
        ) {
            AnimatedVisibility(visible = expanded) {
                AsyncImage(
                    model = rocket.imageUrl,
                    contentDescription = "article image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .animateContentSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = rocket.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = rocket.firstFlightDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        alignment = Alignment.CenterEnd,
                        painter = painterResource(id = if (expanded) R.drawable.baseline_arrow_upward_24 else R.drawable.baseline_arrow_downward_24),
                        contentDescription = "star",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
            if (expanded) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
                Button(colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.surfaceTint ,containerColor = MaterialTheme.colorScheme.outlineVariant),onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        imagevisibility = false
                        expanded = false
                        delay(350) // delay for 100ms
                        onRocketClick()
                    }
                                 },
                    modifier = Modifier.padding(16.dp)) {
                    Text("Open WebView")
                }
            }
            }
        }
    }

@Composable
fun ExpandableArchiveCard(title: String, rocket: RocketDisplayable, cardResize: MutableLiveData<Boolean?>, initialState: Boolean, onRocketClick: () -> Unit, onExpandChange: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(initialState) }
    var imagevisibility by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(4.dp),
//        shape = if (cardResize.value == false) RoundedCornerShape(16.dp) else RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                expanded = !expanded
                onExpandChange(expanded)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.animateContentSize()
        ) {
//            AnimatedVisibility(visible = expanded) {
//                AsyncImage(
//                    model = rocket.imageUrl,
//                    contentDescription = "article image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(16.dp))
//                        .animateContentSize(),
//                    contentScale = ContentScale.FillWidth
//                )
//            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = rocket.firstFlightDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        alignment = Alignment.CenterEnd,
                        painter = painterResource(id = if (expanded) R.drawable.baseline_arrow_upward_24 else R.drawable.baseline_arrow_downward_24),
                        contentDescription = "star",
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }
            }
            if (expanded) {
//                Text(
//                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier.padding(16.dp)
//                )

                ArchivedList()






//                Button(colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.surfaceTint ,containerColor = MaterialTheme.colorScheme.outlineVariant),onClick = {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        imagevisibility = false
//                        expanded = false
//                        delay(350) // delay for 100ms
//                        onRocketClick()
//                    }
//                },
//                    modifier = Modifier.padding(16.dp)) {
//                    Text("Open WebView")
//                }
            }
        }
    }
}

@Composable
fun FullScreenRocketView(rocket: RocketDisplayable, cardResize: MutableLiveData<Boolean?>, onDismiss: () -> Unit) {
    BackHandler {
        onDismiss()
        cardResize.value = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // you need to get rid of all the code that changes the list order
//            Text(text = "", modifier = Modifier.padding(16.dp))
            WebViewWithPlaceholder(rocket = rocket)
        }
    }
}
@Composable
fun WebViewWithPlaceholder(rocket: RocketDisplayable) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            visibility = View.INVISIBLE
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    visibility = View.VISIBLE
                }
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            loadUrl(rocket.wikiUrl)
        }
    }, modifier = Modifier.fillMaxSize())
}

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
            }

        }
    }
}

//private fun LazyListScope.ArchivedList() {
//    items(10) { index ->
//        ExpandableArticleCard(rocket = RocketDisplayable(
//            id = "id",
//            name = "name",
//            costPerLaunchInMillions = 1,
//            firstFlightDate = "firstFlightDate",
//            heightInMeters = 1,
//            weightInTonnes = 1,
//            wikiUrl = "wikiUrl",
//            imageUrl = "imageUrl",
//        ), cardResize = MutableLiveData<Boolean?>(false), initialState = false, onRocketClick = {}, onExpandChange = {})
//        Divider(modifier = Modifier
//            .testTag(ROCKET_DIVIDER_TEST_TAG)
//            .padding(horizontal = 16.dp),
//            color = Color.DarkGray
//        )
//    }
//}

@Composable
private fun ArchivedList() {
    LazyColumn(modifier = Modifier.height(500.dp)) {
        items(100){
            ExpandableArticleCard(rocket = RocketDisplayable(
                id = "id",
                name = "name",
                costPerLaunchInMillions = 1,
                firstFlightDate = "firstFlightDate",
                heightInMeters = 1,
                weightInTonnes = 1,
                wikiUrl = "wikiUrl",
                imageUrl = "imageUrl",
            ), cardResize = MutableLiveData<Boolean?>(false), initialState = false, onRocketClick = {}, onExpandChange = {})
            Divider(modifier = Modifier
                .testTag(ROCKET_DIVIDER_TEST_TAG)
                .padding(horizontal = 16.dp),
                color = Color.DarkGray
            )
        }
    }
}
