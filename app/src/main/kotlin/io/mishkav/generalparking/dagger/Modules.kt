package io.mishkav.generalparking.dagger

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.mishkav.generalparking.data.repositories.AuthRepository
import io.mishkav.generalparking.data.repositories.DatabaseRepository
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import io.mishkav.generalparking.domain.repositories.IDatabaseRepository
import javax.inject.Singleton

@Module
class FirebaseAuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseDatabase() = Firebase.database.reference
}

@Module
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindIAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Singleton
    @Binds
    fun bindIDatabaseRepository(databaseRepository: DatabaseRepository): IDatabaseRepository
}