import java.util.Date;
import java.util.List;
import java.util.UUID;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

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
	public void notify(UUID id, String message, Date time, UUID messageId) {
		if (this.id.equals(id)) {
			return;
		}

		// この２行で解析できる
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(message);

		// 結果を出力してみる
		String name = "そんなもの知らない。";
		for (Token token : tokens) {
			if (token.getPartOfSpeech().contains("名詞")) {
				name = String.format("%sっていいよね", token.getSurfaceForm());
				break;
			}
		}

		this.notiry.notify("ロボット1", this.id, name, messageId);
	}
}