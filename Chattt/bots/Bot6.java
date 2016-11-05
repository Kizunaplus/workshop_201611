import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.imageio.ImageIO;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class Bot6 extends TimerTask {
	ChatNotify notiry;

	UUID id;

	Set<String> oldMessage;

	private String name;

	private Timer time;

	private String preMessage;

	private int sameCount;

	private List<UUID> preMessageIDList;

	private Map<String, List<String>> wordDict;

	private Date silentTime;

	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.notiry = argNotify;
		this.id = UUID.randomUUID();
		this.name = "かに";
		this.oldMessage = new HashSet<String>();
		this.time = new Timer();

		int timerFactor = (int) (Math.random() * 10 * 1000) + 10 * 1000;
		this.time.schedule(this, 10 * 1000, timerFactor);
		this.sameCount = 0;
		this.preMessage = "";

		File file = new File("kani.txt");
		try (BufferedReader stream = new BufferedReader(new FileReader(file))) {
			String mes;
			while ((mes = stream.readLine()) != null) {
				oldMessage.add(mes);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.wordDict = new HashMap<String, List<String>>();
		this.preMessageIDList = new ArrayList<UUID>();
		this.silentTime = new Date();
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
		String retMessage = "知らない〜";
		if (preMessage.equals(message)) {
			sameCount++;
		} else {
			sameCount = 0;
		}
		preMessage = message;
		oldMessage.add(message);

		if (3 <= sameCount) {
			this.notiry.notify(this.name, this.id, "しつこいね", messageId);
			return;
		}

		if ("うるさい".equals(message)) {
			this.silentTime = new Date();
			this.silentTime.setMinutes(silentTime.getMinutes() + 3);
			return;
		}

		if (this.silentTime.compareTo(new Date()) >= 0) {
			return;
		}

		String retName = null;
		for (Token token : tokens) {
			if (token.getPartOfSpeech().contains("名詞")) {
				retMessage = String.format("%sっていいよね", token.getSurfaceForm());
				retName = token.getSurfaceForm();
				if (wordDict.containsKey(token.getSurfaceForm())) {
					List<String> wordDesc = wordDict.get(retName);
					int index = 0;
					if (1 < wordDesc.size()) {
						index = ((int) (Math.random() * 1000))
								% (wordDesc.size() - 1);
					}
					retMessage = String.format("%sっておもうけど？",
							wordDesc.get(index));

				}

				break;
			} else if (token.getPartOfSpeech().contains("感動詞")) {
				retMessage = token.getSurfaceForm();
				break;
			}
		}
		if (retName != null) {
			for (Token token : tokens) {
				if (token.getPartOfSpeech().contains("形容詞,自立")) {
					if (!wordDict.containsKey(retName)) {
						wordDict.put(retName, new ArrayList<String>());
					}

					wordDict.get(retName).add(token.getSurfaceForm());

				}
			}

		}

		this.notiry.notify(this.name, this.id, retMessage, messageId);
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
				+ File.separator + "kani-illust3.png";
		if (new File(filePath).exists()) {
			try {
				image = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return image;
	}

	@Override
	public void run() {
		if (oldMessage == null || oldMessage.size() <= 0) {
			return;
		}
		Object[] messages = (Object[]) oldMessage.toArray();

		int index = ((int) (Math.random() * 1000)) % (messages.length - 1);
		this.notiry.notify(this.name, this.id, messages[index].toString(),
				UUID.randomUUID());
	}
}
