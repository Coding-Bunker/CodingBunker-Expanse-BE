package it.github.codingbunker.expanse.app.di

import it.github.codingbunker.expanse.app.viewmodel.login.LoginViewModel
import it.github.codingbunker.expanse.app.viewmodel.login.LoginViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModelImpl(get(), get()) } bind LoginViewModel::class
}