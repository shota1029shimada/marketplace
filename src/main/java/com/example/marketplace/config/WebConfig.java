package com.example.marketplace.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web設定クラス
 * 静的リソース（画像ファイルなど）の配信設定を行う
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final String uploadDir;

	public WebConfig(@Value("${local.image.upload-dir:uploads/images}") String uploadDirPath) {
		this.uploadDir = uploadDirPath;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// アップロードされた画像ファイルを /images/** でアクセスできるように設定
		Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
		String uploadPathStr = "file:" + uploadPath + "/";
		
		registry.addResourceHandler("/images/**")
				.addResourceLocations(uploadPathStr)
				.setCachePeriod(3600); // キャッシュ期間を1時間に設定
	}
}
