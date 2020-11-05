package com.delhischool.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.delhischool.listeners.OnItemClickListener
import com.delhischool.R
import com.delhischool.ui.activity.AddUpdateUserDetailsActivity
import com.delhischool.adapter.UserDataAdapter
import com.delhischool.databinding.FragmentStudentDataBinding
import com.delhischool.ui.viewModel.UserViewModel

class StudentDataFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var binding: FragmentStudentDataBinding
    private lateinit var studentAdapter: UserDataAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_data, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.readAllStudentData.observe(viewLifecycleOwner, Observer { user ->
            studentAdapter.getList(user)
        })
    }

    private fun setRecyclerView() {
        binding.rvStudents.setHasFixedSize(true)
        binding.rvStudents.layoutManager = GridLayoutManager(mContext, 2)
        studentAdapter = UserDataAdapter(mContext, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                startActivity(Intent(mContext, AddUpdateUserDetailsActivity::class.java)
                        .putExtra("from", true)
                        .putExtra("for", false)
                        .putExtra("id", studentAdapter.getItemByPosition(position).userId)
                )
            }
        })
        binding.rvStudents.adapter = studentAdapter
    }
}