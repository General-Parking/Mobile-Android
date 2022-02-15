package io.mishkav.generalparking.dagger

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaseAuthModule::class, RepositoriesModule::class])
interface AppComponent {

    // fun inject(authViewModel)
}

