package com.flipperdevices.main.impl.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flipperdevices.core.ui.ktx.OrangeAppBarWithIcon
import com.flipperdevices.core.ui.res.R as DesignSystem
import com.flipperdevices.main.impl.R
import com.flipperdevices.main.impl.composable.switch.ComposableFapHubNewSwitch
import com.flipperdevices.main.impl.composable.switch.ComposableFapHubSwitch
import com.flipperdevices.main.impl.model.FapHubTabEnum
import com.flipperdevices.main.impl.viewmodel.InstalledNotificationViewModel
import com.flipperdevices.main.impl.viewmodel.MainViewModel
import tangle.viewmodel.compose.tangleViewModel

@Composable
fun ComposableFapHubMainScreen(
    onBack: () -> Unit,
    catalogTabComposable: @Composable () -> Unit,
    installedTabComposable: @Composable () -> Unit,
    onOpenSearch: () -> Unit
) {
    val installedNotificationViewModel = viewModel<InstalledNotificationViewModel>()
    val installedNotificationCount by
    installedNotificationViewModel.getNotificationCountStateFlow().collectAsState()

    val viewModel = tangleViewModel<MainViewModel>()
    val selectedTab by viewModel.getTabFlow().collectAsState()
    val isNewBar by viewModel.isExperimentalSwitch().collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isNewBar) {
            ComposableFapHubNewSwitch(
                fapHubTabEnum = selectedTab,
                onSelect = viewModel::onSelectTab,
                installedNotificationCount = installedNotificationCount,
                onBack = onBack,
                onEndClick = onOpenSearch
            )
            Spacer(
                modifier = Modifier
                    .height(18.dp)
                    .fillMaxWidth()
            )
        } else {
            OrangeAppBarWithIcon(
                titleId = R.string.faphub_main_title,
                onBack = onBack,
                endIconId = DesignSystem.drawable.ic_search,
                onEndClick = onOpenSearch
            )

            ComposableFapHubSwitch(
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp),
                fapHubTabEnum = selectedTab,
                onSelect = viewModel::onSelectTab,
                installedNotificationCount = installedNotificationCount
            )
        }

        when (selectedTab) {
            FapHubTabEnum.APPS -> catalogTabComposable()
            FapHubTabEnum.INSTALLED -> installedTabComposable()
        }
    }
}
