package ru.kggm.feature_browse.test.presentation.ui.characters.details

import io.kotest.assertions.timing.eventually
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.setMain
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.domain.use_cases.GetCharacterById
import ru.kggm.feature_browse.domain.use_cases.GetLocationById
import ru.kggm.feature_browse.presentation.entities.CharacterPresentationEntity.Companion.toPresentationEntity
import ru.kggm.feature_browse.presentation.ui.characters.details.CharacterDetailsViewModel
import ru.kggm.feature_browse.presentation.ui.shared.LoadResult
import ru.kggm.feature_browse.presentation.ui.shared.LoadingState
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailsViewModelTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    Dispatchers.setMain(Dispatchers.Default)
    val timeToLoad = 5.seconds

    val getCharacterById = mockk<GetCharacterById>()
    val getLocationById = mockk<GetLocationById>()
    val viewModel = CharacterDetailsViewModel(getCharacterById, getLocationById)

    val genericTestEntity = CharacterEntity(
        id = 0,
        name = "name",
        status = CharacterEntity.Status.Alive,
        species = "species",
        type = "type",
        gender = CharacterEntity.Gender.Male,
        image = "image",
        locationId = 1,
        originId = 1,
        episodeIds = listOf(1, 2, 3)
    )

    Given("character exists") {
        coEvery { getCharacterById(genericTestEntity.id) } returns genericTestEntity

        When("the character is already loaded") {
            eventually(timeToLoad) {
                viewModel.loadCharacter(genericTestEntity.id.toLong())
            }
            val characterFlow
            val dataAfter = eventually(timeToLoad) {
                viewModel.loadCharacter(genericTestEntity.id.toLong())
            }

            Then("data is not updated") {
                /* I think ideally viewModel.character flow should be checked
                for any value updates, but i couldn't figure out how
                (tried spyk & verify, doesn't really work)*/
                dataAfter.shouldBe(dataBefore)
            }
        }

        When("immediately after loading starts") {
            val expectedResult = LoadResult(
                null,
                LoadingState.Loading
            )

            viewModel.loadCharacter(genericTestEntity.id.toLong())
            val actualResult = viewModel.character.first()

            Then("returns null & load state 'Loading'") {
                actualResult.shouldBe(expectedResult)
            }
        }

        When("after waiting for loading to complete") {
            val expectedResult = LoadResult(
                genericTestEntity.toPresentationEntity(),
                LoadingState.Loaded
            )

            viewModel.loadCharacter(genericTestEntity.id.toLong())
            val actualResult = eventually(timeToLoad) { viewModel.character.first() }

            Then("returns character & load state 'Loaded'") {
                actualResult.shouldBe(expectedResult)
            }
        }
    }

    Given("character does not exist") {
        coEvery { getCharacterById(genericTestEntity.id) } returns null
        val expectedResult = LoadResult(
            null,
            LoadingState.Error
        )

        When("loading completes") {
            viewModel.loadCharacter(genericTestEntity.id.toLong())
            val actualResult = eventually(timeToLoad) {
                viewModel.character.first()
            }

            Then("returns null & load state is Error") {
                actualResult.shouldBe(expectedResult)
            }
        }
    }
})
