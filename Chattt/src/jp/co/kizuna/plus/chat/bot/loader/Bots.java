package jp.co.kizuna.plus.chat.bot.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class Bots {

	private Map<String, BotClassLoader> botMap;

	/**
	 * コンストラクタ
	 */
	public Bots() {
		this.botMap = new HashMap<>();
	}

	/**
	 * BOTの追加
	 */
	public void append(String className, ChatNotify notify) throws Exception {
		if (!botMap.containsKey(className)) {
			botMap.put(className, new BotClassLoader(className, notify));
		}
	}

	/**
	 * BOTの削除
	 */
	public void remove(String className) {
		if (botMap.containsKey(className)) {
			botMap.remove(className);
		}
	}

	/**
	 * BOT一覧を取得
	 * 
	 * @return bot一覧
	 */
	public Collection<BotClassLoader> getList() {
		return botMap.values();
	}

}
