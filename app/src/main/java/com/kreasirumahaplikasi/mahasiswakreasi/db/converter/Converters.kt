package aish.android.countries.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigInteger


class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun bigIntToString(value: BigInteger): String {
        return value.toString()
    }

    @TypeConverter
    fun StringToBigInteger(value: String): BigInteger {
        return value.toBigInteger()
    }


}