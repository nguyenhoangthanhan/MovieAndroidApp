package com.example.movieapp.database;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.movieapp.utils.CONSTANT;

public class MovieMigrations {

    static final Migration MIGRATION_2_3 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE " + CONSTANT.NAME_OF_REMINDER_TABLE
                    +" ADD COLUMN `"+ CONSTANT.REMINDER_ID +"` INTEGER, PRIMARY KEY("+CONSTANT.REMINDER_ID+") AUTOINCREMENT ");
        }
    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE Song ADD COLUMN movieGenre TEXT NOT NULL DEFAULT ''");
        }
    };
}
