package jp.co.kizuna.plus.chat.bot.loader;

import java.io.Serializable;

public class KeyValuePair implements Serializable {

	/**
	 * キー
	 */
	private String key;

	/**
	 * 値
	 */
	private String value;

	/**
	 * コンストラクタ
	 */
	public KeyValuePair() {
	}

	/**
	 * コンストラクタ
	 * 
	 * @param key
	 *            キー
	 * @param value
	 *            値
	 */
	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * キーの取得
	 * 
	 * @return キー
	 */
	public String getKey() {
		return key;
	}

	/**
	 * キーの設定
	 * 
	 * @param key
	 *            キー
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 値の取得
	 * 
	 * @return 値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 値の設定
	 * 
	 * @param value
	 *            値
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
