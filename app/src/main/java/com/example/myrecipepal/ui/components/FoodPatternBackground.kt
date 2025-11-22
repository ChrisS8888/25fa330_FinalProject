package com.example.myrecipepal.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myrecipepal.R

@Composable
fun FoodPatternBackground(
    modifier: Modifier = Modifier,
    alpha: Float = 0.8f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // INCREASED ALPHA: 0.8f makes them colorful but slightly transparent so text is readable.
        // Set to 1.0f if you want them fully solid.
       // val alpha = 0.8f

        // --- TOP SECTION ---
        AnimatedPixelFood(
            drawableId = R.drawable.tomato,
            startX = (-20).dp, startY = 50.dp,
            size = 50.dp, alpha = alpha, durationMillis = 4000
        )
        AnimatedPixelFood(
            drawableId = R.drawable.onion,
            startX = 150.dp, startY = (-30).dp,
            size = 45.dp, alpha = alpha, durationMillis = 4800
        )
        AnimatedPixelFood(
            drawableId = R.drawable.steak,
            startX = 320.dp, startY = 20.dp,
            size = 60.dp, alpha = alpha, durationMillis = 5200
        )
        AnimatedPixelFood(
            drawableId = R.drawable.chicken,
            startX = 80.dp, startY = 120.dp,
            size = 55.dp, alpha = alpha, durationMillis = 4200
        )

        // --- MIDDLE SECTION ---
        AnimatedPixelFood(
            drawableId = R.drawable.fishsteak,
            startX = (-40).dp, startY = 250.dp,
            size = 65.dp, alpha = alpha, durationMillis = 4500
        )
        AnimatedPixelFood(
            drawableId = R.drawable.bread, // Reuse tomato or add new image
            startX = 280.dp, startY = 220.dp,
            size = 40.dp, alpha = alpha, durationMillis = 5800
        )
        AnimatedPixelFood(
            drawableId = R.drawable.lemon,
            startX = 100.dp, startY = 350.dp,
            size = 50.dp, alpha = alpha, durationMillis = 5000
        )
        AnimatedPixelFood(
            drawableId = R.drawable.avocado,
            startX = 350.dp, startY = 400.dp,
            size = 55.dp, alpha = alpha, durationMillis = 4100
        )
        AnimatedPixelFood(
            drawableId = R.drawable.olive,
            startX = (-10).dp, startY = 450.dp,
            size = 45.dp, alpha = alpha, durationMillis = 4700
        )

        // --- BOTTOM SECTION ---
        AnimatedPixelFood(
            drawableId = R.drawable.potato,
            startX = 200.dp, startY = 520.dp,
            size = 60.dp, alpha = alpha, durationMillis = 5300
        )
        AnimatedPixelFood(
            drawableId = R.drawable.shrimp,
            startX = 20.dp, startY = 600.dp,
            size = 50.dp, alpha = alpha, durationMillis = 4400
        )
        AnimatedPixelFood(
            drawableId = R.drawable.apple,
            startX = 300.dp, startY = 650.dp,
            size = 45.dp, alpha = alpha, durationMillis = 5600
        )
        AnimatedPixelFood(
            drawableId = R.drawable.steak,
            startX = 120.dp, startY = 700.dp,
            size = 55.dp, alpha = alpha, durationMillis = 4900
        )
        AnimatedPixelFood(
            drawableId = R.drawable.chicken,
            startX = (-25).dp, startY = 750.dp,
            size = 50.dp, alpha = alpha, durationMillis = 5100
        )
        AnimatedPixelFood(
            drawableId = R.drawable.fishsteak,
            startX = 260.dp, startY = 780.dp,
            size = 65.dp, alpha = alpha, durationMillis = 4600
        )

        // --- Main Screen Content ---
        content()
    }
}

@Composable
fun AnimatedPixelFood(
    @DrawableRes drawableId: Int,
    startX: Dp,
    startY: Dp,
    size: Dp,
    alpha: Float,
    durationMillis: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating_pixel_food")

    // Animate Y position (bobbing)
    val offsetY by infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = 20.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    // Animate Rotation (rocking)
    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis + 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        modifier = Modifier
            .offset(x = startX, y = startY + offsetY)
            .rotate(rotation)
            .size(size)
            .alpha(alpha)
    )
}