package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMessageProcessorTest {

    private UserMessageProcessor userMessageProcessor;

    private UserData userData;

    @BeforeEach
    public void setUp() {
        this.userData = new UserData();

        Map<String, Command> commands = Map.of(
            "startCommand", new StartCommand(userData),
            "helpCommand", new HelpCommand(userData),
            "trackCommand", new TrackCommand(userData),
            "untrackCommand", new UntrackCommand(userData),
            "listCommand", new ListCommand(userData)
        );

        this.userMessageProcessor = new UserMessageProcessor(commands);
    }

    @Test
    void notAuthorisedTest() {
        // Arrange
        Update update = mockUpdate("username", "/list");
        Update update1 = mockUpdate("username", "/help");

        // Act
        SendMessage sendMessage = userMessageProcessor.process(update);
        String text = (String) sendMessage.getParameters().get("text");

        SendMessage sendMessage1 = userMessageProcessor.process(update1);
        String text1 = (String) sendMessage1.getParameters().get("text");

        // Assert
        assertEquals(UserMessageProcessor.UNAUTHORISED, text);
        assertNotEquals(UserMessageProcessor.UNAUTHORISED, text1);
    }

    @Test
    void unsupportedCommandTest() {
        // Arrange
        Update update = mockUpdate("username", "/command");

        // Act
        SendMessage message = userMessageProcessor.process(update);
        String text = (String) message.getParameters().get("text");

        // Assert
        assertEquals(UserMessageProcessor.UNKNOWN_COMMAND, text);
    }

    @Test
    void authoriseTest() {
        // Arrange
        Update update = mockUpdate("user1", "/start");
        Update update1 = mockUpdate("user1", "/list");

        // Act
        SendMessage message = userMessageProcessor.process(update);
        String text = (String) message.getParameters().get("text");

        SendMessage message1 = userMessageProcessor.process(update1);
        String text1 = (String) message1.getParameters().get("text");

        // Assert
        assertEquals("Пользователь user1 зарегистрирован.", text);
        assertNotEquals(UserMessageProcessor.UNAUTHORISED, text1);
        assertTrue(userData.getData().containsKey("user1"));
    }

    @Test
    void trackTest() {
        // Arrange
        List<Update> updates = List.of(
            mockUpdate("user", "/start"),
            mockUpdate("user", "/track link1"),
            mockUpdate("user", "/track link2"),
            mockUpdate("user", "/track link3"),
            mockUpdate("user", "/untrack link2")
        );
        Set<String> expected = Set.of("link1", "link3");

        // Act
        updates.forEach(update -> userMessageProcessor.process(update));

        // Assert
        assertTrue(userData.getData().containsKey("user"));
        assertEquals(expected, userData.getData().get("user"));
    }

    @Test
    void listTest() {
        // Arrange
        List<Update> updates = List.of(
            mockUpdate("user", "/start"),
            mockUpdate("user", "/track link1"),
            mockUpdate("user", "/track link2"),
            mockUpdate("user", "/track link3")
        );
        Update list = mockUpdate("user", "/list");

        // Act
        updates.forEach(update -> userMessageProcessor.process(update));
        SendMessage sendMessage = userMessageProcessor.process(list);
        String text = (String) sendMessage.getParameters().get("text");

        // Assert
        assertTrue(text.matches("^Ресурсы: (?:[^, ]+(?:, )?)*$"));
    }

    private Update mockUpdate(String username, String text) {
        Update update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        User user = Mockito.mock(User.class);

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.from()).thenReturn(user);
        Mockito.when(message.text()).thenReturn(text);

        Mockito.when(chat.id()).thenReturn(1L);
        Mockito.when(user.username()).thenReturn(username);
        return update;
    }
}
