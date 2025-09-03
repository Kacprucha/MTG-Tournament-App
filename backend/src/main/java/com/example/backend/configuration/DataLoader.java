package com.example.backend.configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.backend.entities.Achievement;
import com.example.backend.entities.AchievementAggregationType;
import com.example.backend.entities.Scoreboard;
import com.example.backend.entities.Tournament;
import com.example.backend.entities.TournamentStatus;
import com.example.backend.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner
{
    private final TournamentRepository tournamentRepository;

    @Override
    public void run(String... args) throws Exception 
    {
        if (tournamentRepository.count() == 0) 
        {
            log.info("Database is empty. Seeding historical tournament data...");
            createHistoricalTournament();
            log.info("Historical data seeded successfully.");
        } else 
        {
            log.info("Database is not empty. Skipping data seeding.");
        }
    }

    private void createHistoricalTournament() 
    {
        // Dane wejściowe (historyczne dane) 
        String legacyTournamentName = "Turniej kurwa";

        // Wyniki graczy
        Map<String, Float> playerPoints = Map.of(
            "Filip", 7f,
            "Ozik", 9f,
            "Zosia", 6f,
            "Oskar", 8f,
            "Kapcer", 5f,
            "QWW", 5f,
            "Marcin", 4f,
            "Albert", 4f,
            "Seba", 1f,
            "Nikodem", 0f
        );
        
        // Wyniki osiągnięć per gracz (Nazwa Osiągnięcia -> Wartość)
        Map<String, Map<String, Integer>> playerAchievements = Map.of(
            "Filip", Map.of ("The most destroyed permaments without combat damage", 2, 
                                "The most life gained", 51, 
                                "The most cards in graveyard at the end of the single game", 3,
                                "The highest amount of dmg dealt in a single turn", 19, 
                                "The highest amount of +1/+1 counters on cretures", 6, 
                                "The highest of lands at the end of the game", 9,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 10, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Ozik", Map.of ("The most destroyed permaments without combat damage", 13, 
                                "The most life gained", 8, 
                                "The most cards in graveyard at the end of the single game", 9,
                                "The highest amount of dmg dealt in a single turn", 9, 
                                "The highest amount of +1/+1 counters on cretures", 9, 
                                "The highest of lands at the end of the game", 13,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 6, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Zosia", Map.of ("The most destroyed permaments without combat damage", 4, 
                                "The most life gained", 27, 
                                "The most cards in graveyard at the end of the single game", 4,
                                "The highest amount of dmg dealt in a single turn", 16, 
                                "The highest amount of +1/+1 counters on cretures", 3, 
                                "The highest of lands at the end of the game", 7,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 8, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Oskar", Map.of ("The most destroyed permaments without combat damage", 10, 
                                "The most life gained", 9, 
                                "The most cards in graveyard at the end of the single game", 7,
                                "The highest amount of dmg dealt in a single turn", 24, 
                                "The highest amount of +1/+1 counters on cretures", 5, 
                                "The highest of lands at the end of the game", 11,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 6, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Kapcer", Map.of ("The most destroyed permaments without combat damage", 2, 
                                "The most life gained", 164, 
                                "The most cards in graveyard at the end of the single game", 10,
                                "The highest amount of dmg dealt in a single turn", 13, 
                                "The highest amount of +1/+1 counters on cretures", 7, 
                                "The highest of lands at the end of the game", 10,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 8, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "QWW", Map.of ("The most destroyed permaments without combat damage", 10, 
                                "The most life gained", 0, 
                                "The most cards in graveyard at the end of the single game", 13,
                                "The highest amount of dmg dealt in a single turn", 14, 
                                "The highest amount of +1/+1 counters on cretures", 0, 
                                "The highest of lands at the end of the game", 9,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 6, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Marcin", Map.of ("The most destroyed permaments without combat damage", 8, 
                                "The most life gained", 10, 
                                "The most cards in graveyard at the end of the single game", 8,
                                "The highest amount of dmg dealt in a single turn", 28, 
                                "The highest amount of +1/+1 counters on cretures", 20, 
                                "The highest of lands at the end of the game", 8,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 8, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Albert", Map.of ("The most destroyed permaments without combat damage", 4, 
                                "The most life gained", 3, 
                                "The most cards in graveyard at the end of the single game", 10,
                                "The highest amount of dmg dealt in a single turn", 14, 
                                "The highest amount of +1/+1 counters on cretures", 4, 
                                "The highest of lands at the end of the game", 9,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 7, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Seba", Map.of ("The most destroyed permaments without combat damage", 7, 
                                "The most life gained", 14, 
                                "The most cards in graveyard at the end of the single game", 13,
                                "The highest amount of dmg dealt in a single turn", 11, 
                                "The highest amount of +1/+1 counters on cretures", 10, 
                                "The highest of lands at the end of the game", 9,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 5, 
                                "Total amount of cards casted from other zones than your hand", 0),
            "Nikodem", Map.of ("The most destroyed permaments without combat damage", 5, 
                                "The most life gained", 45, 
                                "The most cards in graveyard at the end of the single game", 10,
                                "The highest amount of dmg dealt in a single turn", 13, 
                                "The highest amount of +1/+1 counters on cretures", 4, 
                                "The highest of lands at the end of the game", 10,
                                "The highest amount of creatures at the battlefield at any given moment in a game", 7, 
                                "Total amount of cards casted from other zones than your hand", 0)
        );
        
        List<String> usernames = new ArrayList<>(playerPoints.keySet());
        List<UUID> mockIds = usernames.stream()
            .map(u -> UUID.randomUUID())
            .collect(Collectors.toList());

        Tournament legacyTournament = Tournament.builder()
            .status(TournamentStatus.FINISHED)
            .name(legacyTournamentName)
            .addon("March of the Machine")
            .type("draft")
            .date(LocalDate.of(2025, 4, 12))
            .isLegacy(true)
            .participantsUsernames(usernames)
            .participantsIds(mockIds)
            .build();

        tournamentRepository.save(legacyTournament);

        // Lista osiągnięć z tego turnieju
        List<Achievement> achievements = List.of(
            Achievement.builder()
                .name("The most destroyed permaments without combat damage")
                .price("Child of Alara")
                .winnerUsername("Ozik")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("The most life gained")
                .price("Bilbo, Birthday Celebrant")
                .winnerUsername("Kacper")
                .aggregationType(AchievementAggregationType.SUM).build(),
            Achievement.builder()
                .name("The most cards in graveyard at the end of the single game")
                .price("Muldrotha, the Gravetide")
                .winnerUsername("QWW")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("The highest amount of dmg dealt in a single turn")
                .price("Gisela, blade of goldnight")
                .winnerUsername("Marcin")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("The highest amount of +1/+1 counters on cretures")
                .price("Shalai and Hallar")
                .winnerUsername("Marcin")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("The highest of lands at the end of the game")
                .price("Omo, queen of Vesuva")
                .winnerUsername("Ozik")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("The highest amount of creatures at the battlefield at any given moment in a game")
                .price("Adrix and Nev, Twincasters")
                .winnerUsername("Filip")
                .aggregationType(AchievementAggregationType.MAX).build(),
            Achievement.builder()
                .name("Total amount of cards casted from other zones than your hand")
                .price("Grolnok, the Omnivore")
                .winnerUsername("Nikodem")
                .aggregationType(AchievementAggregationType.MAX).build()
        );

        achievements.forEach(legacyTournament::addAchievement);

        final Tournament tournamentFromRepo = tournamentRepository.save(legacyTournament);

        // Stwórz mapę `Nazwa -> Encja Achievement` dla łatwego dostępu
        Map<String, Achievement> savedAchievementsByName = tournamentFromRepo.getAchievements().stream()
                .collect(Collectors.toMap(Achievement::getName, Function.identity()));

        List<String> usernamesList = new ArrayList<>(usernames);
        List<UUID> mockIdsList = new ArrayList<>(mockIds);
        Map<String, UUID> idsByUsername = new HashMap<>();
        for (int i = 0; i < usernamesList.size(); i++) 
        {
            idsByUsername.put(usernamesList.get(i), mockIdsList.get(i));
        }

        playerPoints.forEach((username, points) -> 
        {
            Scoreboard entry = new Scoreboard();
            entry.setUsername(username);
            entry.setUsername(username);
            entry.setUserKeycloakId(idsByUsername.get(username));
            entry.setPoints(points);
            
            Map<String, Integer> achievementsForPlayer = playerAchievements.get(username);
            if (achievementsForPlayer != null) 
            {
                Map<Long, Integer> achievementsMapForDb = new HashMap<> ();
                achievementsForPlayer.forEach((achievementName, value) -> 
                {
                    Achievement definition = savedAchievementsByName.get(achievementName);
                    if (definition != null && definition.getId() != null) 
                    {
                        achievementsMapForDb.put(definition.getId(), value);
                    }
                    else 
                    {
                        log.warn("Could not find achievement definition for name: '{}'. Skipping.", achievementName);
                    }
                });
                entry.setAchievements(achievementsMapForDb);
            }
            
            tournamentFromRepo.addScoreboardEntry(entry);
        });

        tournamentRepository.save(tournamentFromRepo);
    }
}
