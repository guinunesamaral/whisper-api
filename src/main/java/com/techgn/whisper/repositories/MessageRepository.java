package com.techgn.whisper.repositories;

import com.techgn.whisper.model.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
