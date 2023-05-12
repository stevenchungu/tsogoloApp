package com.example.tsogolo.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tsogolo.MainActivity
import com.example.tsogolo.databinding.FragmentHomeBinding
import com.example.tsogolo.ui.career.CareerFinderActivity
import com.example.tsogolo.ui.personality.PersonalityTestActivity
import com.example.tsogolo.ui.profile.ProfilingActivity
import com.example.tsogolo.ui.theme.TsogoloTheme

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.initialize(requireContext().applicationContext)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), ProfilingActivity::class.java)
            startActivity(intent)
        }

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                TsogoloTheme(requireContext()) {
                    HomeLayout(homeViewModel, requireContext(),{
                        AlertDialog.Builder(requireContext())
                            .setMessage("Do you want to delete this profile?")
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes") {_,_ ->
                                if (homeViewModel.users.value.size < 2) {
                                    (requireActivity() as MainActivity?)?.openFirstLaunchActivity()
                                }
                                homeViewModel.deleteUser(it)
                            }.create().show()
                    }, {
                        PersonalityTestActivity.start(requireContext(), homeViewModel.activeUser.value)
                    }, {
                        CareerFinderActivity.start(requireContext(), homeViewModel.activeUser.value)
                    }, {
                        ProfilingActivity.editProfile(requireContext(), it)
                    })
                }
            }
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.resumed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}