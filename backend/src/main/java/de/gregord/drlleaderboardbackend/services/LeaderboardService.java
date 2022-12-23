package de.gregord.drlleaderboardbackend.services;

import de.gregord.drlleaderboardbackend.domain.OverallRankingView;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntry;
import de.gregord.drlleaderboardbackend.entities.LeaderboardEntryMinimal;
import de.gregord.drlleaderboardbackend.repositories.LeaderboardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final ModelMapper modelMapper;

    public LeaderboardService(
            LeaderboardRepository leaderboardRepository,
            ModelMapper modelMapper
    ) {
        this.leaderboardRepository = leaderboardRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void saveAndDeleteLeaderboardEntries(
            Collection<LeaderboardEntry> leaderboardEntriesToBeSaved,
            Collection<LeaderboardEntry> leaderboardEntriesToBeDeleted
    ) {
        List<LeaderboardEntry> leaderboardEntries = saveLeaderboardEntries(leaderboardEntriesToBeSaved);
        setBeatenByEntries(leaderboardEntries);
        deleteLeaderboardEntries(leaderboardEntriesToBeDeleted);
    }

    @Transactional
    public List<LeaderboardEntry> saveLeaderboardEntries(Collection<LeaderboardEntry> leaderboardEntries) {
        return leaderboardRepository.saveAll(leaderboardEntries);
    }

    public void setBeatenByEntries(List<LeaderboardEntry> leaderboardEntries) {
        for (LeaderboardEntry leaderboardEntry : leaderboardEntries) {
            List<LeaderboardEntryMinimal> beatenByEntries = leaderboardEntries.stream()
                    .filter(lbe -> lbe.getScore() < leaderboardEntry.getScore())
                    .filter(lbe -> lbe.getCreatedAt().isAfter(leaderboardEntry.getCreatedAt()))
                    .limit(5)
                    .sorted((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()) * -1)
                    .map(lbe -> {
                        return modelMapper.map(lbe, LeaderboardEntryMinimal.class);
                    })
                    .toList();
            leaderboardEntry.setBeatenBy(beatenByEntries);
//            beatenByEntries.forEach(lbe -> lbe.getBeats().add(leaderboardEntry));
        }
    }

    @Transactional
    public void deleteLeaderboardEntries(Collection<LeaderboardEntry> leaderboardEntries) {
        leaderboardRepository.deleteAll(leaderboardEntries);
    }

    public List<OverallRankingView> getOverallRanking(int page, int limit) throws Exception {
        if (page < 1) throw new Exception("Page must be greater than 0");
        if (limit < 1) throw new Exception("Limit must be greater than 0");
        int offset = (page - 1) * limit;
        return leaderboardRepository.getOverallRanking(limit, offset);
    }
}
