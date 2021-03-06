package com.karntrehan.nagar.cities

import android.arch.lifecycle.LiveData
import com.karntrehan.nagar.cities.entities.CityEntity

/**
 * Created by karn on 14-08-2017.
 */
interface CitiesContract {
    interface Repository {
        fun getCities(offset: Int): LiveData<List<CityEntity>>
        fun getRemoteCities(offset: Int, limit: Int)
        fun deleteCities()
        fun getLoadingStatus(position: Int?): LiveData<Boolean>
        fun getErrorStatus(position: Int?): LiveData<String>
    }
}