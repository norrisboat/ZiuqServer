package com.norrisboat.utils

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import java.util.*

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