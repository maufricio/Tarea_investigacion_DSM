package com.dsm.lugaresudb

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun PicassoImage(url: String, contentDescription: String?, modifier: Modifier = Modifier) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current

    DisposableEffect(url) {
        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.asImageBitmap()
            }

            override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {
                e?.printStackTrace()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        }

        Picasso.get().load(url).into(target)

        onDispose { /* cleanup si es necesario */ }
    }

    image?.let {
        Image(
            bitmap = it,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}