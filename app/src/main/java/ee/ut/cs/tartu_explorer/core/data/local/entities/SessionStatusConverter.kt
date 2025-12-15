package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.TypeConverter

/**
 * Converter class to handle the conversion between [SessionStatus] enum and its String representation
 * for storing in the Room database.
 */
class SessionStatusConverter {
    /**
     * Converts a [SessionStatus] enum to its String representation.
     *
     * @param status The [SessionStatus] enum to convert.
     * @return The String representation of the [SessionStatus].
     */
    @TypeConverter
    fun fromSessionStatus(status: SessionStatus): String {
        return status.name
    }

    /**
     * Converts a String representation back to a [SessionStatus] enum.
     *
     * @param status The String representation of the [SessionStatus].
     * @return The corresponding [SessionStatus] enum.
     */
    @TypeConverter
    fun toSessionStatus(status: String): SessionStatus {
        return SessionStatus.valueOf(status)
    }
}
