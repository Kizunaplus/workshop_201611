import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.bot.loader.XmlBotSetting;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class Bot3 {
	ChatNotify notiry;

	UUID id;

	private List<UUID> preMessageIDList;

	/**
	 * ロボット設定
	 */
	XmlBotSetting setting;

	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.notiry = argNotify;
		this.id = UUID.randomUUID();
		this.preMessageIDList = new ArrayList<UUID>();
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
		if (preMessageIDList.contains(messageId)) {
			return;
		}
		preMessageIDList.add(messageId);

		// この２行で解析できる
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(message);

		// 結果を出力してみる
		String name = "そんなもの知らない。";
		for (Token token : tokens) {
			System.out.println(token.getSurfaceForm() + ":"
					+ token.getPartOfSpeech());

			if (token.getSurfaceForm().contains("おはようございます")) {
				name = String.format("%s", token.getSurfaceForm());
				break;
			}
			if (token.getSurfaceForm().contains("こんにちは")) {
				name = String.format("やあ。%s", token.getSurfaceForm());
				break;
			}
			if (token.getSurfaceForm().contains("こんばんは")) {
				name = String.format("%s。月が綺麗ですね", token.getSurfaceForm());
				break;
			}

			if (token.getPartOfSpeech().contains("国")) {
				name = String.format("%sは行ってみたいね", token.getSurfaceForm());
				break;
			}
			if (token.getPartOfSpeech().contains("地域")) {
				name = String.format("%s？良い所だね", token.getSurfaceForm());
				break;
			}

			if (token.getPartOfSpeech().contains("名詞")) {
				name = String.format("%sっていいよね", token.getSurfaceForm());
				break;
			}
			if (token.getPartOfSpeech().contains("感動詞")) {
				name = String.format("%s・・・感動した・・・", token.getSurfaceForm());
				break;
			}
			if (token.getPartOfSpeech().contains("動詞")) {
				name = String.format("%s、動くっていいよね", token.getSurfaceForm());
				break;
			}
			if (token.getPartOfSpeech().contains("形容詞")) {
				name = String.format("%s、素晴らしいね", token.getSurfaceForm());
				break;
			}
		}

		this.notiry.notify("ロボット3", this.id, name, messageId);
	}
}