package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.dto.ListLinksResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserMessageProcessorTest {

    private static UserMessageProcessor userMessageProcessor;
    @BeforeAll
    public static void setUp() {
        ScrapperClient scrapperClient = Mockito.mock(ScrapperClient.class);

        Mono<ListLinksResponse> mono = Mockito.mock(Mono.class);

        Mockito.when(scrapperClient.getLinks(Mockito.eq(1L))).thenReturn(mono);
        Mockito.when(scrapperClient.getChat(Mockito.eq(1L))).thenThrow(new IllegalArgumentException());

        Map<String, Command> commands = Map.of(
            "startCommand", new StartCommand(scrapperClient),
            "helpCommand", new HelpCommand(),
            "listCommand", new ListCommand(scrapperClient)
        );

        userMessageProcessor = new UserMessageProcessor(commands);
    }

    @Test
    void notAuthorisedTest() {
        // Arrange
        Update update1 = mockUpdate(1L, "/list");
        Update update2 = mockUpdate(1L, "/help");

        // Act
        SendMessage sendMessage1 = userMessageProcessor.process(update1);
        String text1 = (String) sendMessage1.getParameters().get("text");
        SendMessage sendMessage2 = userMessageProcessor.process(update2);
        String text2 = (String) sendMessage2.getParameters().get("text");

        // Assert
        assertEquals("Чат не зарегистрирован", text1);
        assertNotEquals("Чат не зарегистрирован", text2);
    }

    @Test
    void unsupportedCommandTest() {
        // Arrange
        Update update = mockUpdate(1L, "/command");

        // Act
        SendMessage message = userMessageProcessor.process(update);
        String text = (String) message.getParameters().get("text");

        // Assert
        assertEquals("Неизвестная команда", text);
    }

    private Update mockUpdate(Long chatId, String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.text()).thenReturn(text);

        Mockito.when(chat.id()).thenReturn(chatId);
        return update;
    }
}
