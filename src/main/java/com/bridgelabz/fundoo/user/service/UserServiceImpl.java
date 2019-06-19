package com.bridgelabz.fundoo.user.service;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoo.Exception.UserException;
import com.bridgelabz.fundoo.Response.Response;
import com.bridgelabz.fundoo.Response.ResponseToken;
import com.bridgelabz.fundoo.Utility.ResponseHelper;
import com.bridgelabz.fundoo.Utility.TokenUtil;
import com.bridgelabz.fundoo.Utility.UtilityMain;
import com.bridgelabz.fundoo.user.dto.LoginDTO;
import com.bridgelabz.fundoo.user.dto.UserDTO;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.UserRepository;

@PropertySource("classpath:message.properties")
@Service("userService")
public class UserServiceImpl implements UserService 
{
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private Response statusResponse;

	@Autowired
	private Environment environment;

	@Override
	public Response onRegister(UserDTO userDto) 
	{
		String emailId = userDto.getEmailId();
		System.out.println("EmailId from DTO "+emailId);
		User user = modelMapper.map(userDto, User.class);
		System.out.println("EmailId "+user.getEmailId());
		System.out.println("Name "+user.getFirstName());
		System.out.println("Password "+user.getPassword());
		System.out.println("Phone "+user.getMobileNum());
		Optional<User> useralreadyPresent = userRepo.findByEmailId(user.getEmailId());
		if (useralreadyPresent.isPresent()) 
		{
			throw new UserException(environment.getProperty("status.register.emailExistError"));
		}
		// encode user password
		String password = passwordEncoder.encode(userDto.getPassword());

		user.setPassword(password);
		user = userRepo.save(user);
		Long userId = user.getUserId();
		System.out.println(emailId + " " + userId);
		UtilityMain.send(emailId, "confirmation mail", UtilityMain.getUrl(userId) + "/valid");

		statusResponse = ResponseHelper.statusResponse(200, "register successfully");
		return statusResponse;
	}

	@Override
	public ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException 
	{
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		ResponseToken response = new ResponseToken();
		if (user.isPresent()) 
		{
			return authentication(user, loginDto.getPassword());
		}

		System.out.println("response is " + response);
		return response;
	}

	@Override
	public ResponseToken authentication(Optional<User> user, String password) 
	{
		ResponseToken response = new ResponseToken();
		if (user.get().isVerify()) 
		{
			boolean status = passwordEncoder.matches(password, user.get().getPassword());

			if (status == true) 
			{
				String token = tokenUtil.createToken(user.get().getUserId());
				response.setToken(token);
				response.setStatusCode(200);
				response.setStatusMessage(environment.getProperty("user.login"));
				return response;
			}

			throw new UserException(401, environment.getProperty("user.login.password"));
		}

		throw new UserException(401, environment.getProperty("user.login.verification"));
	}

	@Override
	public Response validateEmailId(String token) 
	{
		Long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id).orElseThrow(() -> new UserException(404, environment.getProperty("user.validation.email")));
		user.setVerify(true);
		userRepo.save(user);
		statusResponse = ResponseHelper.statusResponse(200, environment.getProperty("user.validation"));
		return statusResponse;
	}

	@Override
	public Response forgetPassword(String emailId) throws UserException, UnsupportedEncodingException 
	{
		Optional<User> useralreadyPresent = userRepo.findByEmailId(emailId);

		if (!useralreadyPresent.isPresent()) 
		{
			throw new UserException(401, environment.getProperty("user.forgetpassword.emaiId"));
		}
		Long id = useralreadyPresent.get().getUserId();
		UtilityMain.send(emailId, "password reset mail", UtilityMain.getUrl(id));
		return ResponseHelper.statusResponse(200, environment.getProperty("user.forgetpassword.link"));
	}

	@Override
	public Response resetPaswords(String token, String password) 
	{
		Long id = tokenUtil.decodeToken(token);
		User user = userRepo.findById(id).orElseThrow(() -> new UserException(404, environment.getProperty("user.resetpassword.user")));
		String encodedpassword = passwordEncoder.encode(password);
		user.setPassword(encodedpassword);
		userRepo.save(user);
		return ResponseHelper.statusResponse(200, "password successfully reset");
	}
}
