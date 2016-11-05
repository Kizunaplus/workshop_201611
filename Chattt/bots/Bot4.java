import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import jp.co.kizuna.plus.chat.bot.loader.BotAnnotation;
import jp.co.kizuna.plus.chat.main.window.ChatNotify;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class Bot4 {
	ChatNotify notiry;

	UUID id;

	private ArrayList<String> messageLog;

	private int favorability;

	private List<String> favorites;

	private List<String> dislikes;

	private List<UUID> preMessageIDList;

	@BotAnnotation(value = "init")
	public void init(ChatNotify argNotify) {
		this.notiry = argNotify;
		this.id = UUID.randomUUID();
		this.messageLog = new ArrayList<>();
		this.favorability = 50;
		this.favorites = Arrays.asList("イチゴ", "メロン", "苺", "いちご", "めろん");
		this.dislikes = Arrays.asList("ミカン", "リンゴ", "みかん", "蜜柑", "りんご", "林檎");
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

		if (!(this.favorability > 0)) {
			this.notiry.notify("ロボット4", this.id, "もう話したくないよ", messageId);
			System.out.println(this.favorability);
			return;
		}

		this.messageLog.add(0, message);
		if (this.continues(this.messageLog, 3)) {
			this.notiry.notify("ロボット4", this.id, "しつこい", messageId);
			this.favorability -= 20;
			System.out.println(this.favorability);
			return;
		}

		// この２行で解析できる
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(message);

		// 結果を出力してみる
		String name = "そんなもの知らない。";
		for (Token token : tokens) {
			System.out.println(token.getSurfaceForm() + "\t"
					+ token.getAllFeatures());
			if (token.getAllFeatures().contains("名詞")) {
				if (this.favorites.contains(token.getSurfaceForm())) {
					name = String.format("%sっていいよね", token.getSurfaceForm());
					this.favorability += 10;
					System.out.println(this.favorability);
					break;
				} else if (this.dislikes.contains(token.getSurfaceForm())) {
					name = String.format("%sは嫌いだよ", token.getSurfaceForm());
					this.favorability -= 10;
					System.out.println(this.favorability);
					break;
				}
			}
		}
		this.notiry.notify("ロボット4", this.id, name, messageId);
	}

	/**
	 * メッセージが指定回数連続しているか
	 * 
	 * @param messageLog
	 *            メッセージログ
	 * @param max
	 *            回数
	 * @return
	 */
	private boolean continues(ArrayList<String> messageLog, int max) {
		int count = 0;
		String prevMessage = "";
		for (String message : messageLog) {
			if (prevMessage.equals(message)) {
				count++;
				if (count >= max - 1) {
					return true;
				}
			} else {
				count = 0;
			}
			prevMessage = message;
		}
		return false;
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
				+ File.separator + "kyodai_robot.png";
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