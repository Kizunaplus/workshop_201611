import java.util.Date;
import java.util.UUID;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class Bot1 {
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

		this.notiry.notify("ロボット1", this.id, "自動返信 : " + message);
	}

}