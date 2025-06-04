package com.aio.fe_music_player.screens.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import com.aio.fe_music_player.screens.theme.FE_Music_PlayerTheme

@Composable
fun MainScreen() {

    SetSystemBarsDarkIcons(useDarkIcons = false) // Status Bar 아이콘을 흰색으로 처리하는 부분

    val tabs = listOf("폴더", "나를 위한", "노래", "재생 목록", "나를 위한", "노래", "재생 목록")
    var selectedTab by remember { mutableStateOf("폴더") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            // Toolbar 부분
            MyToolbar()

            Spacer(modifier = Modifier.height(20.dp)) // 위쪽 마진 효과

            MyTag(
                tabs, selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
fun MyToolbar(
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                    append("JH")
                }
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append(" MUSIC")
                }
            },
            fontSize = 20.sp,

            )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "YouTube",
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }

    }
}

@Composable
fun MyTag(
    tagList: List<String>,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(start = 12.dp), // 👈 여기!
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tagList) { tab ->
            val isSelected = tab == selectedTab
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Color.White else Color.DarkGray)
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tab,
                    color = if (isSelected) Color.Black else Color.White,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }


}

// Status Bar 아이콘을 흰색으로 처리하는 부분
@Composable
fun SetSystemBarsDarkIcons(useDarkIcons: Boolean) {
    val view = LocalView.current
    val activity = view.context as? Activity

    SideEffect {
        activity?.window?.let { window ->
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = useDarkIcons // false = white icons
            controller.isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}

@Preview
@Composable
fun showMain() {
    FE_Music_PlayerTheme {
        MainScreen()
    }
}
