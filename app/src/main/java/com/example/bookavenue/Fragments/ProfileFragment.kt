package com.example.bookavenue.Fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookavenue.R
import com.example.bookavenue.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?,): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val user = Firebase.auth.currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            var name = user.displayName
            val email = user.email
            val photoUrl: Uri? = user.photoUrl
Glide.with(requireContext()).load(photoUrl).placeholder(R.drawable.ic_profile).into(binding.ivUserImg)
            binding.tvUserName.text=email
            // Check if user's email is verified
            val emailVerified = user.isEmailVerified
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid
        }
        return binding.root
    }
}