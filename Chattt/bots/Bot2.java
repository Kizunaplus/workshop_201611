import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.UUID;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class Bot2 extends XmlBot {

	/**
	 * 初期化処理
	 * 
	 * @param argNotify
	 */
	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		super.init(argNotify);
	}

	/**
	 * ロボットIDの取得
	 * 
	 * @return
	 */
	@BotAnnotation(value = "getUUID")
	public UUID getUUID() {
		return super.getUUID();
	}

	@BotAnnotation(value = "notify")
	public void notify(UUID id, String message, Date time, UUID messageId) {
		super.notify(id, message, time, messageId);
	}

	@BotAnnotation(value = "getImage")
	/**
	 * BOTのイメージを取得
	 * 
	 * @return BOTイメージ
	 */
	public BufferedImage getImage() {
		return super.getImage();
	}

}
