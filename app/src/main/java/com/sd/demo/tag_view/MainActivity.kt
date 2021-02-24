package com.sd.demo.tag_view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.tag_view.databinding.ActivityMainBinding
import com.sd.lib.tag_view.FTagViewManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mBinding: ActivityMainBinding
    private val mMainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        FTagViewManager.default.mIsDebug = true;
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mMainScope.launch {
            Log.i(TAG, "launch start, thread:${Thread.currentThread().name}")
            val result = testLoading()
            Log.i(TAG, "launch finish result:$result, thread:${Thread.currentThread().name}")
        }
    }

    override fun onClick(v: View?) {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
    }

    suspend fun testLoading(): String {
        Log.i(TAG, "testLoading start, thread:${Thread.currentThread().name}")
        delay(5 * 1000)
        Log.i(TAG, "testLoading finish, thread:${Thread.currentThread().name}")
        return "result"
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.viewTag.itemManager.destroyItem()
        mMainScope.cancel()
    }
}