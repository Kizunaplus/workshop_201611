import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.bot.loader.KeyValuePair;
import jp.co.kizuna.plus.chat.bot.loader.XmlBotSetting;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class XmlBot {
	/**
	 * 通知領域
	 */
	ChatNotify notiry;

	/**
	 * ロボット設定
	 */
	XmlBotSetting setting;

	/**
	 * 前回のメッセージ
	 */
	UUID prevMessage;

	/**
	 * 初期化処理
	 * 
	 * @param argNotify
	 */
	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.notiry = argNotify;
		String fileName = String.format("%s.xml", this.getClass()
				.getSimpleName());

		try {
			XMLDecoder dec = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(fileName)));
			setting = (XmlBotSetting) dec.readObject();
			dec.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * ロボットIDの取得
	 * 
	 * @return
	 */
	@BotAnnotation(value = "getUUID")
	public UUID getUUID() {
		return setting.getId();
	}

	@BotAnnotation(value = "notify")
	public void notify(UUID id, String message, Date time, UUID messageId) {
		if (messageId == prevMessage) {
			// すでに返答した話題
			return;
		}

		for (KeyValuePair patternValue : setting.getMessageRegex()) {
			// 判定するパターンを生成
			Pattern p = Pattern.compile(patternValue.getKey());
			Matcher m = p.matcher(message);

			// 画面表示
			if (m.find()) {
				this.notiry.notify(setting.getName(), setting.getId(),
						patternValue.getValue(), messageId);
				prevMessage = messageId;
			}
		}

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
				+ File.separator + setting.getImage();
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
