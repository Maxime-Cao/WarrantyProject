package com.warranty.warrantyproject.presenters.views

import android.content.Context

interface CanCreateAddView {
    fun setPeriods(periods : List<String>)

    fun getCurrentContext() : Context?
}