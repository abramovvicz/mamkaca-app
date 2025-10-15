package com.abramovvicz.mamkaca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatCard(title: String, totalVotes: Int, positiveVotes: Int, negativeVotes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Kac", value = positiveVotes, color = Color.Red)
                StatItem(label = "Razem", value = totalVotes, color = Color.Blue)
                StatItem(label = "Trzeźwi", value = negativeVotes, color = Color.Green)
            }

            Spacer(modifier = Modifier.height(8.dp))

            val positivePercentage = if (totalVotes > 0) (positiveVotes.toFloat() / totalVotes * 100).toInt() else 0

            LinearProgressIndicator(
                progress = positivePercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                color = Color.Red,
                trackColor = Color.Green
            )

            Text(
                text = "$positivePercentage% osób ma kaca",
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}