package com.mE.Health.di.qualifiers

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockDb

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppDb