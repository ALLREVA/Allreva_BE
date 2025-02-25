package com.backend.allreva.support;

import java.util.Arrays;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BeanArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.PriorityConstructorArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FixtureUtil {

    public static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(new FailoverIntrospector(
            Arrays.asList(
                FieldReflectionArbitraryIntrospector.INSTANCE,
                PriorityConstructorArbitraryIntrospector.INSTANCE,
                BeanArbitraryIntrospector.INSTANCE
                //BuilderArbitraryIntrospector.INSTANCE
            )
        ))
        .plugin(new JakartaValidationPlugin())
        .defaultNotNull(true) // FixtureMonkey의 대입값에 null 허용하지 않음.
        .build();
}