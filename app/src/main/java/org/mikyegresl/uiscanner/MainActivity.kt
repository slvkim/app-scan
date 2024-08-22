package org.mikyegresl.uiscanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//    private val uiScanner by lazy { XmlViewScanner() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        uiScanner.scanActivityUi(this) {
//            Log.e(TAG, "viewData: $it")
//        }
    }

    companion object {
        private const val TAG = "MyActivity"
    }
}