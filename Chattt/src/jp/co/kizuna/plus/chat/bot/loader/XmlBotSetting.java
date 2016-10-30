package jp.co.kizuna.plus.chat.bot.loader;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XmlBotSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 350912026349102532L;

	/**
	 * Bot Id
	 */
	private String id;

	/**
	 * Bot名
	 */
	private String name;

	/**
	 * 会話用 正規表現
	 */
	private List<KeyValuePair> messageRegex;

	/**
	 * イメージパス
	 */
	private String image;

	/**
	 * コンストラクタ
	 */
	public XmlBotSetting() {
		messageRegex = new ArrayList<KeyValuePair>();
	}

	/**
	 * Bot Idの取得
	 * 
	 * @return Bot Id
	 */
	public UUID getId() {
		return UUID.fromString(id);
	}

	/**
	 * Bot Idの取得
	 * 
	 * @return Bot Id
	 */
	public String getIdValue() {
		return id;
	}

	/**
	 * Bot Idの設定
	 * 
	 * @param id
	 *            Bot Id
	 */
	public void setIdValue(String id) {
		this.id = id;
	}

	/**
	 * Bot名の取得
	 * 
	 * @return Bot名
	 */
	public String getName() {
		return name;
	}

	/**
	 * Bot名の設定
	 * 
	 * @param name
	 *            Bot名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * メッセージ用正規表現の取得
	 * 
	 * @return メッセージ用正規表現
	 */
	public List<KeyValuePair> getMessageRegex() {
		return messageRegex;
	}

	/**
	 * メッセージ用正規表現の設定
	 * 
	 * @param messageRegex
	 *            メッセージ用正規表現
	 */
	public void setMessageRegex(List<KeyValuePair> messageRegex) {
		this.messageRegex = messageRegex;
	}

	/**
	 * Botイメージの取得
	 * 
	 * @return Botイメージ
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Botイメージの設定
	 * 
	 * @param image
	 *            Botイメージ
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * エントリーポイント
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		XmlBotSetting setting = new XmlBotSetting();
		setting.setIdValue(UUID.randomUUID().toString());
		setting.setName("名前");
		setting.setImage("イメージファイル名");
		setting.getMessageRegex().add(new KeyValuePair("こんにちは", "こんにちは"));
		setting.getMessageRegex().add(new KeyValuePair("おはよう", "おはよう"));
		setting.getMessageRegex().add(new KeyValuePair("こんばんは", "こんばんは"));

		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream("BotSetting.xml")));
			encoder.writeObject(setting);
			encoder.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
