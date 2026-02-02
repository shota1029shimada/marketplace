package com.example.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security の設定クラス
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	/**
	 * PasswordEncoder を DI コンテナに登録する Bean 定義
	 * DelegatingPasswordEncoder により、{bcrypt} / {noop} などの prefix 形式に対応
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * Spring Security のフィルタチェーン定義
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// 認可（アクセス制御）
				.authorizeHttpRequests(auth -> auth
						// 未ログインでもアクセス可能なパス
						.requestMatchers(
								"/login",
								"/register",
								"/css/**",
								"/js/**",
								"/images/**",
								"/webjars/**")
						.permitAll()

						// 管理者専用
						.requestMatchers("/admin/**").hasRole("ADMIN")

						// それ以外はログイン必須
						.anyRequest().authenticated())

				// フォームログイン設定
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/items", true)
						.permitAll())

				// ログアウト設定
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.permitAll())

				// CSRF（デフォルト有効）
				.csrf(Customizer.withDefaults());

		return http.build();
	}
}
