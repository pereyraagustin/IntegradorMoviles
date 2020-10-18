package com.iua.agustinpereyra.controller

import com.iua.agustinpereyra.R
import com.iua.agustinpereyra.model.Cattle

class StaticDataGenerator {
    companion object {
        fun generateCattleList() : List<Cattle> {
            val cattleList = mutableListOf<Cattle>()

            // Fill with data
            for (i in 0..40) {
                val cattle = Cattle(caravan = generateRandomCaravan(), weight = (300..1000).random(), imageId = generateCattleImageId(i))
                cattleList.add(cattle)
            }
            return cattleList
        }

        fun generateRandomCaravan() : String {
            return ('A'..'Z').random().toString() + (10..99).random() + ('A'..'Z').random().toString()
        }

        fun generateCattleImageId(run: Int) : Int {
            when (run % 4) {
                0 -> return R.drawable.sample_cow_1
                1 -> return R.drawable.sample_cow_2
                2 -> return R.drawable.sample_cow_3
                3 -> return R.drawable.sample_cow_4
            }
            return R.drawable.sample_cow_1
        }
    }
}