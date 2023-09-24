package com.example.bookavenue.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bookavenue.Models.UserLoginModel
import com.example.bookavenue.R
import com.example.bookavenue.databinding.FragmentProfileBinding
import com.example.bookavenue.user.AboutUsActivity
import com.example.bookavenue.user.EditEmailActivity
import com.example.bookavenue.user.EditNameActivity
import com.example.bookavenue.user.UserProfileImgActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {
private lateinit var binding:FragmentProfileBinding
private var image:String=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?,): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val user = Firebase.auth.currentUser

        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl: Uri? = user.photoUrl
            FirebaseDatabase.getInstance().getReference("Users").child(
                FirebaseAuth.getInstance().currentUser!!.uid)
                .child("profileimg").setValue(photoUrl.toString())
Glide.with(requireContext()).load(photoUrl).placeholder(R.drawable.ic_profile).into(binding.ivUserImg)
            updatePhotoUrl(photoUrl = photoUrl.toString())
            binding.tvUserName.text=name
            binding.tvName.text=name
            binding.tvEmail.text=email
        }
        binding.cardView1.setOnClickListener {
startActivity(Intent( context,EditNameActivity::class.java))
        }
        binding.cardView2.setOnClickListener {
            startActivity(Intent( context,EditEmailActivity::class.java))
        }
        binding.cardView4.setOnClickListener {
            startActivity(Intent(context,AboutUsActivity::class.java))
        }

        binding.ivUserImg.setOnLongClickListener {
            val intent=Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,1)
            return@setOnLongClickListener true
        }
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data=snapshot.getValue(UserLoginModel::class.java)
                    if (data != null) {
                        image=data.profileimg.toString()
//                        context?.let { Glide.with(it).load(image).placeholder(R.drawable.ic_profile).into(binding.ivUserImg) }
                        updatePhotoUrl(image)
//                   binding.tvName.text=data.name.toString()
//                        binding.tvEmail.text=data.email.toString()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        binding.ivUserImg.setOnClickListener {
            val intent=Intent(requireContext(),UserProfileImgActivity::class.java)
            intent.putExtra("profileimg",image)
            startActivity(intent)
        }
        binding.switch1.isChecked=getSaveBoolean()
        if(binding.switch1.isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.switch1.setOnCheckedChangeListener{_,isChecked->
            if(isChecked){
              saveBoolean(true)
                binding.switch1.isChecked= getSaveBoolean()
                Toast.makeText(context,"Dark mode on",Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                saveBoolean(false)
                binding.switch1.isChecked= getSaveBoolean()
                Toast.makeText(context,"Dark mode off",Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1){
            if(data!=null){
                val data1=data.data
                val storage=Firebase.storage
                val storageRef=storage.reference.child("userprofileimg").child(FirebaseAuth.getInstance().currentUser!!.uid)
                if (data1 != null) {
                    val uploadTask= storageRef.putFile(data1)
                    uploadTask.continueWithTask {task->
                        if(!task.isSuccessful){
                            task.exception?.let {
                                throw it
                            }
                        }
                        storageRef.downloadUrl}
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                FirebaseDatabase.getInstance().getReference("Users").child(
                                    FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child("profileimg").setValue(downloadUri.toString())

                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                }
            }
        }
    }
    private fun saveBoolean(value: Boolean) {
        val sharedPref = activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("switch_value", value)
        editor?.apply()
    }

    private fun getSaveBoolean(): Boolean {
        val sharedPref = activity?.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        return sharedPref?.getBoolean("switch_value", false) ?: false
    }
    private fun updatePhotoUrl(photoUrl: String) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            // Step 1: Get the reference to the current user from Firebase Auth

            // Step 2: Update the photoUrl property of the user's profile
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(photoUrl)
            }

            // Step 3: Save the changes to Firebase Auth
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    } else {
                        Toast.makeText(context, task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}