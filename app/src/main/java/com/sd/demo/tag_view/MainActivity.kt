package com.sd.demo.tag_view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.tag_view.databinding.ActivityMainBinding
import com.sd.lib.tag_view.FTagViewManager

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        FTagViewManager.isDebug = true;
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

    override fun onClick(v: View?) {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding.tagView.destroyItem()
    }
}