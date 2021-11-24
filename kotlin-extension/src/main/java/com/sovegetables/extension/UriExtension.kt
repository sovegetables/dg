package com.sovegetables.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.DrawableRes

fun Context.toUri(@DrawableRes drawableId:Int): Uri {
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + this.resources.getResourcePackageName(drawableId)
                + '/'.toString() + this.resources.getResourceTypeName(drawableId)
                + '/'.toString() + this.resources.getResourceEntryName(drawableId)
    )
}

fun Context.toStringUrl(@DrawableRes drawableId:Int): String {
    return (ContentResolver.SCHEME_ANDROID_RESOURCE +
            "://" + this.resources.getResourcePackageName(drawableId)
            + '/'.toString() + this.resources.getResourceTypeName(drawableId)
            + '/'.toString() + this.resources.getResourceEntryName(drawableId))
}