package com.example.service.api;

import java.util.List;


public interface CharacterMapService {
    List<List<String>> getCharacterMapCatalogue();
    List<List<String>> getCharacterMapFiles(String name);
}