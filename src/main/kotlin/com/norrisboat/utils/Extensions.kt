package com.norrisboat.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun String.toUUID(): UUID = UUID.fromString(this)

fun Table.getUUID(column: Column<UUID>): UUID {
    val uuid = UUID.randomUUID()
    val id = this.select { column eq uuid }.singleOrNull()
    return if (id == null) {
        uuid
    } else {
        getUUID(column)
    }
}

fun Random.nextInt(range: IntRange): Int {
    return range.first + nextInt(range.last - range.first + 1)
}

fun getRandomImage(): String {
    val random = ThreadLocalRandom.current().nextInt(0..199)
    return String.format("%04d", random)
}