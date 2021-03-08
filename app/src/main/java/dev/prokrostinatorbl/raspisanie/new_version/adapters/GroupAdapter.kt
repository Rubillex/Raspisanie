package dev.prokrostinatorbl.raspisanie.new_version.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.`$Gson$Preconditions`
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.models.GroupModel

class GroupAdapter(var clickListener: OnItemClick): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mGroupList: ArrayList<GroupModel> = ArrayList()

    fun setupList(groupList: ArrayList<GroupModel>){
        mGroupList.clear()
        mGroupList.addAll(groupList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupViewHolder)
            holder.bind(groupModel = mGroupList[position], action = clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.fragment_group_num, parent, false)
        return GroupViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mGroupList.count()
    }

    class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val btn1 = itemView.findViewById<Button>(R.id.group1)
        private val btn2 = itemView.findViewById<Button>(R.id.group2)
        private val btn3 = itemView.findViewById<Button>(R.id.group3)

        fun bind(groupModel: GroupModel, action: OnItemClick){

            Log.e("GroupBind", groupModel.name_1)

            btn1.text = groupModel.name_1
            btn1.setOnClickListener {
                action.onItemClick(groupModel, adapterPosition, 1)
            }

            if (groupModel.name_2!= ""){
                btn2.text = groupModel.name_2
                btn2.setOnClickListener {
                    action.onItemClick(groupModel, adapterPosition, 2)
                }
            } else {
                btn2.visibility = View.GONE
            }

            if (groupModel.name_3!= "") {
                btn3.text = groupModel.name_3
                btn3.setOnClickListener {
                    action.onItemClick(groupModel, adapterPosition, 3)
                }
            } else{
                btn3.visibility = View.GONE
            }
        }
    }

    interface OnItemClick{
        fun onItemClick(item: GroupModel, position: Int, button: Int)
    }

}