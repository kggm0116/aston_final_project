package ru.kggm.feature_browse.presentation.ui.utility.resources

import android.content.Context
import ru.kggm.feature_browse.domain.entities.CharacterEntity
import ru.kggm.feature_browse.R

fun CharacterEntity.Status.toResourceString(context: Context) = context.getString(
    when (this) {
        CharacterEntity.Status.Alive -> R.string.string_character_status_alive
        CharacterEntity.Status.Dead -> R.string.string_character_status_dead
        CharacterEntity.Status.Unknown -> R.string.string_character_status_unknown
    }
)

fun CharacterEntity.Gender.toResourceString(context: Context) = context.getString(
    when (this) {
        CharacterEntity.Gender.Female -> R.string.string_character_gender_female
        CharacterEntity.Gender.Male -> R.string.string_character_gender_male
        CharacterEntity.Gender.Genderless -> R.string.string_character_gender_genderless
        CharacterEntity.Gender.Unknown -> R.string.string_character_gender_unknown
    }
)