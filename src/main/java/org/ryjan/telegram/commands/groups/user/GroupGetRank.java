package org.ryjan.telegram.commands.groups.user;

import java.io.File;
import org.ryjan.telegram.commands.groups.BaseCommand;
import org.ryjan.telegram.commands.groups.config.GroupPermissions;
import org.ryjan.telegram.config.BotConfig;
import org.ryjan.telegram.handler.CommandsHandler;
import org.ryjan.telegram.interfaces.Permissions;
import org.ryjan.telegram.main.BotMain;
import org.ryjan.telegram.model.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
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

@Component
public class GroupGetRank extends BaseCommand {
    @Value("${bot.token}")
    private String token;
    private String fileId;

    protected GroupGetRank() {
        super("/rank", "💖Получить карточку профиля", GroupPermissions.ANY);
    }

    @Override
    protected void executeCommand(String chatId, BotMain bot, CommandsHandler handler) throws Exception {
        Message message = getUpdate().getMessage();
        User user = userService.findUser(message.getFrom().getId());
        File card = generateLevelCard(user.getUsername(), user.getLevel(), user.getXp(), 123123);

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setPhoto(new InputFile(card));
        bot.execute(photo);
    }

    private File generateLevelCard(String username, int level, double xp, double xpForNextLevel) throws Exception {
        int width = 800, height = 200;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.drawString(username, 50, 50);

        int progressBarWidth = 600;
        int progressBarHeight = 30;
        double progress = xp / xpForNextLevel;

        g.setColor(Color.CYAN);
        g.fillRect(50, 120, (int) (progress * progressBarWidth), progressBarHeight);

        g.setColor(Color.WHITE);
        g.drawString(String.format("%.0f / %.0f XP", xp, xpForNextLevel), 50, 170);

        g.dispose();

        File file = new File("level_card.png");
        ImageIO.write(image, "png", file);
        return file;
    }

    private void fetchUserAvatar(Long userId, BotMain bot) {
        try {
            GetUserProfilePhotos photoRequest = new GetUserProfilePhotos();
            photoRequest.setUserId(userId);
            photoRequest.setLimit(1);

            UserProfilePhotos photos = bot.execute(photoRequest);

            if (photos.getTotalCount() > 0) {
                fileId = photos.getPhotos().getFirst().getFirst().getFileId();
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void downloadAvatar(String fieldId, BotMain bot) {
        try {
            GetFile getFileRequest = new GetFile();
            getFileRequest.setFileId(fieldId);

            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFileRequest);
            String filePath = file.getFilePath();

            //String url = String.format("https://api.telegram.org/file/bot%s/", token) + filePath;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
