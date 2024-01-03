package eu.krzdabrowski.starter.basicfeature.presentation.composable

import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import eu.krzdabrowski.starter.basicfeature.R
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ExpandableArticleCard(
    rocket: RocketDisplayable,
    cardResize: MutableLiveData<Boolean?>,
    initialState: Boolean,
    onRocketClick: () -> Unit,
    onExpandChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(initialState) }
    var imagevisibility by remember { mutableStateOf(false) }

    Card(
//        shape = RoundedCornerShape(16.dp),
        shape = if (cardResize.value == false) RoundedCornerShape(16.dp) else RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                    CoroutineScope(Dispatchers.Main).launch {
//                        delay(350)
                        onRocketClick()
                    }
                }) {
                    Column {
                        Text(text = rocket.name, style = MaterialTheme.typography.headlineMedium)
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
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                expanded = !expanded
                                onExpandChange(expanded)
                            },
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }
            if (expanded) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ExpandableArchiveCard(
    title: String,
    rocket: RocketDisplayable,
    initialState: Boolean,
    onExpandChange: (Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(initialState) }
    var imagevisibility by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(4.dp),
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
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = title, style = MaterialTheme.typography.headlineLarge)
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
                ArchivedList()
            }
        }
    }
}

@Composable
fun WebViewWithPlaceholder(rocket: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            visibility = View.INVISIBLE
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
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
            loadUrl(rocket)
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
private fun ArchivedList() {
    LazyColumn(modifier = Modifier.height(500.dp)) {
        items(100) {
            ExpandableArticleCard(rocket = RocketDisplayable(
                id = "id",
                name = "name",
                costPerLaunchInMillions = 1,
                firstFlightDate = "firstFlightDate",
                heightInMeters = 1,
                weightInTonnes = 1,
                wikiUrl = "wikiUrl",
                imageUrl = "imageUrl",
            ),
                cardResize = MutableLiveData<Boolean?>(false),
                initialState = false,
                onRocketClick = {},
                onExpandChange = {})
            Divider(
                modifier = Modifier
                    .testTag(ROCKET_DIVIDER_TEST_TAG)
                    .padding(horizontal = 16.dp),
                color = Color.DarkGray
            )
        }
    }
}
