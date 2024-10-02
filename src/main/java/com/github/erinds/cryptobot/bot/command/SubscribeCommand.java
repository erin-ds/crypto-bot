package com.github.erinds.cryptobot.bot.command;

import com.github.erinds.cryptobot.service.SubscribesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {

    @Autowired
    private SubscribesService subscribesService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        String argument = String.join("", arguments).replace(",", ".");
        String answerText;

        if (!argument.matches("\\d+.?\\d+")) {
            answerText = "Допустимы только целые или дробные числа, разделенные точкой или запятой";
        } else {

            BigDecimal price = new BigDecimal(argument);
            subscribesService.subscribe(price, message.getChat().getId());
            answerText = String.format("Вы подписаны на стоимость биткоина %s USD", price);
        }

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        answer.setText(answerText);

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /subscribe command", e);
        }

    }
}