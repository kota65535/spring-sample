package com.sre.aip.sample.core.app.controller;

import org.example.api.PetsApi;
import org.example.model.Pet;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PetsApiImpl implements PetsApi {

    @Override
    public ResponseEntity<Void> createPets() {
        return null;
    }

    @Override
    public ResponseEntity<List<Pet>> listPets(Integer limit) {
        return null;
    }
}
