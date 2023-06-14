package com.techgn.whisper.repositories;

import com.techgn.whisper.model.entities.Chat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends CrudRepository<Chat, Integer> {

    @Query(value = """
            select * from chat c
            where c.chat_user_one_id = :userId or c.chat_user_two_id = :userId
            """, nativeQuery = true)
    Optional<List<Chat>> findByUserId(@Param("userId") int userId);
}
