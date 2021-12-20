package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public Integer insertNote(Note note, int userId) {
        note.setUserId(userId);
        return noteMapper.insertNote(note);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public Integer deleteNote(int noteId) {
        return noteMapper.deleteNote(noteId);
    }

    public List<Note> getNotes(int userId) {
        return noteMapper.getNotes(userId);
    }

    public List<Note> getAllNotes() {
        return noteMapper.getAllNotes();
    }

}
