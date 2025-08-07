package com.robsonribeiro

import androidx.compose.ui.window.application
import com.robsonribeiro.component.window.ClientWindow
import com.robsonribeiro.viewmodel.MainViewModel

fun main() = application {
    ClientWindow(
        exitApplication = ::exitApplication,
    ) {
        App(viewModel = MainViewModel())
    }
}