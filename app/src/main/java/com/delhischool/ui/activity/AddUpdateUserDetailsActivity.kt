package com.delhischool.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.delhischool.R
import com.delhischool.databinding.ActivityAddUpdateUserDetailsBinding
import com.delhischool.models.UserDataModel
import com.delhischool.ui.viewModel.UserViewModel
import java.io.ByteArrayOutputStream

class AddUpdateUserDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private val PERMISSIONS_READ_EXTERNAL_STORAGE = 100
    private lateinit var mContext: Context
    private lateinit var mUserViewModel: UserViewModel
    private lateinit var binding: ActivityAddUpdateUserDetailsBinding
    private var fromEdit = false
    private var isTeacher = false
    private var userId = ""
    private lateinit var userEditModel: UserDataModel
    private lateinit var b: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_update_user_details)
        mContext = this

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        if (intent != null) {
            if (intent.getBooleanExtra("from", false)) {
                fromEdit = true
                userId = intent.getStringExtra("id")!!
                mUserViewModel.getSingleItem(userId)
            }
            isTeacher = intent.getBooleanExtra("for", false)
        }

        if (isTeacher) {
            binding.etClassSection.visibility = View.GONE
            binding.etRollNumber.hint = getString(R.string.teacher_id)
            binding.etClassName.hint = getString(R.string.subject_name)
        }

        mUserViewModel.itemAdded.observe(this, Observer {
            if (it.toInt() == -1) {
                Toast.makeText(mContext, "User Id already exist", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, "User added Successfully!!", Toast.LENGTH_SHORT).show()
            }
        })

        mUserViewModel.userData.observe(this, Observer {
            userEditModel = it
            addUserData()
        })

        setUpToolbar()
        setOnClickListener()
    }

    private fun addUserData() {
        b = userEditModel.image
        if (b.isNotEmpty()) {
            val bmp = BitmapFactory.decodeByteArray(userEditModel.image, 0, userEditModel.image.size)
            binding.ivUserImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false))
        }

        binding.etRollNumber.editText!!.setText(userEditModel.userId)
        binding.etRollNumber.editText!!.setSelection(userEditModel.userId.length)

        binding.etName.editText!!.setText(userEditModel.userName)
        binding.etClassName.editText!!.setText(userEditModel.className)
        binding.etClassSection.editText!!.setText(userEditModel.classSection)
    }

    private fun setOnClickListener() {
        binding.btnAddUpdate.setOnClickListener(this)
        binding.btnDelete.setOnClickListener(this)
        binding.uploadImage.setOnClickListener(this)

        binding.etRollNumber.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etRollNumber.isErrorEnabled = false
            }
        })

        binding.etName.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etName.isErrorEnabled = false
            }
        })

        binding.etClassName.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etClassName.isErrorEnabled = false
            }
        })

        binding.etClassSection.editText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etClassSection.isErrorEnabled = false
            }
        })
    }

    private fun checkValidation(): Boolean {
        if (binding.etRollNumber.editText!!.text.isEmpty()) {
            if (isTeacher) {
                binding.etRollNumber.error = getString(R.string.enter_teacher_id)
            } else {
                binding.etRollNumber.error = getString(R.string.enter_roll_number)
            }
            return false
        } else if (binding.etName.editText!!.text.isEmpty()) {
            binding.etName.error = getString(R.string.enter_name)
            return false
        } else if (binding.etClassName.editText!!.text.isEmpty()) {
            if (isTeacher) {
                binding.etClassName.error = getString(R.string.enter_subject_name)
            } else {
                binding.etClassName.error = getString(R.string.enter_class_name)
            }
            return false
        } else if (!isTeacher && binding.etClassSection.editText!!.text.isEmpty()) {
            binding.etClassSection.error = getString(R.string.enter_class_section)
            return false
        } else {
            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
//            overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setUpToolbar() {
        if (fromEdit) {
            binding.etRollNumber.isEnabled = false
            binding.toolBar.title.text = getString(R.string.edit_details)
            binding.btnAddUpdate.text = getString(R.string.update)
        } else {
            binding.toolBar.title.text = getString(R.string.add_details)
            binding.btnAddUpdate.text = getString(R.string.add)
            binding.btnDelete.visibility = View.GONE
        }

        setSupportActionBar(binding.toolBar.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAddUpdate -> {
                if (checkValidation()) {
                    val userId = binding.etRollNumber.editText!!.text.toString()
                    val userName = binding.etName.editText!!.text.toString()
                    val className = binding.etClassName.editText!!.text.toString()
                    val classSection = binding.etClassSection.editText!!.text.toString()
                    val isTeacher = isTeacher
                    val image: ByteArray

                    if (::b.isInitialized) {
                        image = b
                    } else {
                        image = ByteArray(0)
                    }

                    val userDataModel = UserDataModel(userId, userName, className, classSection, isTeacher, image)

                    if (fromEdit) {
                        mUserViewModel.updateUser(userDataModel)
                    } else {
                        mUserViewModel.addUser(userDataModel)
                    }
                    finish()
                }
            }
            R.id.btnDelete -> {
                mUserViewModel.deleteUser(userEditModel)
                finish()
            }
            R.id.uploadImage -> {
                if (permissionIfNeeded()) {
                    getGalleryImage()
                }
            }
        }
    }

    private fun getGalleryImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    private fun permissionIfNeeded(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_READ_EXTERNAL_STORAGE)
                false
            } else {
                true
            }
        } else true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGalleryImage()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = data!!.data!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = contentResolver.query(selectedImage,
                    filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            val picturePath: String = cursor.getString(columnIndex)
            cursor.close()

            val bm = BitmapFactory.decodeFile(picturePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos) // bm is the bitmap object
            b = baos.toByteArray()

            binding.ivUserImage.setImageURI(selectedImage)
        }
    }
}