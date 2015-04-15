package edu.cuit.hzhspace.util;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 1.调用存储过程返回object结果集
 * 2.调用存储过程返回封装好的实体
 * 3.nativeSQL查询返回object结果集
 * 4.nativeSQL查询返回封装好的实体
 * @author Zhonghua Hu
 *
 */
public class JdbcUtil {
	private static Logger logger = Logger.getLogger(JdbcUtil.class);
	private static final String URL = SystemConf.getConf("url");
	private static final String USER = SystemConf.getConf("user");
	private static final String PWD = SystemConf.getConf("pwd");
	private static Connection conn = null;

	/**
	 * @param name
	 * @param params
	 * @return
	 */
	public static List<Object[]> callProcedure(String name, List<Object> params) {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		StringBuffer sb = new StringBuffer("call " + name + "(");
		List<Object[]> result = new LinkedList<Object[]>();
		try {
			getConnection();
			for (int i = 0; i < params.size(); i++) {
				sb.append("?,");
			}
			if (null != params && params.size() > 0) {
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append(")");
			statement = conn.prepareCall(sb.toString());
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] object = new Object[resultSet.getMetaData().getColumnCount()];
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					object[i - 1] = resultSet.getObject(i);
				}
				result.add(object);
				object = null;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				closeConnection();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
			}
		}
		return new ArrayList<Object[]>(result);
	}

	/**
	 * @param sql
	 * @return
	 */
	public static List<Object[]> nativeQuery(String sql) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		List<Object[]> result = new LinkedList<Object[]>();
		try {
			getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Object[] object = new Object[resultSet.getMetaData().getColumnCount()];
				for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
					object[i - 1] = resultSet.getObject(i);
				}
				result.add(object);
				object = null;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				closeConnection();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
			}
		}
		return new ArrayList<Object[]>(result);
	}

	/**
	 * 返回单个结果集
	 * @param sql
	 * @return
	 */
	public static Object[] singleNativeQuery(String sql) {
		List<Object[]> result = nativeQuery(sql);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public static Object getFirstBySingleNativeQuery(String sql) {
		Object[] result = singleNativeQuery(sql);
		if (result != null) {
			return result[0];
		} else {
			return null;
		}
	}

	/**
	 * 封装成实体
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> nativeQuery(String sql, Class<T> clazz) {
		ResultSet resultSet = null;
		PreparedStatement statement = null;
		List<T> result = new LinkedList<T>();
		Map<String, Method> methodMap = getSetterMethod(clazz);
		try {
			getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				T class_ = clazz.newInstance();
				ResultSetMetaData data = resultSet.getMetaData();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String columnName = data.getColumnName(i);
					Method method = methodMap.get(columnName.toLowerCase());
					if (method != null) {
						setterMethodInvoke(class_, resultSet.getObject(i), method);
					}
				}
				result.add(class_);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
			try {
				resultSet.close();
				statement.close();
				closeConnection();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
			}
		}
		return new ArrayList<T>(result);
	}

	/**
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public static <T> T singleNativeQuery(String sql, Class<T> clazz) {
		List<T> result = nativeQuery(sql, clazz);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 执行update或delete
	 * @param sql
	 * @param param
	 */
	public static <T> void nativeUpdate(String sql, List<T> param) {
		PreparedStatement statement = null;
		try {
			getConnection();
			statement = conn.prepareStatement(sql);
			for (int i = 1; i <= param.size(); i++) {
				statement.setObject(i, param.get(i - 1));
			}
			statement.executeUpdate();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
			try {
				statement.close();
				closeConnection();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
			}
		}
	}

	/**
	 * 调用setter方法赋值，并对特殊类型的字段进行处理
	 * @param target
	 * @param value
	 * @param method
	 * @return
	 */
	private static <T> Object setterMethodInvoke(T target, T value, Method method) {
		Object result = null;
		Class<?> clazz = method.getParameterTypes()[0];
		try {
			if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
				result = method.invoke(target, value.equals("false") ? false : true);
			} else if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
				result = (value == null) ? null : method.invoke(target, value);
			} else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
				result = method.invoke(target, value == null ? 0 : ((Integer) value).byteValue());
			} else {
				result = method.invoke(target, value);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return result;
	}

	/**
	 * 得到实体类中的setter方法
	 * @param clazz
	 * @return
	 */
	private static <T> Map<String, Method> getSetterMethod(Class<T> clazz) {
		Method[] methods = clazz.getMethods();
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				//将set去掉同时所有字母改成小写方便匹配
				//对boolean方法默认前面加上is,为防止命名不规范的情况，例如boolean access;增加两条记录，isaccess和access
				Class<?>[] class_ = m.getParameterTypes();
				if (class_[0].equals(boolean.class) || class_[0].equals(Boolean.class)) {
					methodMap.put(m.getName().replaceFirst("set", "is").toLowerCase(), m);
				}
				methodMap.put(m.getName().replaceFirst("set", "").toLowerCase(), m);
			}
		}
		return methodMap;
	}

	private static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(URL, USER, PWD);
		return conn;
	}

	private static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
	}
}
