package com.example.chatapp.repository;

import com.example.chatapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderByTimestampAsc(String roomId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :sender AND m.receiver = :receiver) OR " +
            "(m.sender = :receiver AND m.receiver = :sender) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            @Param("sender") String sender,
            @Param("receiver") String receiver);

}
