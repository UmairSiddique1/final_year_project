package com.example.bookavenue


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bookavenue.Fragments.ChatsFragment
import com.example.bookavenue.Fragments.FavouritesFragment
import com.example.bookavenue.Fragments.HomeFragment
import com.example.bookavenue.Fragments.ProfileFragment
import com.example.bookavenue.databinding.ActivityUserHomeBinding
import com.example.bookavenue.user.UserChatActivity
import com.example.bookavenue.user.UserSigninActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import java.util.*
class UserHomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
loadFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_userInterface-> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.profile_userInterface->{
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.chat_userInterface->{
                    loadFragment(ChatsFragment())
                    true
                }
                 R.id.favourite_userInterface->{
                 loadFragment(FavouritesFragment())
                     true
                 }
                else -> {
                    loadFragment(FavouritesFragment())
                    false
                }
            }
        }

    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.signOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, UserSigninActivity::class.java))
            }
            R.id.chat->{
                startActivity(Intent(applicationContext,UserChatActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}