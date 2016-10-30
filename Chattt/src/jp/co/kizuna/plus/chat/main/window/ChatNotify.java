package jp.co.kizuna.plus.chat.main.window;

import java.util.UUID;

public interface ChatNotify {

	void notify(String name, UUID id, String message);
}
