package com.example.horizontalpagerproblem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.horizontalpagerproblem.ui.theme.HorizontalpagerproblemTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

/**
 * Simple test to have an anim of height on HorizontalPager while swaping. The Horizontalpager is in a LazyColumn.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HorizontalpagerproblemTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    ListView()
                }
            }
        }
    }
}

@Composable
fun ListView() {
    val scrollState = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState
        ) {
            items(30) { index ->
                HorizontalPagerElement()
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerElement() {
    val pagerState = rememberPagerState()
    val heightArray = remember { mutableListOf<Int>(200, 400, 300) }
    val pageHeight = remember { mutableStateOf(200) }

    Box(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            count = 3,
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.Yellow)
                .fillMaxWidth()
                .height(pageHeight.value.dp)
        ) { page ->

            val backgroundColor = if (page == 0) {
                Color.Blue
            } else if (page == 1) {
                Color.Red
            } else {
                Color.Yellow
            }
            Row(modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    animHeight(
                        page,
                        targetPage = pagerState.targetPage,
                        pageOffset = pageOffset,
                        heightArray = heightArray
                    ) { heigth ->
                        pageHeight.value = heigth
                    }
                }) {

            }
        }

        HorizontalPagerIndicator(
            indicatorWidth = 8.dp,
            indicatorHeight = 8.dp,
            activeColor = Color.Black,
            inactiveColor = Color.Black.copy(alpha = 0.2f),
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
        )

        // Bottom gloss
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.3f))
        )
    }
}

fun animHeight(
    currentPage: Int,
    targetPage: Int,
    pageOffset: Float,
    heightArray: MutableList<Int>,
    updateHeight: (heigth: Int) -> Unit
) {
    val currentPageHeight = heightArray[currentPage]
    val targetPageHeight = heightArray[targetPage]

    if (currentPageHeight == targetPageHeight) {
        return
    }

    Log.v(
        "Checking",
        "currentPage" + currentPage + " nextPage=" + targetPage + " pageOffset=" + pageOffset + " currentPageHeight=" + currentPageHeight + " targetPageHeight=" + targetPageHeight
    )

    val height = lerp(
        start = currentPageHeight,
        stop = targetPageHeight,
        fraction = pageOffset.coerceIn(0f, 1f)
    )

    updateHeight(height)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HorizontalpagerproblemTheme {
        ListView()
    }
}