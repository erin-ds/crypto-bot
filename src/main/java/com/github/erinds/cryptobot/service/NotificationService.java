package com.github.erinds.cryptobot.service;


import com.github.erinds.cryptobot.bot.CryptoBot;
import com.github.erinds.cryptobot.client.BinanceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NotificationService {

    @Value("${telegram.bot.notify.delay.value}")
    private int notificationTimeout;

    @Autowired
    private BinanceClient binanceClient;

    @Autowired
    private SubscribesService subscribesService;

    @Autowired
    private CryptoBot bot;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRateString = "${telegram.bot.update-timeout.value}")
    public void notificate() throws IOException {

        BigDecimal currentPrice = BigDecimal.valueOf(binanceClient.getBitcoinPrice());
        subscribesService.getAllSubscribes()
                .stream()
                .filter(subscribe -> subscribe.getPrice().compareTo(currentPrice) > 0)
                .filter(subscribe -> hasLastNotificationLaterThanTimeout(notificationTimeout, subscribe.getLastSend()))
                .forEach(subscribe -> {
                    sendNotification(currentPrice, subscribe.getTelegramId());
                    subscribesService.updateSendDate(subscribe.getTelegramId());
                });
    }

    private void sendNotification(BigDecimal currentPrice, Long telegramId) {
        SendMessage message = new SendMessage();
        message.setChatId(telegramId);
        message.setText(String.format("Пора покупать, стоимость биткоина %s USD", currentPrice));
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка при отправке сообщения");
        }

    }

    private boolean hasLastNotificationLaterThanTimeout(int notificationTimeout, Instant lastSend) {
        if (lastSend == null) {
            return true;
        } else {
            return Instant.now().isAfter(lastSend.plus(notificationTimeout, ChronoUnit.MINUTES));
        }
    }


}
