package com.bridgelabz.fundoo.user.controller;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bridgelabz.fundoo.Exception.UserException;
import com.bridgelabz.fundoo.Response.Response;
import com.bridgelabz.fundoo.Response.ResponseToken;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.service.UserService;

@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RestController
public class UserController 
{
	@Autowired
	UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody UserDTO userDto)
			throws UserException, UnsupportedEncodingException 
	{
		System.out.println("Inside Controller"+userDto.getEmailId());
		Response response = userService.onRegister(userDto);
		System.out.println("Inside controller after service");
		System.out .println(response);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseToken> onLogin(@RequestBody LoginDTO loginDTO)
			throws UserException, UnsupportedEncodingException 
	{
		System.out.println("Inside Controller"+loginDTO.getEmailId());
		ResponseToken response = userService.onLogin(loginDTO);
		System.out.println("Inside controller after service");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{token}/valid")
	public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException 
	{
		Response response = userService.validateEmailId(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestParam String emailId)
			throws UnsupportedEncodingException, UserException, MessagingException 
	{
		System.out.println(emailId);
		Response status = userService.forgetPassword(emailId);
		return new ResponseEntity<Response>(status, HttpStatus.OK);
	}

	@PutMapping(value = "/resetpassword/{token}")
	public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam("password") String password)
			throws UserException 
	{
		Response response = userService.resetPaswords(token, password);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
