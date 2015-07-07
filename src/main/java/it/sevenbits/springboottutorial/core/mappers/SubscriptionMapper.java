package it.sevenbits.springboottutorial.core.mappers;

import it.sevenbits.springboottutorial.core.domain.Subscription;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SubscriptionMapper {

    @Select("SELECT id, name, email FROM subscriptions")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "email", property = "email"),
        @Result(column = "name", property = "name")
    })
    List<Subscription> findAll();

    @Insert("INSERT INTO subscriptions (email, name) VALUES (#{email}, #{name})")
    void save(final Subscription subscription);
}
