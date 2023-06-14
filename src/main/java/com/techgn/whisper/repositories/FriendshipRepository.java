package com.techgn.whisper.repositories;

import com.techgn.whisper.model.entities.Friendship;
import com.techgn.whisper.model.entities.FriendshipKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipKey> {
    @Query(value = """
             select * from friendship f
             where f.user_one_id = :userOneId and f.user_two_id = :userTwoId
                or f.user_one_id = :userTwoId and f.user_two_id = :userOneId
            """, nativeQuery = true)
    Optional<Friendship> findByUserOneAndUserTwoIds(@Param("userOneId") Integer userOneId,
                                                    @Param("userTwoId") Integer userTwoId);

    @Query(value = """
            select * from friendship f
            where f.user_one_id = :userId or f.user_two_id = :userId
            """, nativeQuery = true)
    Optional<List<Friendship>> findByUserOneOrTwoIdEqualsTo(@Param("userId") int userId);
}
