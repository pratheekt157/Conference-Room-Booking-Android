package com.example.conferencerommapp.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.conferencerommapp.AddConferenceRoom
import com.example.conferencerommapp.Helper.*
import com.example.conferencerommapp.R
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.ViewModel.AddConferenceRoomViewModel
import com.example.conferenceroomtabletversion.utils.GetPreference
import com.google.android.material.switchmaterial.SwitchMaterial
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_adding_conference.*

@Suppress("DEPRECATION")
class AddingConference : AppCompatActivity() {
    /**
     * Declaring Global variables and binned butter knife
     */
    @BindView(R.id.conference_Name)
    lateinit var conferenceRoomEditText: EditText

    @BindView(R.id.conference_capacity)
    lateinit var roomCapacity: EditText

    @BindView(R.id.monitor_check_box)
    lateinit var monitor: CheckBox

    @BindView(R.id.whiteboard_marker_check_box)
    lateinit var whiteboard: CheckBox

    @BindView(R.id.speaker_check_box)
    lateinit var speaker: CheckBox

    @BindView(R.id.extension_board_check_box)
    lateinit var extensionBoard: CheckBox

    @BindView(R.id.projector_check_box)
    lateinit var projector: CheckBox

    @BindView(R.id.permission_required)
    lateinit var switchButton: SwitchMaterial

    private lateinit var mAddConferenceRoomViewModel: AddConferenceRoomViewModel
    private var mConferenceRoom = AddConferenceRoom()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_conference)
        ButterKnife.bind(this)
        init()
        observeData()
    }

    /**
     * initialize all lateinit variables
     */
    fun init() {
        val actionBar = supportActionBar
        actionBar!!.title = Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Add_Room) + "</font>")
        textChangeListenerOnRoomName()
        textChangeListenerOnRoomCapacity()
        progressDialog = GetProgress.getProgressDialog(getString(R.string.progress_message_processing), this)
        mAddConferenceRoomViewModel = ViewModelProviders.of(this).get(AddConferenceRoomViewModel::class.java)
    }

    /**
     * observing data for adding conference
     */
    fun observeData() {
        mAddConferenceRoomViewModel.returnSuccessForAddingRoom().observe(this, Observer {
            progressDialog.dismiss()
            Toasty.success(this, getString(R.string.room_add_success), Toast.LENGTH_SHORT, true).show()
            finish()
        })
        mAddConferenceRoomViewModel.returnFailureForAddingRoom().observe(this, Observer {
            progressDialog.dismiss()
            if (it == getString(R.string.invalid_token)) {
                showAlert()
            } else {
                ShowToast.show(this, it as Int)
            }
        })
    }

    /**
     * function will invoke whenever the add button is clicked
     */
    @OnClick(R.id.add_conference_room)
    fun addRoomButton() {
        if (validateInputs()) {
            if (NetworkState.appIsConnectedToInternet(this)) {
                addDataToObject(mConferenceRoom)
                addRoom()
            } else {
                val i = Intent(this@AddingConference, NoInternetConnectionActivity::class.java)
                startActivityForResult(i, Constants.RES_CODE)
            }

        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RES_CODE && resultCode == Activity.RESULT_OK) {
            addDataToObject(mConferenceRoom)
            addRoom()
        }
    }

    /**
     *  set values to the different properties of object which is required for api call
     */
    private fun addDataToObject(mConferenceRoom: AddConferenceRoom) {
        val bundle: Bundle? = intent.extras
        val buildingId = bundle!!.get(Constants.EXTRA_BUILDING_ID)!!.toString().toInt()
        mConferenceRoom.bId = buildingId
        mConferenceRoom.roomName = conferenceRoomEditText.text.toString().trim()
        mConferenceRoom.capacity = roomCapacity.text.toString().toInt()
        mConferenceRoom.monitor = monitor.isChecked
        mConferenceRoom.speaker = speaker.isChecked
        mConferenceRoom.whiteBoardMarker = whiteboard.isChecked
        mConferenceRoom.extensionBoard = extensionBoard.isChecked
        mConferenceRoom.projector = projector.isChecked
        mConferenceRoom.permission = switchButton.isChecked
    }

    /**
     * validation for room employeeList
     */
    private fun validateRoomName(): Boolean {
        val input = conferenceRoomEditText.text.toString().trim()
        return if (input.isEmpty()) {
            room_name_layout_name.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            room_name_layout_name.error = null
            true
        }
    }

    /**
     * validation for spinner
     */
    private fun validateRoomCapacity(): Boolean {
        return if (roomCapacity.text.toString().trim().isEmpty()) {
            room_capacity_layout.error = getString(R.string.field_cant_be_empty)
            false
        } else {
            val input = roomCapacity.text.toString().toLong()
            if (input <= 0 || input > Int.MAX_VALUE) {
                room_capacity_layout.error = getString(R.string.room_capacity_must_be_more_than_0)
                false
            } else {
                room_name_layout_name.error = null
                true
            }
        }
    }

    /**
     * validate all input fields
     */
    private fun validateInputs(): Boolean {
        if (!validateRoomName() or !validateRoomCapacity()) {
            return false
        }
        return true
    }

    /**
     * function calls the ViewModel of addingConference and data into the database
     */
    private fun addRoom() {
        progressDialog.show()
        mAddConferenceRoomViewModel.addConferenceDetails(GetPreference.getTokenFromPreference(this), mConferenceRoom)
    }

    /**
     * show dialog for session expired
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

    /**
     * add text change listener for the room name
     */
    private fun textChangeListenerOnRoomName() {
        conferenceRoomEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateRoomName()
            }
        })
    }

    /**
     * add text change listener for the room name
     */
    private fun textChangeListenerOnRoomCapacity() {
        roomCapacity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // nothing here
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateRoomCapacity()
            }
        })
    }
}
