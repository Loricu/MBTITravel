package com.example.mbtitravel.model;

/**
 * Un loc de vizitat din oraș. Datele sunt simulate (ținute în memorie),
 * vezi {@link com.example.mbtitravel.data.AttractionRepository}.
 */
public class Attraction {

    /** Atmosfera locului, folosită pentru a-l recomanda extrovertiților (SOCIAL) sau introvertiților (LINISTIT). */
    public static final String VIBE_SOCIAL = "Social";
    public static final String VIBE_QUIET = "Liniștit";

    public int id;
    public String name;
    public String category;
    public String description;
    public String vibe;

    public Attraction(int id, String name, String category, String description, String vibe) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.vibe = vibe;
    }

    /** Recomandat dacă atmosfera locului se potrivește cu dimensiunea E/I a tipului MBTI. */
    public boolean isRecommendedFor(String mbti) {
        if (mbti == null || mbti.isEmpty()) return false;
        boolean extrovert = mbti.toUpperCase().startsWith("E");
        return extrovert == VIBE_SOCIAL.equals(vibe);
    }
}
