package com.ffpro.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ffpro.settings.core.AppContainer
import com.ffpro.settings.i18n.LocalStrings
import com.ffpro.settings.i18n.stringsFor
import com.ffpro.settings.presentation.viewmodel.MainViewModel
import com.ffpro.settings.presentation.viewmodel.MainViewModelFactory
import com.ffpro.settings.ui.navigation.AppNavGraph
import com.ffpro.settings.ui.theme.ProPlayerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container: AppContainer = (application as App).container

        setContent {
            val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(container))
            val uiState by viewModel.uiState.collectAsState()
            val strings = stringsFor(uiState.language)

            CompositionLocalProvider(LocalStrings provides strings) {
                ProPlayerTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavGraph(viewModel)
                    }
                }
            }
        }
    }
}
