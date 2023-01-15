package de.gregord.drlleaderboardbackend;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class MyCustomRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(org.hibernate.dialect.PostgreSQLDialect.class, MemberCategory.values());
        hints.reflection().registerType(org.postgresql.util.PGobject.class, MemberCategory.values());
        hints.reflection().registerType(de.gregord.drlleaderboardbackend.entities.TsidGenerator.class,MemberCategory.values());
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
    }
}
