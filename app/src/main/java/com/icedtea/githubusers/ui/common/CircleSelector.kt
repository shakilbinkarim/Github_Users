package com.icedtea.githubusers.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.icedtea.githubusers.ui.theme.GithubUsersTheme

@Composable
fun CircleLevelSelector(
    modifier: Modifier = Modifier,
    maxLevel: Int,
    onLevelSelect: (Int) -> Unit = {},
    currentLevel: Int = 0,
    activeColor: Color = MaterialTheme.colors.primary,
    inActiveColor: Color = MaterialTheme.colors.secondary, // TODO: change to onDisabled
    selectorScale: Float = 0.4f,
    connectorHeightScale: Float = 0.3f,
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val buttonSize = ((maxWidth.value / (maxLevel)) * selectorScale).dp
        val connectorWidth = (maxWidth - (maxLevel * buttonSize.value).dp) / (maxLevel - 1)
        val connectorHeight = (buttonSize.value * connectorHeightScale).dp

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..maxLevel) {
                LevelSelectorButton(
                    onLevelSelect = onLevelSelect,
                    index = i,
                    isActive = i <= currentLevel && currentLevel != 1,
                    activeColor = activeColor,
                    inActiveColor = inActiveColor,
                    buttonWidth = buttonSize
                )
                if (i != maxLevel) {
                    LevelSelectorButtonConnector(
                        isActive = i < currentLevel,
                        activeColor = activeColor,
                        inActiveColor = inActiveColor,
                        modifier = Modifier.size(
                            height = connectorHeight,
                            width = connectorWidth
                        )
                    )
                }
            }
        }
    }

}

@Composable
private fun LevelSelectorButtonConnector(
    isActive: Boolean = false,
    activeColor: Color,
    inActiveColor: Color,
    modifier: Modifier
) {
    val color = if (isActive) activeColor else inActiveColor
    Divider(
        modifier = modifier,
        color = color
    )
}

@Composable
private fun LevelSelectorButton(
    onLevelSelect: (Int) -> Unit = {},
    index: Int,
    isActive: Boolean = false,
    activeColor: Color,
    inActiveColor: Color,
    buttonWidth: Dp
) {
    val color = if (isActive) activeColor else inActiveColor
    Button(
        onClick = {
            onLevelSelect(index)
        },
        colors = ButtonDefaults.buttonColors(color),
        shape = CircleShape,
        modifier = Modifier.size(buttonWidth),
    ) {}
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GithubUsersTheme {
        CircleLevelSelector(
            modifier = Modifier.padding(horizontal = 20.dp),
            maxLevel = 7,
            currentLevel = 5
        )
    }
}