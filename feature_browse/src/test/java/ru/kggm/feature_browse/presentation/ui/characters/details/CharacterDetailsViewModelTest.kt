package ru.kggm.feature_browse.presentation.ui.characters.details

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
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.entities.LocationPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.utility.genericCharacterEntity
import ru.kggm.feature_browse.presentation.ui.utility.genericLocationEntity
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import kotlin.time.Duration.Companion.seconds

@Suppress("NAME_SHADOWING")
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalKotest::class)
class CharacterDetailsViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    Dispatchers.setMain(Dispatchers.Default)

    val loadDuration = 10.seconds

    val getCharacterById = mockk<GetCharacterById>()
    val getLocationById = mockk<GetLocationById>()
    val viewModel = CharacterDetailsViewModel(getCharacterById, getLocationById)

    Given("character is loaded") {
        val character = genericCharacterEntity.copy(id = 123)

        coEvery { getCharacterById(character.id) } returns character

        viewModel.loadCharacter(character.id.toLong())
        val initialCharacter = eventually(loadDuration) { viewModel.character.first() }

        When("loading same character") {
            viewModel.loadCharacter(character.id.toLong())

            Then("character does not change") {
                viewModel.character.test {
                    awaitItem().shouldBeSameInstanceAs(initialCharacter)
                    expectNoEvents()
                }
            }
        }
    }

    Given("character is not loaded") {

        Given("character will be loaded") {
            val character = genericCharacterEntity.copy(id = 123)

            coEvery { getCharacterById(character.id) } returns character

            When("after waiting for loading to complete") {
                val expectedResult = LoadResult(
                    character.toPresentationEntity(),
                    LoadingState.Loaded
                )

                viewModel.loadCharacter(character.id.toLong())

                Then("returns character & load state 'Loaded'") {
                    viewModel.character.test {
                        awaitItem().shouldBe(expectedResult)
                    }
                }
            }

            Given("character has origin") {
                val character = character.copy(id = 123, originId = 456)
                val origin = genericLocationEntity.copy(id = 456)

                coEvery { getCharacterById(character.id) } returns character

                Given("origin will be loaded") {
                    coEvery { getLocationById(origin.id) } returns origin

                    When("after waiting for loading to complete") {
                        val expectedResult =
                            LoadResult(origin.toPresentationEntity(), LoadingState.Loaded)
                        viewModel.loadCharacter(character.id.toLong())

                        Then("returns origin & load state 'Loaded'") {
                            viewModel.origin.test {
                                awaitItem().shouldBe(expectedResult)
                            }
                        }
                    }
                }

                Given("origin won't be loaded") {
                    coEvery { getLocationById(origin.id) } returns null

                    When("after waiting for loading to complete") {
                        val expectedResult = LoadResult(null, LoadingState.Error)
                        viewModel.loadCharacter(character.id.toLong())

                        Then("returns null & load state 'Error'") {
                            viewModel.origin.test {
                                awaitItem().shouldBe(expectedResult)
                            }
                        }
                    }
                }
            }

            Given("character has no origin") {
                val character = genericCharacterEntity.copy(id = 123, originId = null)

                coEvery { getCharacterById(character.id) } returns character

                When("after waiting for loading to complete") {
                    val expectedResult = LoadResult(null, LoadingState.Loaded)
                    viewModel.loadCharacter(character.id.toLong())

                    Then("returns null & load state 'Loaded'") {
                        viewModel.origin.test {
                            awaitItem().shouldBe(expectedResult)
                        }
                    }
                }
            }

            Given("character has location") {
                val character = genericCharacterEntity.copy(id = 123, locationId = 456)
                val location = genericLocationEntity.copy(id = 456)

                coEvery { getCharacterById(character.id) } returns character

                Given("location will be loaded") {
                    coEvery { getLocationById(location.id) } returns location

                    When("after waiting for loading to complete") {
                        val expectedResult =
                            LoadResult(location.toPresentationEntity(), LoadingState.Loaded)
                        viewModel.loadCharacter(character.id.toLong())

                        Then("returns location & load state 'Loaded'") {
                            viewModel.location.test {
                                awaitItem().shouldBe(expectedResult)
                            }
                        }
                    }
                }

                Given("location won't be loaded") {
                    coEvery { getLocationById(location.id) } returns null

                    When("after waiting for loading to complete") {
                        val expectedResult = LoadResult(null, LoadingState.Error)
                        viewModel.loadCharacter(character.id.toLong())

                        Then("returns null & load state 'Error'") {
                            viewModel.location.test {
                                awaitItem().shouldBe(expectedResult)
                            }
                        }
                    }
                }
            }

            Given("character has no location") {
                val character = genericCharacterEntity.copy(id = 123, locationId = null)

                coEvery { getCharacterById(character.id) } returns character

                When("after waiting for loading to complete") {
                    val expectedResult = LoadResult(null, LoadingState.Loaded)
                    viewModel.loadCharacter(character.id.toLong())

                    Then("returns null & load state 'Loaded'") {
                        viewModel.location.test {
                            awaitItem().shouldBe(expectedResult)
                        }
                    }
                }
            }
        }

        Given("character won't be loaded") {
            coEvery { getCharacterById(genericCharacterEntity.id) } returns null

            val expectedResult = LoadResult(null, LoadingState.Error)

            When("loading completes") {
                viewModel.loadCharacter(genericCharacterEntity.id.toLong())

                Then("returns null & load state is Error") {
                    viewModel.character.test {
                        awaitItem().shouldBe(expectedResult)
                    }
                }
            }
        }
    }
})
