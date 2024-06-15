package org.d3if3134.currencycalculator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3134.currencycalculator.model.CurrencyCode
import org.d3if3134.currencycalculator.model.History

@Database(entities = [CurrencyCode::class, History::class], version = 1, exportSchema = false)
abstract class CurrencyDb: RoomDatabase() {

    abstract val dao : CurrencyDao

    companion object {
        @Volatile
        private var INSTANCE: CurrencyDb? = null

        fun getInstance(context: Context): CurrencyDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CurrencyDb::class.java,
                        "currency_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}