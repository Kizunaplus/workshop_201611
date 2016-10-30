package jp.co.kizuna.plus.chat.main.window;//package jp.co.kizuna.plus.chat.main.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import jp.co.kizuna.plus.chat.bot.loader.BotClassLoader;
import jp.co.kizuna.plus.chat.bot.loader.Bots;

public class UserPanel extends JComponent implements ChatNotify {

	/**
	 * 背景イメージパス
	 */
	private final static String BACKGROUND_IMG = "resources" + File.separator
			+ "background.jpg";

	/**
	 * キャラクターデフォルトイメージパス
	 */
	private final static String DEFAULT_CHARCACTOR_IMG = "resources"
			+ File.separator + "default_charactor.png";

	/**
	 * デフォルトメッセージエリアの高さ
	 */
	private final static int DEFAULT_MESSAGE_AREA_HEIGHT = 100;

	/**
	 * デフォルト角丸資格の角度
	 */
	private final static int DEFAULT_CIRCUL_VALUE = 50;

	/**
	 * 背景イメージ
	 */
	private BufferedImage backgoundImage;

	/**
	 * キャラクタデフォルトイメージ
	 */
	private BufferedImage defaultCharactorImage;

	/**
	 * ズーム因子
	 */
	private double zoomXFactor = 1;

	/**
	 * ズーム因子
	 */
	private double zoomYFactor = 1;

	/**
	 * メッセージ
	 */
	private List<String> messages;

	/**
	 * メッセージポジション
	 */
	private int messageStartPosition;

	/**
	 * 会話bot
	 */
	private Bots bots;

	/**
	 * パネルの初期化
	 */
	public void init() {
		messages = new ArrayList<>();
		bots = new Bots();
		try {
			bots.append("Bot1", this);
			bots.append("Bot2", this);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		try {
			String currentPath = System.getProperty("user.dir");

			// 背景イメージの読み込み
			String filePath = currentPath + File.separator + BACKGROUND_IMG;
			if (new File(filePath).exists()) {
				backgoundImage = ImageIO.read(new File(filePath));
			}

			// キャラクターイメージの読み込み
			filePath = currentPath + File.separator + DEFAULT_CHARCACTOR_IMG;
			if (new File(filePath).exists()) {
				defaultCharactorImage = ImageIO.read(new File(filePath));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int mouseWheel = arg0.getWheelRotation();
				messageStartPosition = Math.max(messageStartPosition
						+ mouseWheel, 0);
				repaint();
			}
		});

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D graphic = (Graphics2D) g;
		if (backgoundImage != null) {
			// 背景イメージの描画
			graphic.drawImage(backgoundImage, 0, 0,
					(int) (zoomXFactor * backgoundImage.getWidth()),
					(int) (zoomYFactor * backgoundImage.getHeight()), 0, 0,
					backgoundImage.getWidth(), backgoundImage.getHeight(), this);
		}

		int padding = 10;
		// メッセージ枠の描画
		graphic.setColor(Color.WHITE);
		graphic.setStroke(new BasicStroke(5.0f));
		graphic.drawRoundRect(padding, this.getHeight()
				- DEFAULT_MESSAGE_AREA_HEIGHT, this.getWidth() - padding * 2,
				DEFAULT_MESSAGE_AREA_HEIGHT - padding, DEFAULT_CIRCUL_VALUE,
				DEFAULT_CIRCUL_VALUE);
		graphic.setColor(new Color(255, 255, 255, 80));
		graphic.fillRoundRect(padding, this.getHeight()
				- DEFAULT_MESSAGE_AREA_HEIGHT, this.getWidth() - padding * 2,
				DEFAULT_MESSAGE_AREA_HEIGHT - padding, DEFAULT_CIRCUL_VALUE,
				DEFAULT_CIRCUL_VALUE);

		// メッセージの描画
		graphic.setColor(Color.BLACK);
		Font font = new Font(this.getFont().getFontName(), Font.PLAIN, 32);
		graphic.setFont(font);
		int messagePosition = DEFAULT_MESSAGE_AREA_HEIGHT;
		int messageStart = Math.max(
				Math.min(messageStartPosition, messages.size() - 2), 0);
		for (int messageIndex = 0; messageIndex < 2; messageIndex++) {
			if (messages.size() <= messageStart + messageIndex) {
				continue;
			}

			String message = messages.get(messageStart + messageIndex);
			messagePosition -= font.getSize();
			graphic.drawString(message, 20, this.getHeight() - messagePosition);
		}
		graphic.setFont(this.getFont());

		int botIndex = 0;
		int botCount = bots.getList().size();
		for (BotClassLoader bot : bots.getList()) {
			BufferedImage botImage = bot.getImage();
			if (botImage == null && defaultCharactorImage != null) {
				botImage = defaultCharactorImage;
			}
			// キャラクターイメージの描画
			int x = this.getWidth() / botCount * botIndex;
			graphic.drawImage(botImage, x, DEFAULT_MESSAGE_AREA_HEIGHT + 20,
					(int) (zoomXFactor * 350 + x), (int) (zoomYFactor * 350)
							+ DEFAULT_MESSAGE_AREA_HEIGHT + 20, 0, 0,
					botImage.getWidth(), botImage.getHeight(), this);
			botIndex++;
		}

		super.paintComponent(g);
	}

	/**
	 * イメージのズーム値(X)を設定
	 * 
	 * @param argZoom
	 */
	public void setXZoom(double argZoom) {
		if (0 < argZoom) {
			this.zoomXFactor = argZoom;
		}
	}

	/**
	 * イメージのズーム値(Y)を設定
	 * 
	 * @param argZoom
	 */
	public void setYZoom(double argZoom) {
		if (0 < argZoom) {
			this.zoomYFactor = argZoom;
		}
	}

	/**
	 * ユーザ入力の通知
	 * 
	 * @param message
	 *            メッセージ
	 */
	public void notify(String message) {
		UUID id = new UUID(0, 0);
		notify("ユーザ", id, message);
	}

	/**
	 * ユーザ入力の通知
	 * 
	 * @param message
	 *            メッセージ
	 */
	public void notify(String name, UUID id, String message) {
		Date sendTime = new Date();
		messageStartPosition = messages.size() - 1;
		messages.add(String.format("%s : %s", name, message));
		for (BotClassLoader bot : bots.getList()) {
			if (bot.getUUID() == id) {
				// 自身のメッセージは通知しない
				continue;
			}

			bot.notify(id, message, sendTime);
		}
		this.repaint();
	}
}
