package com.saharsh.notes

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.note_item.view.*


class NotesItemAdapter(val noteList: ArrayList<NoteModel>, val context: Context, fragmentManager: FragmentManager, contract: Contract) : RecyclerView.Adapter<NotesItemAdapter.ViewHolder>() {

    val fragmentManager = fragmentManager
    val contract = contract
    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvNoteHeading?.text = noteList.get(position).note_heading
        holder.tvNotDetails?.text = noteList.get(position).note_details
        if(holder.tvNotDetails?.text?.trim()==""){
            holder.tvNotDetails?.visibility = View.GONE
        }
        else{
            holder.tvNotDetails?.visibility = View.VISIBLE
        }

        holder.note_item.setOnClickListener(View.OnClickListener {
                showBottomSheet(noteList.get(position), position)
        })



    }


    override fun getItemCount(): Int {
        return noteList.size
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNoteHeading = itemView.note_item_heading
            val tvNotDetails = itemView.note_item_details
            val note_item = itemView.note_item
    }

    fun showBottomSheet(item : NoteModel, pos:Int){
        val bottomSheetFragment = NoteDetailsFragment(item,pos, context,contract)
        bottomSheetFragment.setCancelable(true)
        bottomSheetFragment.show(fragmentManager,bottomSheetFragment.tag)
    }
}
