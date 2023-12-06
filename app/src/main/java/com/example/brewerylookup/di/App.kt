package di

import android.app.Application
import android.content.Context
import com.example.inventory.common.AppPreferences
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var appPrefs: AppPreferences

    var disposable = CompositeDisposable()
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        disposable.dispose()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}