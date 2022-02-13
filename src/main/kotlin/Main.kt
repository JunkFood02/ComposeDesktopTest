// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    DesktopMaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        )
        {
            val musicDiaryList = remember { mutableStateListOf<MusicDairyItem>() }
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            val scrollState = rememberScrollState()
            musicDiaryList.add(MusicDairyItem("菜狗 1", getDate(), "bg_silent.jpg"))
            musicDiaryList.add(MusicDairyItem("菜狗 2", getDate(), "bg_groove.jpg"))
            musicDiaryList.add(MusicDairyItem("菜狗 3", getDate(), "bg_hopeful.jpg"))
            musicDiaryList.add(MusicDairyItem("菜狗 4", getDate(), "bg_passion.jpg"))
            musicDiaryList.add(MusicDairyItem("菜狗 5", getDate(), "bg_romance.jpg"))
            LazyColumn(
                state = listState, modifier = Modifier.draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            listState.scrollBy(-delta)
                        }
                    },
                )
            ) {
                itemsIndexed(musicDiaryList) { _, item ->
                    GreetingCard(
                        date = item.date,
                        title = item.title,
                        resPath = item.resPath, {
                            makeToast(item.title)
                        }, {
                            makeToast(item.title + "(long click)")
                        }
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize().padding(36.dp),
                contentAlignment = Alignment.BottomEnd
            ) {

                FAB() {
                    coroutineScope.launch {
                    }
                }
            }
        }
    }
}


fun getDate(): String {
    val simpleDateFormat = SimpleDateFormat("MM月dd日 E HH:mm", Locale.CHINA)
    return simpleDateFormat.format(Date())
}

fun makeToast(title: String) {

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun GreetingCard(
    date: String = getDate(),
    title: String = "菜狗",
    resPath: String = "bg_romance.jpg",
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 12.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            Modifier.combinedClickable(
                enabled = true,
                null,
                onLongClick = onLongClick,
                onClick = onClick
            )
        ) {
            Image(
                painter = painterResource(resPath), contentDescription = "Title Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(width = Dp.Unspecified, height = 150.dp)
            )
            Column(
                modifier = Modifier.padding(12.dp, 9.dp)
            ) {
                Text(
                    text = "# $date",
                )
                Text(
                    text = title,
                )
            }
        }
    }

}

@Composable
fun FAB(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier
            .size(64.dp), onClick = onClick
    ) {
        Icon(
            painter = painterResource("round_whatshot_white_24.png"),
            contentDescription = "add new item"
        )
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
