package dev.prokrostinatorbl.raspisanie.new_version.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.prokrostinatorbl.raspisanie.R
import dev.prokrostinatorbl.raspisanie.new_version.models.MainModel
import java.time.temporal.TemporalQuery

class MainAdapter(var clickListener: OnItemClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var mSourceList: ArrayList<MainModel> = ArrayList()
    private var mMainList: ArrayList<MainModel> = ArrayList()

    fun setupList(mainList: ArrayList<MainModel>){
        mSourceList.clear()
        mSourceList.addAll(mainList)
        search(query = "")
    }

    fun search(query: String){
        mMainList.clear()
        mSourceList.forEach {
            if (it.name.startsWith(query)){
                mMainList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder){
            holder.bind(mainModel = mMainList[position], action = clickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.fragment_institut, parent, false)
        return MainViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mMainList.count()
    }

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val mBtn = itemView.findViewById<RelativeLayout>(R.id.btnInstitut)
        private val mName = itemView.findViewById<Button>(R.id.institut_text)

        fun bind(mainModel: MainModel, action: OnItemClick){
            mName.text = mainModel.name

            Log.e("MyLog", mainModel.name)

            mName.setOnClickListener {
                action.onItemClick(mainModel, adapterPosition)
            }
        }
    }

    interface OnItemClick{
        fun onItemClick(item: MainModel, position: Int)
    }
}