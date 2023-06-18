package ru.kggm.core.utility

import androidx.fragment.app.Fragment

inline fun <reified T:Any> T.classTag(): String = classTagOf<T>()

inline fun <reified T:Any> classTagOf(): String = T::class.java.simpleName