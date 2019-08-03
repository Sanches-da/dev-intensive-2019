package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity(){

    companion object{
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private var repoChangeListener: TextWatcher = object :  TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            checkRepoIsValid()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    var isEditMode = false
    lateinit var viewFields : Map<String, TextView>
    lateinit var viewModel : ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViews(savedInstanceState)

        initViewModel()

    }

    private fun initViews(savedInstanceState: Bundle?) {
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false


        viewFields = mapOf(
                "nickName" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )

        showCurrentMode(isEditMode)


        btn_edit.setOnClickListener {
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        if (isEditMode){
            checkRepoIsValid()
        }else{
            wr_repository.isErrorEnabled = false
        }

    }

    private fun checkRepoIsValid() {
        val repo = et_repository.text.toString()
        wr_repository.isErrorEnabled = (repo.isBlank() || Utils.validateRepository(repo)).not()
        wr_repository.error = if (repo.isBlank() || Utils.validateRepository(repo)) "" else "Невалидный адрес репозитория"
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })

    }

    private fun updateTheme(theme: Int) {
        delegate.setLocalNightMode(theme)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also{
            for ((k,v) in viewFields){
                v.text = it[k].toString()
            }
        }

        setAvatarImage()
    }

    private fun setAvatarImage() {
        val initials = Utils.toInitials(et_first_name.text.toString(), et_last_name.text.toString())
        if (!initials.isNullOrEmpty()) {
            iv_avatar.setImageBitmap(Utils.getInitialsBitmap(this, initials))
        } else {
            iv_avatar.setImageDrawable(resources.getDrawable(R.drawable.avatar_default, theme))
        }
    }

    private fun saveProfileInfo(){
        if (!Utils.validateRepository(et_repository.text.toString())){
            et_repository.text.clear()
        }
        Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about .text.toString(),
                repository = et_repository.text.toString()
        ).apply{
            viewModel.setProfileData(this)
        }
    }

    private fun showCurrentMode(editMode: Boolean) {
        val info =  viewFields.filter { setOf("firstName","lastName","about","repository").contains(it.key) }
        for ((_,v) in info){
            v as EditText
            v.isFocusable = editMode
            v.isFocusableInTouchMode = editMode
            v.isEnabled = editMode
            v.background.alpha = if(editMode) 255 else 0
        }

        ic_eye.visibility = if (editMode) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = editMode

        with (btn_edit){
            val filter : ColorFilter? = if(editMode) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN
                )
            }else{
                null
            }

            val icon = if (editMode){
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            }else{
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }

        if (editMode){
            checkRepoIsValid()
            et_repository.addTextChangedListener(repoChangeListener)
        }else{
            et_repository.removeTextChangedListener(repoChangeListener)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(IS_EDIT_MODE, isEditMode)

    }



}
