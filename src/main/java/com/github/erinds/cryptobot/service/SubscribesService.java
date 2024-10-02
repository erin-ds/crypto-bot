package com.github.erinds.cryptobot.service;

import com.github.erinds.cryptobot.model.Subscribe;
import com.github.erinds.cryptobot.repository.SubscribesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscribesService {

    @Autowired
    private SubscribesRepository subscribesRepository;

    public void registerUser(Long telegramId) {
        Subscribe subscribe = new Subscribe();
        subscribe.setTelegramId(telegramId);

        subscribesRepository.save(subscribe);
    }

    public void subscribe(BigDecimal price, Long telegramId) {
        subscribesRepository.updatePriceByTelegramId(price, telegramId);
    }

    public Optional<Subscribe> findByTelegramId(Long telegramId) {
        return subscribesRepository.findByTelegramId(telegramId);
    }

    public void unsubscribe(Long telegramId) {
        subscribesRepository.updatePriceByTelegramId(null, telegramId);
    }

    public List<Subscribe> getAllSubscribes() {
        return subscribesRepository.findByPriceNotNull();
    }

    public void updateSendDate(Long telegramId) {
        subscribesRepository.updateLastSendByTelegramId(Instant.now(), telegramId);
    }

}
