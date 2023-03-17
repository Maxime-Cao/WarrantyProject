package com.warranty.warrantyproject.presenters.views

import android.content.Context

interface CanCreateLookView {
    abstract fun setPeriods(periods: List<String>)
    fun getCurrentContext(): Context?
}