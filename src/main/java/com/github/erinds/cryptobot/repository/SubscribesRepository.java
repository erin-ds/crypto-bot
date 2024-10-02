package com.github.erinds.cryptobot.repository;

import com.github.erinds.cryptobot.model.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribe, UUID> {

    @Transactional
    @Modifying
    @Query("update Subscribe s set s.price = ?1 where s.telegramId = ?2")
    void updatePriceByTelegramId(BigDecimal price, Long telegramId);

    Optional<Subscribe> findByTelegramId(Long telegramId);

    List<Subscribe> findByPriceNotNull();

    @Transactional
    @Modifying
    @Query("update Subscribe s set s.lastSend = ?1 where s.telegramId = ?2")
    void updateLastSendByTelegramId(Instant lastSend, Long telegramId);


}
