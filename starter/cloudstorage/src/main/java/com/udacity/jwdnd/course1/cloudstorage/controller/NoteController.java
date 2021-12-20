package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    @PostMapping()
    public String addOrUpdateNote(@ModelAttribute Note note, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        int userId = user.getUserId();
        Integer noteId = note.getNoteId();
        try {
            if (noteId != null && noteId > 0) {
                noteService.updateNote(note);
            } else {
                int rowsAdded = noteService.insertNote(note, userId);
                if (rowsAdded <= 0) {
                    return "redirect:/result?error";
                }
            }
        } catch (Exception e) {
            return "redirect:/result?limits";
        }
        logNotes("addOrUpdateNote");
        return "redirect:/result?success";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable int id) {
        logger.info("Requesting delete for noteId: " + id);
        if (id > 0) {
            int rowsDeleted = noteService.deleteNote(id);
            if (rowsDeleted <= 0) {
                return "redirect:/result?error";
            }
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }

    private void logNotes(String methodName) {
        logger.info(methodName);
        List<Note> notes = noteService.getAllNotes();
        for (Note note : notes) {
            logger.info(note.toString());
        }
    }

}
