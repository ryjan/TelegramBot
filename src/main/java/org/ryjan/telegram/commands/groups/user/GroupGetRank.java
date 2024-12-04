package org.ryjan.telegram.commands.groups.user;

import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.commands.groups.level.XpService;
import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.utils.RankImageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.InputStream;

@Component
public class GroupGetRank extends BaseCommand {
    @Value("${bot.token}")
    private String token;
    private String fileId;

    @Autowired
    private BotMain botMain;

    @Autowired
    private XpService xpService;

    protected GroupGetRank() {
        super("/rank", "💖Получить карточку профиля", GroupPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) throws Exception {
        Message message = getUpdate().getMessage();
        User user = userService.findUser(message.getFrom().getId());

        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/defaultImage.jpg");
        byte[] image = RankImageGenerator.generateRankImage(user.getUsername(), user.getLevel(), user.getXp(),
                xpService.xpForNextLevel(user.getLevel()), null);
        InputStream inputStream = new ByteArrayInputStream(image);

        SendPhoto photo = new SendPhoto();
        photo.setChatId(getUpdate().getMessage().getChatId());
        photo.setPhoto(new InputFile(inputStream, "rank_image.png"));
        photo.setReplyToMessageId(message.getMessageId());
        botMain.execute(photo);
    }
}
