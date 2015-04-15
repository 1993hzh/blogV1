package edu.cuit.hzhspace.service;

import org.springframework.stereotype.Service;

import edu.cuit.hzhspace.model.User;
import edu.cuit.hzhspace.util.Encryption;

@Service
public class UserService extends AbstractService<User> {

	public UserService(Class<User> entity) {
		super(entity);
	}

	public UserService() {
		this(User.class);
	}

	/**
	 * @param name
	 * @param password
	 * @return
	 */
	public User login(String name, String password) {
		password = Encryption.encoderBySHA1(password);// 密码加密
		User dbUser = null;
		try {
			dbUser = (User) emRead.createQuery(
					"SELECT u FROM User u WHERE u.name='" + name + "' and u.password='" + password + "'")
					.getSingleResult();
		} catch (Exception ingore) {
//			logger.error(ingore.getLocalizedMessage());
		}
		return dbUser;
	}

}
