package com.example.marketplace.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 開発環境用のローカルファイルシステムに画像を保存するサービス
 * Cloudinaryが設定されていない場合に使用されます
 */
@Service
public class LocalImageService {

	private final Path uploadDir;

	public LocalImageService(@Value("${local.image.upload-dir:uploads/images}") String uploadDirPath) {
		// アップロードディレクトリのパスを設定
		this.uploadDir = Paths.get(uploadDirPath);
		
		// ディレクトリが存在しない場合は作成
		try {
			if (!Files.exists(this.uploadDir)) {
				Files.createDirectories(this.uploadDir);
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to create upload directory: " + uploadDirPath, e);
		}
	}

	/**
	 * 画像ファイルをローカルファイルシステムに保存し、公開URLを返す
	 * 
	 * @param file アップロードされたファイル
	 * @return 公開URL（/images/ファイル名の形式）
	 * @throws IOException ファイル保存に失敗した場合
	 */
	public String uploadFile(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			return null;
		}

		// 元のファイル名を取得
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.isEmpty()) {
			return null;
		}

		// 一意のファイル名を生成（UUID + 元の拡張子）
		String extension = "";
		int lastDotIndex = originalFilename.lastIndexOf('.');
		if (lastDotIndex > 0) {
			extension = originalFilename.substring(lastDotIndex);
		}
		String uniqueFilename = UUID.randomUUID().toString() + extension;

		// ファイルを保存
		Path targetPath = uploadDir.resolve(uniqueFilename);
		Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

		// 公開URLを返す（/images/ファイル名の形式）
		return "/images/" + uniqueFilename;
	}

	/**
	 * 画像ファイルを削除
	 * 
	 * @param imageUrl 削除する画像のURL
	 * @throws IOException ファイル削除に失敗した場合
	 */
	public void deleteFile(String imageUrl) throws IOException {
		if (imageUrl == null || imageUrl.isEmpty()) {
			return;
		}

		// URLからファイル名を抽出（/images/ファイル名の形式を想定）
		String filename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
		Path filePath = uploadDir.resolve(filename);

		// ファイルが存在する場合は削除
		if (Files.exists(filePath)) {
			Files.delete(filePath);
		}
	}
}
