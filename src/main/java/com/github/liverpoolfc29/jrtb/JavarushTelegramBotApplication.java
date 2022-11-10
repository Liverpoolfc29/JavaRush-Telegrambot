package com.github.liverpoolfc29.jrtb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling это механизм в Spring фреймворке для создания фонового процесса, который будет выполняться в определенное время, задаваемого нами. Вешаем на входной класс.
@SpringBootApplication
@EnableScheduling
public class JavarushTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavarushTelegramBotApplication.class, args);
	}

}
