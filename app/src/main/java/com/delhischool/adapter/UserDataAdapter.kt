package com.delhischool.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.delhischool.R
import com.delhischool.adapter.UserDataAdapter.MyViewHolder
import com.delhischool.databinding.RowUserItemBinding
import com.delhischool.listeners.OnItemClickListener
import com.delhischool.models.UserDataModel
import java.util.*

class UserDataAdapter(private val mContext: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<MyViewHolder>() {
    private val alUserData: MutableList<UserDataModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model: UserDataModel = alUserData[holder.adapterPosition]
        if (model.image.isNotEmpty()) {
            val bmp = BitmapFactory.decodeByteArray(model.image, 0, model.image.size)
            holder.binding!!.ivUserImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false))
        }

        holder.binding!!.tvUserName.text = "Name : " + model.userName
        holder.binding!!.tvUserId.text = "Id : " + model.userId

        if (!model.isTeacher) {
            holder.binding!!.tvUserClass.text = "Class : " + model.className
            holder.binding!!.tvUserSection.text = "Section : " + model.classSection
        } else {
            holder.binding!!.tvUserClass.text = "Subject : " + model.className
            holder.binding!!.tvUserSection.visibility = View.GONE
        }

        holder.binding!!.parentView.setOnClickListener { v: View? -> listener.onItemClick(holder.adapterPosition) }
    }

    fun getItemByPosition(position: Int): UserDataModel {
        return alUserData[position]
    }

    override fun getItemCount(): Int {
        return alUserData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RowUserItemBinding? = DataBindingUtil.bind(itemView)
    }

    fun getList(alList: List<UserDataModel>) {
        alUserData.clear()
        alUserData.addAll(alList)
        notifyDataSetChanged()
    }
}