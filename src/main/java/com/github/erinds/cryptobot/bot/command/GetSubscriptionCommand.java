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

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    @Autowired
    private SubscribesService subscribesService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        BigDecimal currentPrice = null;

        Optional<Subscribe> subscribe = subscribesService.findByTelegramId(message.getChat().getId());
        if (subscribe.isPresent()) {
            currentPrice = subscribe.get().getPrice();
        }

        String answerText = currentPrice == null ? "Активные подписки отсутствуют"
                : String.format("Вы подписаны на стоимость биткоина %s USD", currentPrice);

        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            answer.setText(answerText);
            absSender.execute(answer);
        } catch (Exception e) {
            log.error("Ошибка возникла /get_subscription методе", e);
        }
    }

}