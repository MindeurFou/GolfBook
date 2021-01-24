package com.example.golfbook.data.model

data class Player(
        var playerId: String? = null,
        var name: String,
        var drawableResourceId: Int = -1,
        val managerId: String? = null
) {

    override fun equals(other: Any?): Boolean {

        if (other is Player) {
            return this.name == other.name && this.drawableResourceId == other.drawableResourceId && this.managerId == other.managerId
        } else
            return false

    }

}