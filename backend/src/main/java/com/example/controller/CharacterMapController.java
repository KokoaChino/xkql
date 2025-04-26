package com.example.controller;

import com.example.service.api.CharacterMapService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/character-map")
public class CharacterMapController {

    @Resource
    CharacterMapService service;

    @GetMapping("/get-character-map-catalogue")
    public List<List<String>> getCharacterMapCatalogue() {
        return service.getCharacterMapCatalogue();
    }

    @PostMapping("/set-name")
    public void setName(@RequestBody String name, HttpSession session) {
        session.setAttribute("characterName", name);
    }

    @PostMapping("/get-name")
    public String getName(HttpSession session) {
        return (String) session.getAttribute("characterName");
    }

    @PostMapping("/get-character-map-files")
    public List<List<String>> getCharacterMapFiles(@RequestBody String name) {
        name = name.replace("\"", "");
        return service.getCharacterMapFiles(name);
    }
}