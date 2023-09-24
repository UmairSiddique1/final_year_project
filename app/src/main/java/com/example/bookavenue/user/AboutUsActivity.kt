package com.example.bookavenue.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookavenue.R
import com.example.bookavenue.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

binding.textView2.text="Welcome to Book A Complex, the premier banquet hall booking app designed to make event planning a breeze. With our user-friendly interface and extensive network of top-notch venues, we aim to transform the way people book banquet halls for their special occasions.\n" +
        "\n" +
        "At Book A Complex, we understand that finding the perfect venue is crucial to hosting a successful event. Our mission is to simplify the entire process, empowering our users to discover, compare, and book their ideal banquet halls with ease. Whether you're organizing a wedding reception, corporate event, or any other gathering, we've got you covered.\n" +
        "\n" +
        "What sets us apart is our commitment to providing an unparalleled user experience. We have carefully curated a diverse selection of exquisite banquet halls, ranging from intimate spaces to grand ballrooms, ensuring that you find the perfect fit for your event's size and ambiance. Our dedicated team works tirelessly to handpick and verify each venue, guaranteeing quality and professionalism every step of the way.\n" +
        "\n" +
        "With our intuitive search and filtering options, you can quickly narrow down your options based on location, capacity, amenities, and pricing. Our detailed venue profiles provide comprehensive information, including high-resolution images, virtual tours, floor plans, and customer reviews, allowing you to make an informed decision.\n" +
        "\n" +
        "Booking through our app is secure, seamless, and time-saving. Once you've selected your desired banquet hall, our streamlined reservation system enables you to confirm your booking, make payments, and communicate directly with the venue managers, all within the app. We prioritize transparency and ensure that there are no hidden fees or unexpected surprises along the way.\n" +
        "\n" +
        "Customer satisfaction is at the heart of everything we do. Our support team is available around the clock to assist you with any inquiries, concerns, or special requests you may have. We strive to exceed your expectations, making your event planning journey stress-free and enjoyable.\n" +
        "\n" +
        "Join our community of satisfied users who have experienced the convenience and reliability of Book A Complex. Discover the perfect banquet hall for your next event and create unforgettable memories with your loved ones.\n" +
        "\n" +
        "Book A Complex - Where celebrations come to life."
    }
}