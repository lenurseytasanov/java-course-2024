package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.dto.Link;
import edu.java.dto.ListLinksResponse;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandsTest {

    private static ScrapperClient scrapperClient;

    @BeforeAll
    public static void setUp() {
        scrapperClient = Mockito.mock(ScrapperClient.class);
        Mono<Long> mono1 = Mockito.mock(Mono.class);
        Mockito.when(mono1.block()).thenReturn(1L);

        Mockito.when(scrapperClient.addChat(Mockito.eq(1L))).thenReturn(mono1);
        Mockito.when(scrapperClient.addChat(Mockito.eq(2L))).thenThrow(new IllegalArgumentException());

        Mono<String> mono2 = Mockito.mock(Mono.class);
        Mockito.when(mono2.block()).thenReturn("https://stackoverflow.com/questions/1/question1");

        Mockito.when(scrapperClient.addLink(
            Mockito.eq(1L),
            Mockito.eq("https://stackoverflow.com/questions/1/question1"))
        ).thenReturn(mono2);
        Mockito.when(scrapperClient.addLink(
            Mockito.eq(1L),
            Mockito.eq("https://stackoverflow.com/questions/2/question2")
        )).thenThrow(new IllegalArgumentException());

        ListLinksResponse linksResponse = Mockito.mock(ListLinksResponse.class);
        Mockito.when(linksResponse.links()).thenReturn(new Link[]{
            new Link(1L, "https://stackoverflow.com/questions/1/question1", null, null),
            new Link(1L, "https://stackoverflow.com/questions/2/question2", null, null),
        });

        Mono<ListLinksResponse> mono3 = Mockito.mock(Mono.class);
        Mockito.when(mono3.block()).thenReturn(linksResponse);
        Mockito.when(scrapperClient.getLinks(Mockito.eq(1L))).thenReturn(mono3);
    }

    @Test
    void startTest() {
        // Arrange
        Update update1 = mockUpdate(1L, "/start");
        Update update2 = mockUpdate(2L, "/start");

        Command command = new StartCommand(scrapperClient);

        String expected1 = "Чат 1 зарегистрирован";
        String expected2 = "Чат уже существует";

        // Act
        SendMessage result1 = command.handle(update1);
        SendMessage result2 = command.handle(update2);
        String actual1 = (String) result1.getParameters().get("text");
        String actual2 = (String) result2.getParameters().get("text");

        // Assert
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void listTest() {
        // Arrange
        Update update = mockUpdate(1L, "/list");

        Command command = new ListCommand(scrapperClient);

        String expected = "Отслеживаются: https://stackoverflow.com/questions/1/question1, " +
            "https://stackoverflow.com/questions/2/question2";

        // Act
        SendMessage result = command.handle(update);
        String actual = (String) result.getParameters().get("text");

        // Arrange
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideTrackUpdates")
    void trackTest(Update update, String expected) {
        // Arrange
        Command command = new TrackCommand(scrapperClient);

        // Act
        SendMessage result = command.handle(update);
        String actual = (String) result.getParameters().get("text");

        // Assert
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTrackUpdates() {
        return Stream.of(
            Arguments.of(
                mockUpdate(1L, "/track https://stackoverflow.com/questions/1/question1"),
                "Ресурс https://stackoverflow.com/questions/1/question1 добавлен"
            ),
            Arguments.of(
                mockUpdate(1L, "/track https://stackoverflow.com/questions/2/question2"),
                "Ресурс уже отслеживается"
            ),
            Arguments.of(
                mockUpdate(1L, "/track"),
                "Введите ссылку"
            ),
            Arguments.of(
                mockUpdate(1L, "/track https://somehost.com/path/to"),
                "Неизвестный ресурс"
            )
        );
    }

    private static Update mockUpdate(Long chatId, String text) {
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
