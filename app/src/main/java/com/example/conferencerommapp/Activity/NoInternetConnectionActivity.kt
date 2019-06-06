package com.example.conferencerommapp.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.NetworkState
import com.example.conferencerommapp.R


class NoInternetConnectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet_connection)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.retry_button)
    fun checkConnection() {
        val returnIntent = Intent()
        var status = NetworkState.appIsConnectedToInternet(this)
        if (status) {
            returnIntent.putExtra(getString(R.string.status_of_internet), status)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.please_check_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }
}