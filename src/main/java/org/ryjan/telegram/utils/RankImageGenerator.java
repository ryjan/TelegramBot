package org.ryjan.telegram.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class RankImageGenerator {
    public static byte[] generateRankImage(String username, int level, double xp, double xpForNextLevel, BufferedImage userAvatar) throws Exception {
        int width = 400;
        int height = 200;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height);

        int avatarSize = 80;
        int avatarX = 30;
        int avatarY = (height - avatarSize) / 2;
        g.setColor(Color.GRAY);
        g.fill(new Ellipse2D.Double(avatarX, avatarY, avatarSize, avatarSize));

        if (userAvatar != null) {
            BufferedImage croppedAvatar = cropToCircle(userAvatar, avatarSize, avatarSize);
            g.drawImage(croppedAvatar, avatarX, avatarY, null);
        } else {
            InputStream defaultImage = RankImageGenerator.class.getResourceAsStream("/defaultImage.jpg");
            BufferedImage croppedAvatar = cropToCircle(ImageIO.read(defaultImage), avatarSize, avatarSize);
            g.drawImage(croppedAvatar, avatarX, avatarY, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.drawString(username, 50, 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        g.drawString(String.valueOf(level), 50, 90);

        int progressBarX = 130;
        int progressBarY = (height - 30) / 2;
        int progressBarWidth = 220;
        int progressBarHeight = 30;
        g.setColor(Color.GRAY);
        g.fillRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight);
        g.setColor(Color.CYAN);
        g.fillRect(progressBarX, progressBarY, (int)(progressBarWidth * (xp / xpForNextLevel)), progressBarHeight);

        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(g.getFont().deriveFont(24f));
        FontMetrics fontMetrics = g.getFontMetrics();
        int textX = progressBarX + progressBarWidth / 2;
        int textY = (height + fontMetrics.getHeight()) / 2 - fontMetrics.getDescent();
        //g.drawString(username, textX, textY);
        g.drawString("Уровень: " + level, textX, textY + fontMetrics.getHeight());
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    private static BufferedImage cropToCircle(BufferedImage source, int width, int height) {
        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = output.createGraphics();
        g.setClip(new Ellipse2D.Double(0, 0, width, height));
        g.drawImage(source, 0, 0, width, height, null);
        g.dispose();
        return output;
    }
}
