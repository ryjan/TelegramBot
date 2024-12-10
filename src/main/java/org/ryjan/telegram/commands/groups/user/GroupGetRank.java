package org.ryjan.telegram.commands.groups.user;

import java.awt.geom.Ellipse2D;
import java.io.*;

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
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

@Component
public class GroupGetRank extends BaseCommand {

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
        Long userId = message.getFrom().getId();
        User user = userService.findUser(userId);

        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/defaultImage.jpg");
        byte[] image = RankImageGenerator.generateRankImage(user.getUsername(), user.getLevel(), user.getXp(),
                xpService.xpForNextLevel(user.getLevel()), getUserAvatar(userId));
        InputStream inputStream = new ByteArrayInputStream(image);

        SendPhoto photo = new SendPhoto();
        photo.setChatId(getUpdate().getMessage().getChatId());
        photo.setPhoto(new InputFile(inputStream, "rank_image.png"));
        photo.setReplyToMessageId(message.getMessageId());
        botMain.execute(photo);
    }

    private BufferedImage getUserAvatar(Long userId) throws Exception {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(Objects.requireNonNull(getUserAvatarFieldId(userId)));

        org.telegram.telegrambots.meta.api.objects.File file = botMain.execute(getFileRequest);
        String filePath = file.getFilePath();

        String url = String.format("https://api.telegram.org/file/bot%s/", botMain.getBotToken()) + filePath;

        InputStream inputStream = getUrlConnection(url).getInputStream();
        return ImageIO.read(inputStream);
    }

    private String getUserAvatarFieldId(Long userId) {
        try {
            GetUserProfilePhotos getUserProfilePhotos = new GetUserProfilePhotos();
            getUserProfilePhotos.setUserId(userId);
            getUserProfilePhotos.setLimit(1);

            UserProfilePhotos photos = botMain.execute(getUserProfilePhotos);

            if (photos != null && !photos.getPhotos().isEmpty()) {
                return photos.getPhotos().getFirst().getFirst().getFileId();
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private URLConnection getUrlConnection(String url) throws IOException {
        URI uri = URI.create(url);
        URLConnection connection = uri.toURL().openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }
}
