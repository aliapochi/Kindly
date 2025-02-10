package com.loeth.kindly.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.loeth.kindly.data.OfflinePromisesRepository
import com.loeth.kindly.data.PromiseDao
import com.loeth.kindly.data.PromiseDatabase
import com.loeth.kindly.data.PromisesRepository
import com.loeth.kindly.domain.usecases.AddPromiseUseCase
import com.loeth.kindly.domain.usecases.DeletePromiseUseCase
import com.loeth.kindly.domain.usecases.GetAllPromisesUseCase
import com.loeth.kindly.domain.usecases.GetPromiseByIdUseCase
import com.loeth.kindly.domain.usecases.UpdatePromiseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PromiseDatabase {
        Log.d("DatabaseCheck", "PromiseDatabase is being created")
        return Room.databaseBuilder(
            context,
            PromiseDatabase::class.java,
            "promise_database"
        ).build()
    }

    @Provides
    fun provideDao(database: PromiseDatabase): PromiseDao {
        return database.promiseDao()
    }

    @Provides
    @Singleton
    fun providePromisesRepository(promiseDao: PromiseDao): PromisesRepository {
        return OfflinePromisesRepository(promiseDao)
    }

    @Provides
    fun provideGetAllPromisesUseCase(promisesRepository: PromisesRepository): GetAllPromisesUseCase {
        return GetAllPromisesUseCase(promisesRepository)
    }

    @Provides
    fun provideGetPromiseByIdUseCase(promisesRepository: PromisesRepository): GetPromiseByIdUseCase {
        return GetPromiseByIdUseCase(promisesRepository)
    }

    @Provides
    fun provideAddPromiseUseCase(promisesRepository: PromisesRepository): AddPromiseUseCase {
        return AddPromiseUseCase(promisesRepository)
    }

    @Provides
    fun provideUpdatePromiseUseCase(promisesRepository: PromisesRepository): UpdatePromiseUseCase {
        return UpdatePromiseUseCase(promisesRepository)
    }

    @Provides
    fun provideDeletePromiseUseCase(promisesRepository: PromisesRepository): DeletePromiseUseCase {
        return DeletePromiseUseCase(promisesRepository)
    }

}