package com.saharsh.notes
import java.lang.reflect.Array.set

class NoteModel{

    constructor(note_id: Int, note_heading: String, note_details: String, time: String){
        this.note_heading = note_heading
        this.note_details = note_details
        this.note_id = note_id
        this.time = time

    }

    var note_id : Int
    var note_heading : String
    var note_details : String
    var time: String
}