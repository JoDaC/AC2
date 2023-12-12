package eu.krzdabrowski.starter.basicfeature.presentation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.krzdabrowski.starter.basicfeature.presentation.model.RocketDisplayable

const val ROCKET_DIVIDER_TEST_TAG = "rocketDividerTestTag"

@Composable
fun RocketsListContent(
    rocketList: List<RocketDisplayable>,
    modifier: Modifier = Modifier,
    onRocketClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
            ),
    ) {
        itemsIndexed(
            items = rocketList,
            key = { _, rocket -> rocket.id },
        ) { index, item ->
            SwipeableCard(
                rocket = item,
            ) { onRocketClick(item.wikiUrl) }

            if (index < rocketList.lastIndex) {
                Divider(
                    modifier = Modifier.testTag(ROCKET_DIVIDER_TEST_TAG),
                )
            }
        }
    }
}
