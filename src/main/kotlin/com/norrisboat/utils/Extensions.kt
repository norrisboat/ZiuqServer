package com.norrisboat.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)

fun Table.getUUID(column: Column<UUID>): UUID {
    val uuid = UUID.randomUUID()
    val user = this.select { column eq uuid }.singleOrNull()
    return if (user == null) {
        uuid
    } else {
        getUUID(column)
    }
}