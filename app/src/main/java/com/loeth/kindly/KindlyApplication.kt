package com.loeth.kindly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KindlyApplication : Application() {
    //val database by lazy { PromiseDatabase.getDatabase(this) }
}
