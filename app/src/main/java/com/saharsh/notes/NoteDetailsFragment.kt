package com.saharsh.notes

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.note_bottom_sheet_view.*


class NoteDetailsFragment(
    private var item: NoteModel?, var pos: Int?, context: Context,
    private var contract: Contract
) : BottomSheetDialogFragment() {

    private var check = 0
    private var fragmentView: View? = null
    private var contexts = context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.note_bottom_sheet_view, container,false)
        init()
        return fragmentView
    }

    private fun init(){


        val ll2 :LinearLayout? = fragmentView?.findViewById(R.id.ll2)
        val noteHeadingTv: EditText? = fragmentView?.findViewById(R.id.note_heading)
        val noteDetailsTv: EditText? = fragmentView?.findViewById(R.id.note_details)
        val noteDelete: ImageView? = fragmentView?.findViewById(R.id.delete_notes_img)
        val noteEdit: ImageView? = fragmentView?.findViewById(R.id.edit_notes_img)
        val saveBtn: Button? = fragmentView?.findViewById(R.id.save_note)


        if(item == null){
            ll2?.visibility = View.GONE
            noteHeadingTv?.isEnabled = true
            noteDetailsTv?.isEnabled = true
            saveBtn?.visibility = View.VISIBLE
            check = 0
        }
        else if(item!=null){
            check=1
            noteHeadingTv?.setText(item?.note_heading)
            noteDetailsTv?.setText(item?.note_details)
            saveBtn?.visibility = View.GONE

        }



        noteDelete?.setOnClickListener{
        // Delete Notes..
            deleteNotes()
            contract.updateUI()
            item = null
            dismiss()

        }

        noteEdit?.setOnClickListener{
            noteHeadingTv?.isEnabled = true
            noteDetailsTv?.isEnabled = true
            saveBtn?.visibility = View.VISIBLE
            noteHeadingTv?.setBackgroundResource(R.drawable.editext_bg)
            noteDetailsTv?.setBackgroundResource(R.drawable.editext_bg)
            noteHeadingTv?.showKeyboard()
            noteHeadingTv?.setSelection(noteHeadingTv.text.length)


        }

        saveBtn?.setOnClickListener{
            noteHeadingTv?.isEnabled = false
            noteDetailsTv?.isEnabled = false
            ll2?.visibility = View.VISIBLE
            saveBtn.visibility = View.GONE

            noteHeadingTv?.setBackgroundResource(R.drawable.editext_plain_bg)
            noteDetailsTv?.setBackgroundResource(R.drawable.editext_plain_bg)


            if(noteHeadingTv!!.text!!.length>0){

                if(item !=null){
                    updateNotes()
                }
                else{
                    addNotes()
                }
            }
            item = null
            contract.updateUI()

        }

    }

    fun addNotes(){
        val dbManager = DBManager(contexts)
        val values = ContentValues()

        values.put("title",note_heading.text.toString().trim()+"")
        values.put("description",note_details.text.toString().trim()+"")
        values.put("time",System.currentTimeMillis().toString()+"")

        val ID = dbManager.Insert(values)
        if(ID>0){
            dismiss()
            Toast.makeText(contexts, "Your note is added...", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(contexts, "Unable to add!!!", Toast.LENGTH_SHORT).show()
        }


    }

    fun deleteNotes(){
        val dbManager = DBManager(contexts)
        val selectionArgs = arrayOf(item?.note_id.toString())
        dbManager.Delete("_id=?", selectionArgs)
    }

    fun updateNotes(){
        val dbManager = DBManager(contexts)

        val values = ContentValues()
        values.put("title", note_heading.text.toString().trim()+"")
        values.put("description", note_details.text.toString().trim()+"")
        values.put("time",System.currentTimeMillis().toString()+"")

        val selectionArgs = arrayOf(item?.note_id.toString())
        val ID = dbManager.Update(values,"_id=?",selectionArgs)

        dismiss()
        if(ID>0){
            Toast.makeText(contexts, "Your note is updated...", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(contexts, "Unable to update!!!", Toast.LENGTH_SHORT).show()
        }


    }
    fun View.showKeyboard() {
        this.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }




}