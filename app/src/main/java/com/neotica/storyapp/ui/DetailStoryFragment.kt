package com.neotica.storyapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.neotica.storyapp.databinding.FragmentDetailStoryBinding
import com.neotica.storyapp.ui.viewmodel.MainViewModel
import com.neotica.storyapp.util.formatDateTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DetailStoryFragment : Fragment() {
    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //insert logic here
        val bundle = DetailStoryFragmentArgs.fromBundle(arguments as Bundle)
        binding.apply {
            Glide.with(root).load(bundle.image.toString()).into(ivStory)
            tvName.text = bundle.name
            tvDescription.text = bundle.desc
            tvDate.text = "Created at: " + formatDateTime(bundle.created)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}