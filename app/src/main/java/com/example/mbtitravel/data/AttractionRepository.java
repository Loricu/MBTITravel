package com.example.mbtitravel.data;

import com.example.mbtitravel.model.Attraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Sursa de date simulată pentru atracții: o listă ținută în memorie,
 * inițializată cu date hardcodate. Expune operațiile CRUD
 * (adăugare, citire, modificare, ștergere). Datele se resetează la repornirea aplicației.
 */
public class AttractionRepository {

    private static AttractionRepository instance;

    private final List<Attraction> attractions = new ArrayList<>();
    private int nextId = 1;

    private AttractionRepository() {
        add("Ateneul Român", "Cultură", "Sala de concerte emblematică a Bucureștiului, cu concerte de muzică clasică.", Attraction.VIBE_QUIET);
        add("Centrul Vechi", "Viață de noapte", "Străzi pietonale pline de terase, baruri și muzică.", Attraction.VIBE_SOCIAL);
        add("Parcul Herăstrău (Regele Mihai I)", "Natură", "Cel mai mare parc din oraș, cu lac, alei liniștite și plimbări cu barca.", Attraction.VIBE_QUIET);
        add("Muzeul Satului", "Muzeu", "Muzeu în aer liber cu case tradiționale din toată România.", Attraction.VIBE_QUIET);
        add("Piața Unirii", "Oraș", "Piață centrală aglomerată, cu fântâni arteziene și evenimente.", Attraction.VIBE_SOCIAL);
        add("Palatul Parlamentului", "Istorie", "A doua cea mai mare clădire administrativă din lume, cu tururi ghidate în grup.", Attraction.VIBE_SOCIAL);
        add("Cărturești Carusel", "Cultură", "Librărie spectaculoasă într-o clădire restaurată, ideală pentru o după-amiază liniștită.", Attraction.VIBE_QUIET);
    }

    public static synchronized AttractionRepository getInstance() {
        if (instance == null) {
            instance = new AttractionRepository();
        }
        return instance;
    }

    /** READ: toate atracțiile (copie, ca lista internă să nu fie modificată din afară). */
    public List<Attraction> getAll() {
        return new ArrayList<>(attractions);
    }

    /** READ: o singură atracție după id, sau null dacă nu există. */
    public Attraction getById(int id) {
        for (Attraction a : attractions) {
            if (a.id == id) return a;
        }
        return null;
    }

    /** CREATE: adaugă o atracție nouă și o returnează (cu id generat). */
    public Attraction add(String name, String category, String description, String vibe) {
        Attraction attraction = new Attraction(nextId++, name, category, description, vibe);
        attractions.add(attraction);
        return attraction;
    }

    /** UPDATE: modifică o atracție existentă. Returnează false dacă id-ul nu există. */
    public boolean update(int id, String name, String category, String description, String vibe) {
        Attraction a = getById(id);
        if (a == null) return false;
        a.name = name;
        a.category = category;
        a.description = description;
        a.vibe = vibe;
        return true;
    }

    /** DELETE: șterge atracția cu id-ul dat. */
    public boolean delete(int id) {
        Attraction a = getById(id);
        return a != null && attractions.remove(a);
    }
}
