package com.fetch.test.feature.list.ui.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fetch.test.core.ui.FetchAppTheme
import com.fetch.test.core.ui.R
import com.fetch.test.core.ui.spacings
import com.fetch.test.domain.model.ListItem
import com.fetch.test.feature.list.ui.action.HomeAction
import com.fetch.test.feature.list.ui.state.HomeState
import com.fetch.test.feature.list.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsState()

    HomeScreenLayout(state = state, onAction = { action ->
        when (action) {
            HomeAction.OnRefreshClick -> {
                viewModel.refreshData()
            }

            HomeAction.OnToggleModeClick -> {
                viewModel.toggleMode()
            }

            is HomeAction.OnTabClick -> {
                viewModel.selectListId(action.id)
            }
        }
    })
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HomeScreenLayout(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {

    val layoutDirection = LocalLayoutDirection.current
    val haptics = LocalHapticFeedback.current
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(scrollState)
    val isTopBarCollapsed = scrollBehavior.state.contentOffset != 0f
    val snackbarHostState = remember { SnackbarHostState() }

    val appBarColor by animateColorAsState(
        targetValue =
            if (isTopBarCollapsed) MaterialTheme.colorScheme.surfaceContainerHighest
            else TopAppBarDefaults.topAppBarColors().containerColor,
        label = "color",
    )

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier.padding(
                            start = MaterialTheme.spacings.extraSmall,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                },
                actions = {
                    FilledTonalIconButton(
                        onClick = {
                            haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                            onAction(HomeAction.OnToggleModeClick)
                        },
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .size(48.dp),
                    ) {
                        Icon(
                            painter = painterResource(
                                if (state.isListMode) R.drawable.ic_grid else R.drawable.ic_list
                            ),
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = null,
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars.union(WindowInsets.displayCutout),
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appBarColor,
                    scrolledContainerColor = appBarColor,
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                    onAction(HomeAction.OnRefreshClick)
                },
                icon = {
                    Icon(
                        painterResource(R.drawable.ic_refresh),
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.refresh)
                    )
                },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = MaterialTheme.spacings.small),
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = {
                Snackbar(
                    snackbarData = it,
                    shape = MaterialTheme.shapes.medium,
                )
            })
        },
    ) { paddingValues ->
        val paddingStart = paddingValues.calculateStartPadding(layoutDirection)
        val paddingEnd = paddingValues.calculateEndPadding(layoutDirection)
        val paddingTop = paddingValues.calculateTopPadding()

        LaunchedEffect(state.error) {
            state.error?.let { snackbarHostState.showSnackbar(context.getString(R.string.error_message)) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingStart,
                    end = paddingEnd,
                    top = paddingTop,
                )
        ) {
            AnimatedVisibility(
                visible = state.isLoading && !isTopBarCollapsed
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(appBarColor)
                        .padding(
                            vertical = MaterialTheme.spacings.medium
                        ),
                )
            }

            // Display tabs if there are list IDs available
            if (state.listIds.isNotEmpty()) {
                SecondaryTabRow(
                    selectedTabIndex = state.listIds.indexOf(state.selectedListId),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    divider = { },
                    modifier = Modifier
                        .background(
                            color = appBarColor
                        )
                        .padding(
                            start = MaterialTheme.spacings.default,
                            end = MaterialTheme.spacings.default,
                            top = MaterialTheme.spacings.small,
                            bottom = MaterialTheme.spacings.medium
                        )
                        .clip(CircleShape),
                    indicator = {},
                ) {
                    state.listIds.forEach { listId ->
                        Tab(
                            selected = listId == state.selectedListId,
                            onClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                                onAction(HomeAction.OnTabClick(listId))
                            },
                            text = { Text("List $listId") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (listId == state.selectedListId) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceContainer
                                ),
                            selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            AnimatedContent(
                targetState = state.selectedListItems,
            ) { items ->
                // Display the list of items for the selected tab
                AnimatedVisibility(
                    visible = state.isListMode,
                    enter = scaleIn(),
                    exit = ExitTransition.None
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = MaterialTheme.spacings.default + 112.dp,
                            start = MaterialTheme.spacings.default,
                            end = MaterialTheme.spacings.default,
                            top = MaterialTheme.spacings.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (items.isEmpty() && state.isLoading.not() && state.error == null) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = MaterialTheme.spacings.large)
                                ) {
                                    LoadingIndicator(
                                        modifier = Modifier
                                            .size(240.dp)
                                            .align(Alignment.Center),
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.ic_article),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .align(Alignment.Center),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Text(
                                    "No items to display for this list ID.",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .alpha(0.7f)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        if (items.isNotEmpty()) {
                            itemsIndexed(items) { index, item ->
                                ListItem(
                                    item = item,
                                    modifier = Modifier.clip(
                                        MaterialTheme.shapes.extraSmall.copy(
                                            topStart = if (index == 0) MaterialTheme.shapes.large.topStart else MaterialTheme.shapes.extraSmall.topStart,
                                            topEnd = if (index == 0) MaterialTheme.shapes.large.topEnd else MaterialTheme.shapes.extraSmall.topEnd,
                                            bottomStart = if (index == items.lastIndex) MaterialTheme.shapes.large.bottomStart else MaterialTheme.shapes.extraSmall.bottomStart,
                                            bottomEnd = if (index == items.lastIndex) MaterialTheme.shapes.large.bottomEnd else MaterialTheme.shapes.extraSmall.bottomEnd

                                        )
                                    )
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = state.isListMode.not(),
                    enter = scaleIn(),
                    exit = ExitTransition.None
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = MaterialTheme.spacings.default + 96.dp,
                            start = MaterialTheme.spacings.default,
                            end = MaterialTheme.spacings.default,
                            top = MaterialTheme.spacings.small
                        ),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.small)
                    ) {
                        items(items.size) { index ->
                            val item = items[index]
                            GridItem(
                                item = item,
                                modifier = Modifier.clip(MaterialTheme.shapes.medium)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(
    item: ListItem,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacings.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium)
    ) {
        FilledIconButton(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.Reject)
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_article),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
        ) {
            Text(
                text = item.name.toString(),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "ID: ${item.id}",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.alpha(0.7f)
            )
        }
    }
}

@Composable
fun GridItem(
    item: ListItem,
    modifier: Modifier = Modifier
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(MaterialTheme.spacings.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledIconButton(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.Reject)
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_article),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacings.small))
        Text(
            text = item.name.toString(),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "ID: ${item.id}",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.alpha(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemListScreen() {
    FetchAppTheme {
        HomeScreenLayout(
            state = HomeState(
                items = listOf(
                    ListItem(id = 1, listId = 1, name = "Item 1"),
                    ListItem(id = 2, listId = 1, name = "Item 2"),
                    ListItem(id = 3, listId = 2, name = "Item 3"),
                    ListItem(id = 4, listId = 2, name = "Item 4"),
                ),
                listIds = listOf(1, 2),
                selectedListId = 1,
                selectedListItems = listOf(
                    ListItem(id = 1, listId = 1, name = "Item 1"),
                    ListItem(id = 2, listId = 1, name = "Item 2"),
                ),
                isListMode = true,
                isLoading = false,
                error = null
            ),
            onAction = {}
        )
    }
}
