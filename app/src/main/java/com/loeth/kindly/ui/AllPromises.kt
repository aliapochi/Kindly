package com.loeth.kindly.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loeth.kindly.KindlyViewModel
import com.loeth.kindly.domain.Promise

@Composable
fun AllPromises(viewModel: KindlyViewModel) {
    val promises by viewModel.promises.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (promises.isEmpty()) {
            Text(
                "No promises Yet!",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(promises) { promise ->
                    PromiseCard(promise)
                }
            }
        }
    }
}

@Composable
fun PromiseCard(promise: Promise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = promise.title, style = MaterialTheme.typography.titleMedium)
            Text(text = promise.category, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Due Date : ${promise.dueDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status : ${if (promise.isFulfilled) "Fulfilled" else "Pending"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
