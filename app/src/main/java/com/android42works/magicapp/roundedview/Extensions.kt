package com.android42works.magicapp.roundedview
import android.os.Build
import android.view.View

internal fun View.updateOutlineProvider(cornerRadius: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        outlineProvider = RoundOutlineProvider(cornerRadius)
    }
}