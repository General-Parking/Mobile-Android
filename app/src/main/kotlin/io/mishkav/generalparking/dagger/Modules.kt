package io.mishkav.generalparking.dagger

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.mishkav.generalparking.data.repositories.AuthRepository
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import javax.inject.Singleton

@Module
class FirebaseAuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
}

@Module
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindIAuthRepository(authRepository: AuthRepository): IAuthRepository
}