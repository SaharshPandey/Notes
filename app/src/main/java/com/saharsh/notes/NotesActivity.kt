package com.saharsh.notes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.mauker.materialsearchview.MaterialSearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.toolbar_notes.*


class NotesActivity : AppCompatActivity(), Contract {

    override fun updateUI() {
        LoadQuery("%${search!!.text.toString().trim()}%")

    }


    private var noteList : ArrayList<NoteModel>? = null
    private var noteAdapter : NotesItemAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var search: EditText? = null
    private var realSearch: EditText? = null
    private var closeSearch : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        init()

    }

    override fun onResume() {
        super.onResume()

        //Loading all the items...
        LoadQuery("%${search!!.text.toString().trim()}%")

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        recyclerView = findViewById(R.id.recycler_view)
        search = findViewById(R.id.tb_search_tv)
        realSearch = findViewById(R.id.tb_real_search_tv)


        search?.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                openSearch()
                return true
            }
        })

        close.setOnClickListener{
            closeSearch()
        }

        realSearch?.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                LoadQuery("%${p0.toString().trim()}%")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })



        val floatingBtn: FloatingActionButton = findViewById(R.id.floating_btn)
        recyclerView?.setHasFixedSize(true)

        val sGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView?.layoutManager = sGridLayoutManager

        noteList = ArrayList()

        //Loading all the items...
        LoadQuery("%${search!!.text.toString().trim()}%")

        floatingBtn.setOnClickListener {
            showBottomSheet()
        }

    }


    private fun showBottomSheet(){
        val bottomSheetFragment = NoteDetailsFragment(null,null, this,this)
        bottomSheetFragment.isCancelable = true
        bottomSheetFragment.show(supportFragmentManager,bottomSheetFragment.tag)
    }

    private fun LoadQuery(title: String){
        //Load from DB...
        noteList!!.clear()
        val dbManager = DBManager(this)
        val projections = arrayOf("_id", "title", "description", "time")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "title like ?", selectionArgs, null)

        if(cursor.count==0){
            empty_state.visibility = View.VISIBLE
        }
        else {
            empty_state.visibility = View.GONE
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("_id"))
                    val title = cursor.getString(cursor.getColumnIndex("title"))
                    val descrp = cursor.getString(cursor.getColumnIndex("description"))
                    val time = cursor.getString(cursor.getColumnIndex("time"))

                    noteList?.add(NoteModel(id, title, descrp, time))

                } while (cursor.moveToNext())
            }
            recyclerView?.setHasFixedSize(true)

            val sGridLayoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )

            noteList!!.reverse()
            recyclerView?.layoutManager = sGridLayoutManager
            noteAdapter = NotesItemAdapter(noteList!!,this,supportFragmentManager, this)
            recyclerView?.adapter = noteAdapter

        }

    }

    fun openSearch(){
        search?.visibility = View.GONE
        tb_notes_title?.visibility = View.GONE
        search_rl?.visibility = View.VISIBLE
        realSearch?.showKeyboard()

    }

    fun closeSearch(){
        search?.visibility = View.VISIBLE
        tb_notes_title?.visibility = View.VISIBLE
        search_rl?.visibility = View.GONE
        realSearch?.setText("")
        realSearch?.hideKeyboard()
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

    override fun onBackPressed() {
        if(search_rl.visibility == View.VISIBLE){
            closeSearch()
        }
        else{
        super.onBackPressed()
        }
    }


}
