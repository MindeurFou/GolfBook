package com.example.golfbook.extensions

import com.example.golfbook.model.Player

class CollectionsExtensions {

   fun List<Player>.sort() = this.sortedWith(compareByDescending { it.par })

}