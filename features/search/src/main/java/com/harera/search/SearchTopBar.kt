package com.harera.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
internal fun SearchTopBar(
    onSearchSubmitted: (String) -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentPadding = PaddingValues(5.dp),
    ) {
        SearchField {
            onSearchSubmitted(it)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchTopBarPreview() {
    SearchTopBar(
        {}
    )
}