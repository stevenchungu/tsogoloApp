package com.example.tsogolo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tsogolo.model.*
import com.example.tsogolo.util.Converters

@Database(entities = [Career::class, Category::class, College::class, CollegeProgram::class, Personality::class,
    PersonalityQuestion::class, PersonalitySuitableCareer::class, PreferredCareer::class,
    Program::class, ProgramCareer::class,  ProgramRequirement::class, Subject::class,
    User::class, UserPersonality::class, UserSubject::class, CareerCategory::class], version = 2)
@TypeConverters(Converters::class)
abstract class TsogoloDatabase : RoomDatabase() {

    abstract fun careerDao(): CareerDao
    abstract fun categoryDao(): CategoryDao
    abstract fun collegeDao(): CollegeDao
    abstract fun personalityDao(): PersonalityDao
    abstract fun personalityQuestionDao(): PersonalityQuestionDao
    abstract fun preferredCareerDao(): PreferredCareerDao
    abstract fun programDao(): ProgramDao
    abstract fun programRequirementsDao(): ProgramRequirementsDao
    abstract fun pscDao(): PscDao
    abstract fun subjectDao(): SubjectDao
    abstract fun userDao(): UserDao
    abstract fun userPersonalityDao(): UserPersonalityDao
    abstract fun userSubjectDao(): UserSubjectDao

    companion object {
        private var mInstance: TsogoloDatabase? = null

//        val MIGRATION_1_2: Migration = Migration1To2()
        val MIGRATION_2_3: Migration = Migration2To3()


        fun getInstance(context: Context): TsogoloDatabase {
            if (mInstance == null) {
                Log.d("TsogoloDatabase", "Creating TsogoloDatabase instance")
                mInstance = Room.databaseBuilder(context, TsogoloDatabase::class.java, "tsogolo")
                    .createFromAsset("tsogolo.db")
                    .addMigrations( MIGRATION_2_3)
                    .build()
                Log.d("TsogoloDatabase", "done TsogoloDatabase instance")

            }

            return mInstance as TsogoloDatabase
        }
    }
}

//class Migration1To2 : Migration(1, 2) {
////    override fun migrate(database: SupportSQLiteDatabase) {
////
////        Log.d("Migration", "Executing migration 1 to 2")
////        // Create the 'categories' table
////        database.execSQL(
////            "CREATE TABLE IF NOT EXISTS Category (id INTEGER PRIMARY KEY, categoryName TEXT)"
////        )
////
////        Log.d("Migration", "Executing migration 1 to 2 done")
////
////
////    }
//}

class Migration2To3 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Log.d("TsogoloDatabase", "migration instance")

        // Drop the existing table if it exists
//        database.execSQL("DROP TABLE IF EXISTS CareerCategories")

        Log.d("Migration", "Executing migration 2 to 3")

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Category (id INTEGER PRIMARY KEY, categoryName TEXT)"
        )

        database.execSQL(
            "CREATE TABLE IF NOT EXISTS Career (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, aas REAL NOT NULL)"
        )

        // Create the 'career_categories' table with foreign key constraints and NOT NULL constraint
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS CareerCategory (categoryId INTEGER NOT NULL, careerId INTEGER NOT NULL, PRIMARY KEY (categoryId, careerId), FOREIGN KEY (categoryId) REFERENCES Category (id) ON DELETE CASCADE ON UPDATE NO ACTION, FOREIGN KEY (careerId) REFERENCES Career (id) ON DELETE CASCADE ON UPDATE NO ACTION)"
        )
        Log.d("Migration", "Executing migration 2 to 3 done")
    }

}






