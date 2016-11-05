import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class Bot5 extends XmlBot {

	private List<UUID> preMessageIDList;

	/**
	 * 初期化処理
	 * 
	 * @param argNotify
	 */
	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.preMessageIDList = new ArrayList<UUID>();
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
		if (preMessageIDList.contains(messageId)) {
			return;
		}
		preMessageIDList.add(messageId);

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
