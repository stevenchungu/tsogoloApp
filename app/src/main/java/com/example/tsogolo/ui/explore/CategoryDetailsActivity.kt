package com.example.tsogolo.ui.explore

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.R
import com.example.tsogolo.ui.theme.TsogoloTheme

class CategoryDetailsActivity : AppCompatActivity() {
    private lateinit var categoryViewModel: CategoryViewModel
    private var categoryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        // Get the category ID from the intent or any other source
        val categoryIds = intent.getIntExtra("category_id", -1)

        // Pass the category ID to the view model
//        categoryViewModel.setSelectedCategoryId(categoryId)

        Log.d("CategoryViewModel", "Category id retrieved: $categoryIds")

//        val viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        setContent {
            TsogoloTheme(this.applicationContext) {
                categoryDetail(categoryViewModel, categoryIds) {
                    finish()
                }
            }
        }
    }
}
