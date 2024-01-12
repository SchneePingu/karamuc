package com.example.karamuc.model

enum class Rooms(
    val id: String,
    val displayName: String,
    val size: Int,
    val maxSize: Int
) {
    FUGU("892","Fugu", 2, 3),
    HANAMI("967","Hanami", 3, 4),
    BAMBOO("1354","Bamboo", 4, 5),
    EIGHTBIT("1361","8-Bit", 4, 5),
    TAKOYAKI("1359","Takoyaki", 5, 6),
    DARUMA("1335","Daruma", 5, 6),
    GHIBLI("1349","Ghibli", 7, 8),
    ORIGAMI("1357","Origami", 10, 11),
    GODZILLA("1337","Godzilla", 10, 11),
    CATCAFE("1027","Katzen-Café", 10, 12),
}

fun getRoomById(id: String): Rooms? {
    return Rooms.values().find { it.id == id }
}

fun getRoomIds(): String {
    return Rooms.values()
        .map{ it.id }
        .joinToString(",", transform = { it })
}