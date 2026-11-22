package com.example.myresumeapp.components

import android.widget.Space
import androidx.compose.foundation.Image
import com.example.myresumeapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import com.example.myresumeapp.core.utils.AtsScoreResult
import com.example.myresumeapp.core.utils.AtsSuggestion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtsScoreBottomSheet(
    atsResult: AtsScoreResult,
    onDismiss: () -> Unit,
    onNavigateToSection: (String) -> Unit

) {
    ModalBottomSheet(
        onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            ModalBottomSheet(
                onDismiss,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = "ATS Score",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ATS Completeness Score",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val scoreColor = when {
                        atsResult.overallScore >= 85 -> Color(0xFF2E7D32)
                        atsResult.overallScore >= 40 && atsResult.overallScore <85 -> Color(0xFFF57C00)
                        else -> Color(0xFFD32F2F)
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = scoreColor.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "${atsResult.overallScore}%",
                                    fontSize = 38.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = scoreColor
                                )
                                Text(
                                    text = "Grade: ${atsResult.grade}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = scoreColor
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(scoreColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (atsResult.overallScore >= 85) {

                                        Text(
                                            text = "ATS Ready ",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = scoreColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.hamster3),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                    else if(atsResult.overallScore < 85 && atsResult.overallScore> 40) {
                                        Text(
                                            text = "Okay, but not acceptable",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = scoreColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Image(
                                            painter = painterResource(id = R.drawable.ham2sticker),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                    } else {
                                        Text(

                                            text = "Optimization Needed ",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = scoreColor,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Image(

                                            painter = painterResource(id = R.drawable.ham1sticker),
                                            contentDescription = null,

                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    Text(
                                        text = "Section breakdown",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                item {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                                alpha = 0.5f
                                            )
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            atsResult.sectionScores.forEach { s ->
                                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = s.sectionName,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Text(
                                                            text = "${s.score}/${s.maxScore}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    LinearProgressIndicator(
                                                        progress = { if (s.maxScore > 0) s.score.toFloat() / s.maxScore else 0f },
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(6.dp)
                                                            .clip(CircleShape),
                                                        color = if (s.score == s.maxScore) Color(
                                                            0xFF2E7D32
                                                        ) else MaterialTheme.colorScheme.primary,
                                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                                    )
                                                }

                                            }
                                        }
                                    }
                                }
                                item {
                                    Text(
                                        text = "Actionable Recommendations (${atsResult.suggestions.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                if (atsResult.suggestions.isEmpty()) {
                                    item {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(
                                                    0xFF2E7D32
                                                ).copy(alpha = 0.1f)
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color(0xFF2E7D32)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "Your resume is fully optimized for ATS parsers!",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = Color(0XFF2E7D32),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    items(atsResult.suggestions) { suggestion ->
                                        AtsSuggestionCard (
                                            suggestion = suggestion,
                                            onFixNow = {
                                                onDismiss()
                                                onNavigateToSection(suggestion.targetRoute)
                                            }
                                        )
                                    }
                                }
                                item {
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AtsSuggestionCard(
    suggestion: AtsSuggestion,
    onFixNow: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = suggestion.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "+${suggestion.potentialPoints}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = suggestion.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onFixNow,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)) {
            Text(text = "Fix", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

