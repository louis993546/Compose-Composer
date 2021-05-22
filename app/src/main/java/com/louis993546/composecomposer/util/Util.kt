package com.louis993546.composecomposer.util

import com.louis993546.composecomposer.Id
import kotlin.random.Random

// TODO it'd be nice i can make sure it never collide
fun randId(): Id = Random.nextInt()

val <T> T.exhaustive: T
    get() = this