package com.github.erinds.cryptobot.bot.command;

import com.github.erinds.cryptobot.service.SubscribesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Обработка команды начала работы с ботом
 */
@Service
@AllArgsConstructor
@Slf4j
public class StartCommand implements IBotCommand {

    @Autowired
    private SubscribesService subscribesService;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        Long telegramId = message.getChat().getId();
        if (subscribesService.findByTelegramId(telegramId).isEmpty()) {
            subscribesService.registerUser(telegramId);
        }

        SendMessage answer = getSendMessage(message);
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }

    private static SendMessage getSendMessage(Message message) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());

        answer.setText("""
                Привет! Данный бот помогает отслеживать стоимость биткоина.
                После подписки, при достижении цены, близкой к указанной, Вам будет направлено уведомление
                Для регистрации выполните команду /start
                Поддерживаемые команды:
                 /get_price - получить стоимость биткоина
                 /subscribe [число] - подписаться на определенную стоимость. Допустимо целое или дробное число, разделенное точкой или запятой
                 /get_subscription - вывести информацию о текущей подписке
                 /unsubscribe - отменить подписку
                """);
        return answer;
    }

}