package com.example.marketplace.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.marketplace.entity.User;
import com.example.marketplace.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Controller
public class RegisterController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("form", new RegisterForm());
		return "register";
	}

	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("form") RegisterForm form, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "register";
		}

		// メール重複チェック
		if (userService.getUserByEmail(form.getEmail()).isPresent()) {
			bindingResult.rejectValue("email", "duplicate", "このメールアドレスは既に登録されています。");
			return "register";
		}

		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setRole("USER");
		user.setEnabled(true);
		user.setBanned(false);

		userService.saveUser(user);

		// 登録後はログイン画面へ
		return "redirect:/login?registered";
	}

	public static class RegisterForm {
		@NotBlank(message = "名前は必須です。")
		private String name;

		@NotBlank(message = "メールアドレスは必須です。")
		@Email(message = "メールアドレスの形式が正しくありません。")
		private String email;

		@NotBlank(message = "パスワードは必須です。")
		@Size(min = 6, message = "パスワードは6文字以上にしてください。")
		private String password;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}

