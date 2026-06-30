package com.jshu.innovates.di

import com.jshu.innovates.domain.BirthdateValidator
import com.jshu.innovates.domain.PanValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePanValidator(): PanValidator = PanValidator()

    @Provides
    @Singleton
    fun provideBirthdateValidator(): BirthdateValidator = BirthdateValidator()
}
