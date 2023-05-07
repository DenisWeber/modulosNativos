package com.modulosnativos.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Face.class, Log.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FaceDao faceDao();
    public abstract LogDao logDao();

    private static AppDatabase s_appDatabase = null;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Log` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "`dateTime` INTEGER, " +
                    "`action` INTEGER NOT NULL, " +
                    "`description` TEXT);");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (s_appDatabase == null) {
            s_appDatabase = Room.databaseBuilder(context, AppDatabase.class, "biometria-db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }

        return s_appDatabase;
    }
}
