package tests;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidationTest {

    private final UserController userController = new UserController();
    @Rule
    public final LoggerRule loggerRule = new LoggerRule();

    public User userAccount() {
        User user = new User();
        user.setName("userNameTest");
        user.setEmail("emailTest@gmail.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.parse("1990-05-06"));
        return user;
    }

    @Test
    public void createUserTest() {
        User user = userAccount();
        String expectingLoggerMessage = "User Added: " + user.getName();

        userController.create(user);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }

    @Test
    public void wrongUserNameTest() {
        User user = userAccount();
        user.setName("");

        userController.create(user);

        Assert.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void wrongUserEmailTest() {
        User user1 = userAccount();
        user1.setEmail("wrongEmail");
        User user2 = userAccount();
        user2.setEmail("");
        String expectingLoggerMessage = "Incorrect user email";

        userController.create(user1);
        userController.create(user2);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(1));
    }

    @Test
    public void wrongUserLoginTest() {
        User user1 = userAccount();
        user1.setLogin("wrong login");
        User user2 = userAccount();
        user2.setLogin("");
        String expectingLoggerMessage = "Incorrect user login";

        userController.create(user1);
        userController.create(user2);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(1));
    }

    @Test
    public void wrongUserBirthdayTest() {
        User user = userAccount();
        user.setBirthday(LocalDate.now().plusDays(1));
        String expectingLoggerMessage = "Incorrect user birthday";

        userController.create(user);

        Assert.assertEquals(expectingLoggerMessage, loggerRule.getFormattedMessages().get(0));
    }
}


