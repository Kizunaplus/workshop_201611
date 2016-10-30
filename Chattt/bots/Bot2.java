import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class Bot2 {
	ChatNotify notiry;

	UUID id;

	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.notiry = argNotify;
		this.id = UUID.randomUUID();
	}

	@BotAnnotation(value = "getUUID")
	public UUID getUUID() {
		return this.id;
	}

	@BotAnnotation(value = "notify")
	public void notify(UUID id, String message, Date time) {
		if (this.id.equals(id)) {
			return;
		}

		this.notiry.notify("ロボット2", this.id, "自動返信 : " + message);
	}

	@BotAnnotation(value = "getImage")
	/**
	 * BOTのイメージを取得
	 * 
	 * @return BOTイメージ
	 */
	public BufferedImage getImage() {
		String currentPath = System.getProperty("user.dir");

		BufferedImage image = null;
		// 背景イメージの読み込み
		String filePath = currentPath + File.separator + "resources"
				+ File.separator + "charactor02.png";
		if (new File(filePath).exists()) {
			try {
				image = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return image;
	}

}
