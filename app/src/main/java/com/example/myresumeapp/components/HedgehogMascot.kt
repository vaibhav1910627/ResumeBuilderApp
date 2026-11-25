package com.example.myresumeapp.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HedgehogMascot(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp
) {
    // Subtle breathing/floating animation
    val infiniteTransition = rememberInfiniteTransition(label = "mascotFloating")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingY"
    )

    Box(
        modifier = modifier
            .size(size)
            .offset(y = offsetY.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = this.size.width
            val height = this.size.height
            val scale = width / 108f

            // 1. Soft 3D Ambient Shadow Base
            val shadowPath = Path().apply {
                moveTo(28f * scale, 60f * scale)
                cubicTo(24f * scale, 60f * scale, 18f * scale, 64f * scale, 20f * scale, 72f * scale)
                cubicTo(22f * scale, 80f * scale, 32f * scale, 82f * scale, 44f * scale, 82f * scale)
                cubicTo(58f * scale, 82f * scale, 72f * scale, 78f * scale, 76f * scale, 70f * scale)
                cubicTo(80f * scale, 62f * scale, 74f * scale, 54f * scale, 68f * scale, 54f * scale)
                close()
            }
            drawPath(path = shadowPath, color = Color(0x30081075))

            // 2. Warm Cream 3D Hedgehog Body with Quills
            val bodyPath = Path().apply {
                moveTo(26f * scale, 58f * scale)
                cubicTo(22f * scale, 58f * scale, 20f * scale, 54f * scale, 26f * scale, 50f * scale)
                cubicTo(32f * scale, 46f * scale, 34f * scale, 36f * scale, 40f * scale, 38f * scale)
                cubicTo(44f * scale, 32f * scale, 50f * scale, 30f * scale, 56f * scale, 34f * scale)
                cubicTo(62f * scale, 28f * scale, 70f * scale, 32f * scale, 72f * scale, 38f * scale)
                cubicTo(80f * scale, 38f * scale, 84f * scale, 46f * scale, 82f * scale, 54f * scale)
                cubicTo(86f * scale, 60f * scale, 82f * scale, 68f * scale, 76f * scale, 72f * scale)
                cubicTo(68f * scale, 76f * scale, 54f * scale, 78f * scale, 42f * scale, 78f * scale)
                cubicTo(32f * scale, 78f * scale, 28f * scale, 70f * scale, 32f * scale, 64f * scale)
                cubicTo(28f * scale, 64f * scale, 26f * scale, 62f * scale, 26f * scale, 58f * scale)
                close()
            }
            drawPath(path = bodyPath, color = Color(0xFFFFF8E7))

            // 3. Soft 3D Lighting Layer
            val lightingPath = Path().apply {
                moveTo(30f * scale, 56f * scale)
                cubicTo(34f * scale, 48f * scale, 38f * scale, 40f * scale, 44f * scale, 40f * scale)
                cubicTo(48f * scale, 36f * scale, 54f * scale, 34f * scale, 58f * scale, 38f * scale)
                cubicTo(64f * scale, 32f * scale, 70f * scale, 36f * scale, 72f * scale, 42f * scale)
                cubicTo(78f * scale, 44f * scale, 80f * scale, 50f * scale, 78f * scale, 56f * scale)
                cubicTo(72f * scale, 66f * scale, 58f * scale, 74f * scale, 44f * scale, 74f * scale)
                cubicTo(36f * scale, 74f * scale, 34f * scale, 66f * scale, 36f * scale, 60f * scale)
                close()
            }
            drawPath(path = lightingPath, color = Color(0xFFFFEED3).copy(alpha = 0.75f))

            // 4. Hedgehog Eye Dot
            drawCircle(
                color = Color(0xFF081075),
                radius = 2.5f * scale,
                center = Offset(32f * scale, 52f * scale)
            )

            // 5. Inset Document Emblem
            drawRoundRect(
                color = Color(0xFF0F20D6).copy(alpha = 0.85f),
                topLeft = Offset(50f * scale, 48f * scale),
                size = Size(14f * scale, 16f * scale),
                cornerRadius = CornerRadius(3f * scale, 3f * scale)
            )

            // Document Lines
            drawLine(
                color = Color.White,
                start = Offset(53f * scale, 52f * scale),
                end = Offset(59f * scale, 52f * scale),
                strokeWidth = 1.2f * scale
            )
            drawLine(
                color = Color.White,
                start = Offset(53f * scale, 55f * scale),
                end = Offset(61f * scale, 55f * scale),
                strokeWidth = 1.2f * scale
            )
            drawLine(
                color = Color.White,
                start = Offset(53f * scale, 58f * scale),
                end = Offset(57f * scale, 58f * scale),
                strokeWidth = 1.2f * scale
            )
        }
    }
}