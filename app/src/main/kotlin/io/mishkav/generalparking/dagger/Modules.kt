package io.mishkav.generalparking.dagger

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.mishkav.generalparking.data.repositories.AuthRepository
import io.mishkav.generalparking.data.repositories.UserDatabaseRepository
import io.mishkav.generalparking.data.repositories.MapDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import io.mishkav.generalparking.domain.repositories.IUserDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import javax.inject.Singleton

@Module
class DataModules {

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase() = Firebase.database.reference

    @Provides
    @Singleton
    fun provideSession(context: Context) = Session(context)
}

@Module
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindIAuthRepository(authRepository: AuthRepository): IAuthRepository

    @Singleton
    @Binds
    fun bindIAuthDatabaseRepository(authDatabaseRepository: UserDatabaseRepository): IUserDatabaseRepository

    @Singleton
    @Binds
    fun bindIMapDatabaseRepository(mapDatabaseRepository: MapDatabaseRepository): IMapDatabaseRepository
}