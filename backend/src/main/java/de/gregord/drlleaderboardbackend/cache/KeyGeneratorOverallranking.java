package de.gregord.drlleaderboardbackend.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Set;

public class KeyGeneratorOverallranking implements KeyGenerator {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Key {
        private Set<Integer> mapCategories;
        private int limit;
        private int offset;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return new EqualsBuilder().append(limit, key.limit).append(offset, key.offset).append(mapCategories, key.mapCategories).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(mapCategories).append(limit).append(offset).toHashCode();
        }
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return new Key((Set<Integer>) params[0], (int) params[1], (int) params[2]);
    }
}
