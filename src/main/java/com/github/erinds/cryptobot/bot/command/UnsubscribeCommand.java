package com.github.erinds.cryptobot.bot.command;

import com.github.erinds.cryptobot.model.Subscribe;
import com.github.erinds.cryptobot.service.SubscribesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    @Autowired
    private SubscribesService subscribesService;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Optional<Subscribe> subscribe = subscribesService.findByTelegramId(message.getChat().getId());
        subscribe.ifPresent(value -> subscribesService.unsubscribe(value.getTelegramId()));

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            answer.setText("Подписка отменена");
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /unsubscribe методе", e);
        }
    }
}