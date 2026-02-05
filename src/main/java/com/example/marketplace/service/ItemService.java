package com.example.marketplace.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.marketplace.entity.Item;
import com.example.marketplace.entity.User;
import com.example.marketplace.repository.ItemRepository;

@Service
public class ItemService {

	// 商品リポジトリの参照
	private final ItemRepository itemRepository;

	// カテゴリ関連のユースケースに備えてサービス参照を保持
	private final CategoryService categoryService;

	// 画像アップロード/削除のための Cloudinary サービス参照
	private final CloudinaryService cloudinaryService;
	
	// 開発環境用のローカルファイル保存サービス
	private final LocalImageService localImageService;

	// 依存性はコンストラクタで注入
	public ItemService(ItemRepository itemRepository,
			CategoryService categoryService,
			ObjectProvider<CloudinaryService> cloudinaryServiceProvider,
			LocalImageService localImageService) {

		// フィールドへ商品リポジトリを設定
		this.itemRepository = itemRepository;
		// フィールドへカテゴリサービスを設定
		this.categoryService = categoryService;
		// フィールドへ Cloudinary サービスを設定
		this.cloudinaryService = cloudinaryServiceProvider.getIfAvailable();
		// フィールドへローカル画像サービスを設定
		this.localImageService = localImageService;
	}

	// 商品検索：キーワード/カテゴリ/ページングを組み合わせ、公開中のみ返す
	public Page<Item> searchItems(String keyword, Long categoryId, int page, int size) {

		// ページング指定を生成
		Pageable pageable = PageRequest.of(page, size);

		// キーワードとカテゴリ両方指定時の検索
		if (keyword != null && !keyword.isEmpty() && categoryId != null) {

			// 名前 LIKE × カテゴリ × 出品中
			return itemRepository
					.findByNameContainingIgnoreCaseAndCategoryIdAndStatus(
							keyword,
							categoryId,
							"出品中",
							pageable);

			// キーワードのみ指定時の検索
		} else if (keyword != null && !keyword.isEmpty()) {

			// 名前 LIKE × 出品中
			return itemRepository
					.findByNameContainingIgnoreCaseAndStatus(
							keyword,
							"出品中",
							pageable);

			// カテゴリのみ指定時の検索
		} else if (categoryId != null) {

			// カテゴリ × 出品中
			return itemRepository
					.findByCategoryIdAndStatus(
							categoryId,
							"出品中",
							pageable);

			// 条件未指定時のデフォルト検索
		} else {

			// 出品中のみ全件ページング
			return itemRepository.findByStatus("出品中", pageable);
		}
	}

	// 全商品一覧を返す（管理用など）
	public List<Item> getAllItems() {
		// リポジトリの全件取得を委譲
		return itemRepository.findAll();
	}

	// 主キーで商品を取得
	public Optional<Item> getItemById(Long id) {
		// Optional をそのまま返す
		return itemRepository.findById(id);
	}

	// 商品保存：必要なら画像を Cloudinary またはローカルファイルシステムへアップロードして URL を保存
	public Item saveItem(Item item, MultipartFile imageFile) throws IOException {

		// 画像が添付されている場合にのみアップロード処理を実行
		if (imageFile != null && !imageFile.isEmpty()) {
			String imageUrl;
			
			// Cloudinaryが設定されている場合はそれを使用、そうでなければローカル保存を使用
			if (cloudinaryService != null) {
				// Cloudinary へアップロードし URL を受け取る
				imageUrl = cloudinaryService.uploadFile(imageFile);
			} else {
				// ローカルファイルシステムに保存
				imageUrl = localImageService.uploadFile(imageFile);
			}

			// 画像 URL をエンティティへ設定
			item.setImageUrl(imageUrl);
		}

		// 商品を保存して返す
		return itemRepository.save(item);
	}

	// 商品削除：Cloudinary 上の画像も可能なら削除してから DB 削除
	public void deleteItem(Long id) {

		// まず対象商品を取得し、存在する場合のみ削除処理を進める
		itemRepository.findById(id).ifPresent(item -> {

			// 画像 URL がある場合は画像を削除
			if (item.getImageUrl() != null) {
				try {
					if (cloudinaryService != null) {
						// Cloudinaryが設定されている場合はCloudinaryから削除
						cloudinaryService.deleteFile(item.getImageUrl());
					} else {
						// そうでなければローカルファイルから削除
						localImageService.deleteFile(item.getImageUrl());
					}
				} catch (IOException e) {
					// 画像削除失敗は致命ではないためログ出力に留める
					System.err.println(
							"Failed to delete image: " + e.getMessage());
				}
			}

			// 最後に DB から商品レコードを削除
			itemRepository.deleteById(id);
		});
	}

	// 出品者の出品一覧を取得
	public List<Item> getItemsBySeller(User seller) {
		// seller 条件で検索
		return itemRepository.findBySeller(seller);
	}

	// 売却確定：商品ステータスを売却済へ変更
	public void markItemAsSold(Long itemId) {

		// 商品を取得して存在する場合のみ更新
		itemRepository.findById(itemId).ifPresent(item -> {

			// ステータスを売却済に変更
			item.setStatus("売却済");

			// 変更を保存
			itemRepository.save(item);
		});
	}
}
