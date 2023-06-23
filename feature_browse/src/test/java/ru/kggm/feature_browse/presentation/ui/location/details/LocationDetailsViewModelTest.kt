package ru.kggm.feature_browse.presentation.ui.location.details

import app.cash.turbine.test
import io.kotest.assertions.timing.eventually
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.setMain
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.locations.details.LocationDetailsViewModel
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import ru.kggm.feature_browse.presentation.ui.utility.genericLocationEntity
import kotlin.time.Duration.Companion.seconds

@Suppress("NAME_SHADOWING")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalKotest::class)
class LocationDetailsViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    Dispatchers.setMain(Dispatchers.Default)

    val loadDuration = 10.seconds

    val getLocationById = mockk<GetLocationById>()
    val viewModel = LocationDetailsViewModel(getLocationById)

    Given("location is loaded") {
        val location = genericLocationEntity.copy(id = 123)

        coEvery { getLocationById(location.id) } returns location

        viewModel.loadLocation(location.id.toLong())
        val initialLocation = eventually(loadDuration) { viewModel.location.first() }

        When("loading same location") {
            viewModel.loadLocation(location.id.toLong())

            Then("location does not change") {
                viewModel.location.test {
                    awaitItem().shouldBeSameInstanceAs(initialLocation)
                    expectNoEvents()
                }
            }
        }
    }

    Given("location is not loaded") {

        Given("location will be loaded") {
            val location = genericLocationEntity.copy(id = 123)

            coEvery { getLocationById(location.id) } returns location

            When("after waiting for loading to complete") {
                val expectedResult = LoadResult(
                    location.toPresentationEntity(),
                    LoadingState.Loaded
                )

                viewModel.loadLocation(location.id.toLong())

                Then("returns location & load state 'Loaded'") {
                    viewModel.location.test {
                        awaitItem().shouldBe(expectedResult)
                    }
                }
            }
        }

        Given("location won't be loaded") {
            coEvery { getLocationById(genericLocationEntity.id) } returns null

            val expectedResult = LoadResult(null, LoadingState.Error)

            When("loading completes") {
                viewModel.loadLocation(genericLocationEntity.id.toLong())

                Then("returns null & load state is Error") {
                    viewModel.location.test {
                        awaitItem().shouldBe(expectedResult)
                    }
                }
            }
        }
    }
})
