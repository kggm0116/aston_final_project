package ru.kggm.core.utility

inline fun <reified T:Any> T.classTag(): String = classTagOf<T>()

inline fun <reified T:Any> classTagOf(): String = T::class.java.simpleName