package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.nacho.restaurantapplication.core.utils.ImageLoader
import com.nacho.restaurantapplication.databinding.FragmentNewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val args: NewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            newsTxtTitle.text = args.title
            newsTxtSubtitle.text = Html.fromHtml(args.description, Html.FROM_HTML_MODE_LEGACY)
        }
        ImageLoader.loadImage(
            context = requireContext(),
            url = args.image,
            imageView = binding.newsImg,
            onLoadFailed = {
                // Manejar error al cargar imagen
            },
            onResourceReady = {}
        )
    }

}