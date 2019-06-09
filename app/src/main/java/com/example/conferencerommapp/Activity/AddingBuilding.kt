package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html.fromHtml
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.Model.AddBuilding
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.AddBuildingViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_adding_building.*

@Suppress("DEPRECATION")
class AddingBuilding : AppCompatActivity() {

    /**
     * Declaring Global variables and binned view for using butter knife
     */
    @BindView(R.id.edit_text_building_name)
    lateinit var buildingNameEditText: EditText
    @BindView(R.id.edit_text_building_place)
    lateinit var buildingPlaceEditText: EditText

    private lateinit var mAddBuildingViewModel: AddBuildingViewModel
    private var mAddBuilding = AddBuilding()
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_building)
        ButterKnife.bind(this)
        init()
        textChangeListenerOnBuildingName()
        textChangeListenerOnBuildingPlace()
        observeData()
    }

    /**
     * add text change listener for the building Name
     */
    private fun textChangeListenerOnBuildingName() {
        buildingNameEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateBuildingName()
            }
        })
    }

    /**
     * add text change listener for the building place
     */
    private fun textChangeListenerOnBuildingPlace() {
        buildingPlaceEditText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateBuildingPlace()
            }
        })
    }


    /**
     * function will invoke whenever the add button is clicked
     */
    @OnClick(R.id.button_add_building)
    fun getBuildingDetails() {
        if (validateInputs()) {
            if(NetworkState.appIsConnectedToInternet(this)) {
                addDataToObject(mAddBuilding)
                addBuild(mAddBuilding)
            } else {
                val i = Intent(this@AddingBuilding, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE)
            }

        }
    }



    /**
     * initialize all lateinit variables
     */
    fun init() {
        val actionBar = supportActionBar
        actionBar!!.title = fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Add_Buildings) + "</font>")
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mAddBuildingViewModel = ViewModelProviders.of(this).get(AddBuildingViewModel::class.java)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            addDataToObject(mAddBuilding)
            addBuild(mAddBuilding)
        }
    }

    /**
     * observing data for adding Building
     */
    private fun observeData() {
        mAddBuildingViewModel.returnSuccessForAddBuilding().observe(this, Observer {
            progressDialog.dismiss()
            Toasty.success(this, getString(R.string.building_added), Toast.LENGTH_SHORT, true).show()
            finish()
        })
        mAddBuildingViewModel.returnFailureForAddBuilding().observe(this, Observer {
            progressDialog.dismiss()
            if (it == Constants.INVALID_TOKEN) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }
    /**
     *  set values to the different properties of object which is required for api call
     */
    private fun addDataToObject(mAddBuilding: AddBuilding) {
        mAddBuilding.buildingName = buildingNameEditText.text.toString().trim()
        mAddBuilding.place = buildingPlaceEditText.text.toString().trim()
    }
    /**
     * validation for field building name for empty condition
     */
    private fun validateBuildingName(): Boolean {
        val input = buildingNameEditText.text.toString().trim()
        return if (input.isEmpty()) {
            building_name_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            building_name_layout.error = null
            true
        }
    }
    /**
     * validation for building place for empty condition
     */
    private fun validateBuildingPlace(): Boolean {
        val input = buildingPlaceEditText.text.toString().trim()
        return if (input.isEmpty()) {
            location_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            location_layout.error = null
            true
        }
    }


    /**
     * validate all input fields
     */
    private fun validateInputs(): Boolean {
        if (!validateBuildingName() or !validateBuildingPlace()) {
            return false
        }
        return true
    }
    /**
     * function calls the ViewModel of addingBuilding and send data to the backend
     */
    private fun addBuild(mBuilding: AddBuilding) {

        /**
         * Get the progress dialog from GetProgress Helper class
         */
        progressDialog.show()
        mAddBuildingViewModel.addBuildingDetails(mBuilding, GetPreference.getTokenFromPreference(this))
    }

    /**
     * show dialog when session expired
     */
    private fun showAlert() {
        val dialog = GetAleretDialog.getDialog(
            this, getString(R.string.session_expired), "Your session is expired!\n" +
                    getString(R.string.session_expired_messgae)
        )
        dialog.setPositiveButton(R.string.ok) { _, _ ->
            signOut()
        }
        val builder = GetAleretDialog.showDialog(dialog)
        ColorOfDialogButton.setColorOfDialogButton(builder)
    }
    /**
     * sign out from application
     */
    private fun signOut() {
        val mGoogleSignInClient = GoogleGSO.getGoogleSignInClient(this)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                startActivity(Intent(applicationContext, SignIn::class.java))
                finish()
            }
    }

}
