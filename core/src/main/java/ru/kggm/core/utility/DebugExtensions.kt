package ru.kggm.core.utility

fun <T:Any> T.classTag(): String = this::class.java.simpleName