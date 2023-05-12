package com.example.tsogolo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tsogolo.model.*
import com.example.tsogolo.util.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Database(entities = [Career::class, College::class, CollegeProgram::class, Personality::class,
    PersonalityQuestion::class, PersonalitySuitableCareer::class, PreferredCareer::class,
    Program::class, ProgramCareer::class, ProgramRequirement::class, Subject::class,
    User::class, UserPersonality::class, UserSubject::class], version = 1)
@TypeConverters(Converters::class)
abstract class TsogoloDatabase : RoomDatabase() {

    abstract fun careerDao(): CareerDao
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

        fun getInstance(context: Context): TsogoloDatabase {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context, TsogoloDatabase::class.java, "tsogolo")
                    .createFromAsset("tsogolo.db")
                    .build()
            }

            return mInstance as TsogoloDatabase
        }
    }
}