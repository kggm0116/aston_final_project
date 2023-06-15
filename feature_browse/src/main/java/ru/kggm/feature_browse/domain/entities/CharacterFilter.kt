package ru.kggm.feature_browse.domain.entities

sealed class CharacterFilter {
    class Name(val value: String): CharacterFilter()
    class Status(val value: CharacterEntity.Status): CharacterFilter()
    class Type(val value: String): CharacterFilter()
    class Gender(val value: CharacterEntity.Gender): CharacterFilter()
}