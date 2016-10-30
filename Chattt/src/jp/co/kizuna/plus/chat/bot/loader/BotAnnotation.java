package jp.co.kizuna.plus.chat.bot.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface BotAnnotation {
	String value(); // 文字列を引数とする例
}
