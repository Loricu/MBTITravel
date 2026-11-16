# MBTI Travel

Aplicație Android care recomandă un stil de călătorie și de explorare a orașului în funcție de tipul de personalitate MBTI al utilizatorului.

## 1. Scopul aplicației

**Domeniu:** cultură / timp liber, mai exact turism urban.

Turiștii nu explorează un oraș toți la fel: unii caută locuri aglomerate și evenimente, alții preferă parcuri și muzee liniștite; unii vor un itinerar fix, alții preferă spontaneitatea. MBTI Travel:

1. îl autentifică pe utilizator (cu opțiunea „ține-mă minte”);
2. îi determină tipul MBTI printr-un test scurt cu 4 întrebări (E/I, S/N, T/F, J/P) sau îl lasă să își introducă direct tipul, dacă îl știe deja;
3. generează recomandări personalizate: mijloc de transport, tipuri de atracții, stil de planificare;
4. oferă o listă de **locuri de vizitat** pe care utilizatorul o poate gestiona (adăugare, editare, ștergere); locurile potrivite tipului său sunt marcate cu ⭐;
5. afișează harta orașului (București, ca punct de plecare), cu locația curentă a utilizatorului.

## 2. Alegeri tehnologice

| Tehnologie | Folosire |
|---|---|
| **Java** (+ `MainActivity` în Kotlin) | Limbajul principal al aplicației |
| **Android SDK** (minSdk 24, targetSdk 37) | Platforma țintă |
| **Gradle Kotlin DSL** + Version Catalog (`libs.versions.toml`) | Build și gestionarea dependențelor |
| **AndroidX Navigation Component** | Navigarea între ecrane (un singur Activity, mai multe Fragmente, `nav_graph.xml`) |
| **Fragments + layout-uri XML** | Interfața (ConstraintLayout, Material Components) |
| **RecyclerView** | Lista de atracții |
| **Room** (SQLite) | Baza de date locală: profilul MBTI și utilizatorii |
| **SharedPreferences** | Reține numele utilizatorului („Remember me”) |
| **Google Maps SDK + Play Services Location** | Harta și locația GPS |
| **Retrofit + Gson** | Inclus pentru etapele următoare (servicii web / JSON) |

**De ce:** arhitectura cu un singur Activity și Navigation Component păstrează navigarea declarativă, vizibilă într-un singur fișier. Room oferă persistență tipizată, cu interogări SQL verificate la compilare. Operațiile pe baza de date rulează pe un thread secundar, ca să nu blocheze interfața.

## 3. Structura proiectului

```
MBTITravel/
├── build.gradle.kts, settings.gradle.kts   # configurare Gradle la nivel de proiect
├── gradle/libs.versions.toml               # versiunile dependențelor
└── app/
    ├── build.gradle.kts                    # dependențele modulului (Room, Maps, Navigation...)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/mbtitravel/
        │   ├── MainActivity.kt             # gazda NavHostFragment
        │   ├── model/                      # modele de date
        │   │   ├── Attraction.java         # un loc de vizitat (date simulate)
        │   │   ├── User.java               # utilizator (nume, prenume, vârstă)
        │   │   └── UserProfile.java        # profilul MBTI salvat
        │   ├── data/                       # stratul de acces la date
        │   │   ├── AttractionRepository.java # listă în memorie + CRUD pe atracții
        │   │   ├── AppDatabase.java        # baza de date Room (singleton)
        │   │   ├── UserDao.java            # operații CRUD pe utilizatori
        │   │   └── UserProfileDao.java     # operații pe profilul MBTI
        │   └── ui/                         # ecranele aplicației (Fragmente)
        │       ├── LoginFragment.java      # autentificare + demo de operații CRUD în Room
        │       ├── HomeFragment.java       # alegere: fă testul / introdu tipul MBTI
        │       ├── TestFragment.java       # testul MBTI cu 4 întrebări
        │       ├── ProfileFragment.java    # rezultatul + recomandări de călătorie
        │       ├── AttractionsFragment.java # lista de atracții (RecyclerView)
        │       ├── AttractionAdapter.java  # adaptorul listei
        │       ├── DetailsFragment.java    # formular adăugare / editare / ștergere
        │       └── MapFragment.java        # Google Maps + locație GPS
        └── res/
            ├── layout/                     # câte un XML pentru fiecare ecran
            ├── navigation/nav_graph.xml    # graful de navigare
            └── values/                     # culori, texte, teme
```

### Fluxul de navigare

```
Login ──► Home ──► Test ──► Profil ──► Locuri de vizitat ──► Detalii (adaugă/editează)
  │         └──────────────────▲   └──► Hartă
  └────────── (profil existent) ┘
```

### Funcționalități CRUD (date simulate)

Atracțiile sunt păstrate în memorie, în `AttractionRepository`, care pornește cu 7 locuri din București hardcodate. Datele se resetează la repornirea aplicației.

| Operație | Cum se face în aplicație |
|---|---|
| **Create** | butonul **+** din lista de atracții → formular → „Adaugă” |
| **Read** | lista din ecranul „Locuri de vizitat” |
| **Update** | apasă pe o atracție → modifică câmpurile → „Salvează modificările” |
| **Delete** | iconița de coș, apăsare lungă sau butonul „Șterge” din formular (cu confirmare și opțiune de anulare) |

## 4. Rulare

1. Deschide proiectul în Android Studio și sincronizează Gradle.
2. Rulează aplicația pe un emulator sau pe un dispozitiv cu Android 7.0+.

## 5. Limitări cunoscute (prototip)

- **Harta nu este încă configurată.** Cheia Google Maps API nu a fost adăugată, așa că ecranul „Hartă” nu funcționează în această versiune. Integrarea completă este planificată pentru etapele următoare. Pentru activare:
  - adaugă în `AndroidManifest.xml`, în `<application>`, elementul `<meta-data android:name="com.google.android.geo.API_KEY" android:value="CHEIA_TA" />`;
  - adaugă permisiunea `ACCESS_FINE_LOCATION`.
- Atracțiile sunt ținute doar în memorie (date simulate), deci modificările se pierd la repornirea aplicației.
- Retrofit/Gson sunt incluse, dar nu sunt încă folosite.
