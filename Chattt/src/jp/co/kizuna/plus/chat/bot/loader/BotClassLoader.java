package jp.co.kizuna.plus.chat.bot.loader;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import jp.co.kizuna.plus.chat.main.window.ChatNotify;

public class BotClassLoader {

	/**
	 * Botクラス
	 */
	private Class botClass;

	/**
	 * インスタンス
	 */
	private Object instance;
	
	/**
	 * ID
	 */
	private UUID botId;

	/**
	 * コンストラクタ
	 * 
	 * @param className
	 *            クラス名
	 */
	public BotClassLoader(String className, ChatNotify notify) throws Exception {
		loadClass(className, notify);
		
		this.botId = UUID.randomUUID();
	}

	/**
	 * クラスの読み込み
	 * 
	 * @param className
	 *            クラス名
	 */
	private void loadClass(String className, ChatNotify notify)
			throws Exception {
		if ("".equals(className)) {
			return;
		}

		java.net.URL[] url = new java.net.URL[1];
		java.io.File file;
		// ディレクトリは最後にスラッシュが必要
		file = new java.io.File("bots/");
		url[0] = file.toURI().toURL();

		ClassLoader parent = ClassLoader.getSystemClassLoader();
		java.net.URLClassLoader loader = new java.net.URLClassLoader(url,
				parent);

		botClass = loader.loadClass(className);
		instance = botClass.newInstance();

		Method method = getMethod("init", new Class[] { ChatNotify.class });
		if (method != null) {
			method.setAccessible(true);
			try {
				method.invoke(instance, new Object[] { notify });
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 対象となるアノテーションのメソッドを取得
	 * 
	 * @param annotationClass
	 * @param parameters
	 * @return
	 */
	private Method getMethod(String value, Class[] parameters) {
		if (botClass == null) {
			return null;
		}

		Method returnMethod = null;
		for (Method method : botClass.getDeclaredMethods()) {
			BotAnnotation annotation = method
					.getAnnotation(BotAnnotation.class);
			if (annotation == null) {
				// 対象のアノテーションがない場合
				continue;
			}
			if (!value.equals(annotation.value())) {
				// キーワードが一致しない場合
				continue;
			}

			Class[] actualParameters = method.getParameterTypes();
			if (actualParameters.length != parameters.length) {
				// パラメータが異なる場合
				continue;
			}
			boolean isSame = true;
			for (int aryIndex = 0; aryIndex < actualParameters.length; aryIndex++) {
				if (actualParameters[aryIndex] != parameters[aryIndex]) {
					isSame = false;
					break;
				}
			}
			if (isSame == false) {
				// パラメータが異なる場合
				continue;
			}

			returnMethod = method;
			break;
		}

		return returnMethod;
	}

	/**
	 * BOTのIDを取得
	 * 
	 * @return BOTのID
	 */
	public UUID getUUID() {
		Method method = getMethod("getUUID", new Class[0]);
		if (method != null) {
			method.setAccessible(true);
			try {
				return (UUID) method.invoke(instance, new Object[0]);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return this.botId;
	}

	/**
	 * メッセージの通知
	 * 
	 * @param id
	 * @param message
	 * @param time
	 */
	public void notify(final UUID id, final String message, final Date time) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Method method = getMethod("notify", new Class[] { UUID.class,
						String.class, Date.class });
				if (method != null) {
					method.setAccessible(true);
					try {
						Thread.sleep(500);
						method.invoke(instance, new Object[] { id, message,
								time });
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();
	}

	/**
	 * BOTのイメージを取得
	 * 
	 * @return BOTイメージ
	 */
	public BufferedImage getImage() {
		Method method = getMethod("getImage", new Class[0]);
		if (method != null) {
			method.setAccessible(true);
			try {
				return (BufferedImage) method.invoke(instance, new Object[0]);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}