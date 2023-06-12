package de.gregord.drlleaderboardbackend;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyCustomRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(org.hibernate.dialect.PostgreSQLDialect.class, MemberCategory.values());
        hints.reflection().registerType(org.postgresql.util.PGobject.class, MemberCategory.values());
        hints.reflection().registerType(de.gregord.drlleaderboardbackend.entities.TsidGenerator.class, MemberCategory.values());
        hints.reflection().registerType(de.gregord.drlleaderboardbackend.config.CustomHibernateJsonFormatter.class, MemberCategory.values());

        // JSONB Serialization
        hints.serialization().registerType(de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound.class);
        hints.serialization().registerType(de.gregord.drlleaderboardbackend.entities.tournament.TournamentRanking.class);
        hints.serialization().registerType(de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound.Player.class);
        hints.serialization().registerType(de.gregord.drlleaderboardbackend.entities.tournament.TournamentRound.Match.class);
        hints.serialization().registerType(java.lang.Number.class);
        hints.serialization().registerType(java.lang.Long.class);
        hints.serialization().registerType(java.lang.Integer.class);
        hints.serialization().registerType(java.lang.String.class);
        hints.serialization().registerType(java.lang.Boolean.class);
        hints.serialization().registerType(java.time.LocalDateTime.class);
        // This class is final so we cant reference it, so it is registered via /META-INF/native-image/serialization-config.json
//        hints.serialization().registerType(java.time.Ser.class);

        // JPA Repository Stream, fixed in: https://github.com/spring-projects/spring-data-jpa/issues/2848
        hints.reflection().registerType(org.springframework.data.jpa.repository.Query.class, MemberCategory.INVOKE_PUBLIC_METHODS);
        hints.reflection().registerType(jakarta.persistence.Query.class, MemberCategory.INVOKE_PUBLIC_METHODS);

        hints.resources().registerPattern("db/migration/*");
        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.TrackView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView.LeaderboardByPlayerView_Track.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.LeaderboardByPlayerView.LeaderboardByPlayerView_LeaderboardEntryMinimal.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.LeaderboardActivityView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.LeaderboardMostPbsView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.LeaderboardMostTrackEntriesView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.WorstTracksView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );

        hints.proxies().registerJdkProxy(
                de.gregord.drlleaderboardbackend.domain.ReplaysByTrackView.class,
                org.springframework.data.projection.TargetAware.class,
                org.springframework.aop.SpringProxy.class,
                org.springframework.core.DecoratingProxy.class
        );
    }
}
