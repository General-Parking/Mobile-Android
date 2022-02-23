package io.mishkav.generalparking.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.MainViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseAuthModule::class, RepositoriesModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    fun inject(authViewModel: AuthViewModel)
    fun inject(mainViewModel: MainViewModel)
}

