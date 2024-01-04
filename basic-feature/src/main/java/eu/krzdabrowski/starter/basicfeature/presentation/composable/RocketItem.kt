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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import eu.krzdabrowski.starter.basicfeature.R
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable

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
//                    CoroutineScope(Dispatchers.Main).launch {
//                        onRocketClick()
//                    }
                    onRocketClick()
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
fun ExpandableGoogleCard(
    rocket: RocketDisplayable,
    cardResize: MutableLiveData<Boolean?>,
    initialState: Boolean,
    onRocketClick: () -> Unit,
    onExpandChange: (Boolean) -> Unit,
    fullscreen: () -> Unit
) {
    var expanded by remember { mutableStateOf(initialState) }
    var imagevisibility by remember { mutableStateOf(false) }

    if (cardResize.value == true) {
        BackHandler {
            fullscreen()
            expanded = !expanded
            onExpandChange(expanded)
            cardResize.value = !cardResize.value!!
        }
    }

    Card(
        modifier = Modifier
//            .fillMaxWidth()
            .animateContentSize()
            .clickable {
                onRocketClick()
            }
            .let { if (cardResize.value == false) it.fillMaxSize() else it.fillMaxWidth() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
        ) {
            AnimatedVisibility(visible = true) {
                AsyncImage(
                    model = rocket.imageUrl,
                    contentDescription = "article image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .let { if (cardResize.value == false) it.padding(16.dp) else it }
                        .let { if (cardResize.value == false) it.clip(RoundedCornerShape(16.dp)) else it }
                        .animateContentSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            {
                Text(
                    modifier = Modifier.let { if (cardResize.value == true) it.padding(top = 16.dp) else it },
                    text = "Enormous rocket explosion wipes town off the face of the map",
                    style = MaterialTheme.typography.headlineSmall,
                    // change font size
                    fontSize = 21.sp,
                )

                Row {
                    Text(
                        text = "Domain - PH",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_fullscreen_24),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                            contentDescription = "fullscreen card",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    fullscreen()
                                    expanded = !expanded
                                    onExpandChange(expanded)
                                    cardResize.value = !cardResize.value!!
                                },
                        )
                        if (cardResize.value == false) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                alignment = Alignment.CenterEnd,
                                painter = painterResource(id = if (expanded) eu.krzdabrowski.starter.core.R.drawable.baseline_arrow_drop_down_24 else eu.krzdabrowski.starter.core.R.drawable.baseline_arrow_drop_up_24),
                                contentDescription = "star",
                                modifier = Modifier
                                    .size(36.dp)
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
                }
                if (expanded) {
                    Card(modifier = Modifier.padding(vertical = 16.dp)) {
                        Column {
                            Text(
                                text = "AI Summary :",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp)
                            )
                            Text(
                                text = "In a tragic turn of events, a recent rocket launch resulted in a catastrophic explosion, leading to the complete devastation of a nearby town. The explosion, occurring shortly after liftoff, unleashed an immense force, leveling buildings and infrastructure, and causing an unknown number of casualties. Emergency response teams and investigators are currently on site, working to understand the cause of the disaster and to provide aid to those affected.",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp, top = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Read more",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Text(
                                    text = "Share",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                                Text(
                                    text = "Archive",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                if (cardResize.value == true) {
                    Card {
                        Column {
                            Text(
                                text = "Analysis :",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp)
                            )
                            Text(
                                text = "Analyzing this hypothetical news article about a catastrophic rocket launch that wiped out an entire town, several key points emerge that warrant a deeper exploration:\n" +
                                        "\n" +
                                        "1. **Cause of the Explosion**: The initial focus of any analysis would be on the cause of the explosion. Typically, rocket launches undergo extensive pre-flight checks and simulations to minimize the risk of failure. The article would likely explore whether the explosion was due to a mechanical failure, a design flaw, or external factors like sabotage or environmental conditions. The specifics of the rocket's design, its fuel type, and the launch procedures would be scrutinized. Additionally, the history of the organization conducting the launch, their safety record, and any previous incidents would be relevant.\n" +
                                        "\n" +
                                        "2. **Impact on the Surrounding Area**: The catastrophic nature of the explosion, with an entire town being wiped out, is a central aspect of the story. The analysis would delve into the scale of the destruction, including the number of casualties, the extent of property damage, and the environmental impact. The proximity of the launch site to populated areas would be questioned, and whether adequate safety measures and evacuation protocols were in place.\n" +
                                        "\n" +
                                        "3. **Emergency Response and Relief Efforts**: The effectiveness of the emergency response would be a critical point of analysis. How quickly did emergency services respond? Were there adequate resources and plans for a disaster of this magnitude? The article would examine the coordination between different agencies, such as local fire departments, medical teams, and national disaster response units. \n" +
                                        "\n" +
                                        "4. **Regulatory and Legal Implications**: The incident would inevitably lead to discussions about the regulatory framework governing space launches and the oversight of such operations. Were there lapses in regulatory compliance? What are the legal ramifications for the company or agency responsible for the launch? The analysis might explore potential lawsuits, government inquiries, or international repercussions if the launch had global stakeholders.\n" +
                                        "\n" +
                                        "5. **Community Impact and Recovery**: The long-term impact on the community would be a poignant aspect of the analysis. This would include the psychological trauma experienced by survivors, the economic repercussions on the region, and the rebuilding process. The resilience of the community, support from other regions or countries, and stories of individual heroism or tragedy would be highlighted.\n" +
                                        "\n" +
                                        "6. **Future of Space Launches**: Lastly, the article would likely discuss the implications of the disaster on future space endeavors. This includes the potential for increased regulations, changes in public perception and support for space exploration, and the impact on the plans of other space agencies and private companies.\n" +
                                        "\n" +
                                        "Throughout the analysis, the article would balance technical details with human stories, emphasizing the tragic loss while seeking to understand how such a disaster occurred and what can be done to prevent similar incidents in the future.",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 16.dp, top = 8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                            }
                        }
                    }

                    Card(
                        Modifier
//                .shadow(4.dp)
                            .padding(vertical = 16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Images",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                AsyncImage(
                                    model = rocket.imageUrl,
                                    contentDescription = "article image",
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f)
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .animateContentSize(),
                                    contentScale = ContentScale.FillWidth
                                )
                                AsyncImage(
                                    model = rocket.imageUrl,
                                    contentDescription = "article image",
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f)
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .animateContentSize(),
                                    contentScale = ContentScale.FillWidth
                                )
                                AsyncImage(
                                    model = rocket.imageUrl,
                                    contentDescription = "article image",
                                    modifier = Modifier
                                        .fillMaxWidth(0.3f)
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .animateContentSize(),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
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
