package net.alexandroid.where.model

data class Locations(
    val latitudeE7: Int,
    val longitudeE7: Int,
    val accuracy: Int,
    val source: String,
    val timestamp: String
)

// Data structure
/*
  {
     "locations": [{
       "latitudeE7": 327727290,
       "longitudeE7": 352700680,
       "accuracy": 20,
       "source": "gps", // network, cell, WIFI
       "timestamp": "2011-08-13T12:16:22.183Z"
     },
     ...
     ]
  }
 */