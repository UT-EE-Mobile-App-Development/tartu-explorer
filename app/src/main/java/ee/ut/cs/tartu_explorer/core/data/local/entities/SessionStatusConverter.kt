package ee.ut.cs.tartu_explorer.core.data.local.entities

import androidx.room.TypeConverter

class SessionStatusConverter {
    @TypeConverter
    fun fromSessionStatus(status: SessionStatus): String {
        return status.name
    }

    @TypeConverter
    fun toSessionStatus(status: String): SessionStatus {
        return SessionStatus.valueOf(status)
    }
}
