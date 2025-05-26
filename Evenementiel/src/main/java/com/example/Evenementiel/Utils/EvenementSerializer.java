package com.example.Evenementiel.Utils;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EvenementSerializer {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void sauvegarderEvenements(List<Evenement> evenements, String fichier) throws IOException {
        mapper.writeValue(new File(fichier), evenements);
    }

    public static List<Evenement> chargerEvenements(String fichier) throws IOException {
        Evenement[] tab = mapper.readValue(new File(fichier), Evenement[].class);
        return Arrays.asList(tab);
    }
}