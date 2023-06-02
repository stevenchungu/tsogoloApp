package com.example.tsogolo.ui.career;

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.ui.explore.CareerDescriptionViewModel
import com.example.tsogolo.ui.explore.CategoryViewModel
import com.example.tsogolo.ui.explore.careerDescription
import com.example.tsogolo.ui.explore.categoryDetail
import com.example.tsogolo.ui.theme.TsogoloTheme

class CareerDescriptionActivity : ComponentActivity() {
    private lateinit var careerDescriptionViewModel: CareerDescriptionViewModel
    private var careerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        careerDescriptionViewModel = ViewModelProvider(this).get(CareerDescriptionViewModel::class.java)

        // Get the category ID from the intent or any other source
        val careerId = intent.getIntExtra("careerId", -1)

        // Pass the category ID to the view model
//        categoryViewModel.setSelectedCategoryId(categoryId)

        Log.d("CategoryViewModel", "Career id retrieved: $careerId")

//        val viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        setContent {
            TsogoloTheme(this.applicationContext) {
                careerDescription(careerDescriptionViewModel, careerId) {
                    finish()
                }
            }
        }
    }
}
