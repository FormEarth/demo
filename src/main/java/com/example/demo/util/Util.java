package com.example.demo.util;

import com.example.demo.common.Dict;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * 公共方法类
 *
 * @author raining_heavily
 * @Description
 * @date 2019年2月17日
 */
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	/**
	 * MD5加密
	 *
	 * @param str
	 * @return
	 */
	public static String md5Digest(String str) {
		// spring自带的MD5加密
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	/**
	 * 获取当前时间字符串
	 *
	 * @return
	 */
	public static String getSystemTime() {
		LocalDateTime current = LocalDateTime.now();
		return current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static String getRemoteIpAddress(HttpServletRequest request) {
		return null;
	}

	/**
	 * 获取本地真实IP地址
	 *
	 * @throws SocketException
	 */
	public static String getLocalRealIP() throws SocketException {

		Enumeration<NetworkInterface> IFaces = NetworkInterface.getNetworkInterfaces();
		while (IFaces.hasMoreElements()) {
			NetworkInterface fInterface = IFaces.nextElement();
			if (!fInterface.isVirtual() && !fInterface.isLoopback() && fInterface.isUp()) {
				Enumeration<InetAddress> addressEnum = fInterface.getInetAddresses();
				while (addressEnum.hasMoreElements()) {
					InetAddress address = addressEnum.nextElement();
					byte[] bs = address.getAddress();
					if (bs.length == 4) {
						return address.getHostAddress();
					}
				}
			}

		}
		return "";
	}

	/**
	 * 用户状态校验
	 *
	 * @return
	 * @throws SystemException
	 */
	public static boolean userVerify(User user) throws SystemException {
		if (user == null) {
			throw new SystemException(ExceptionEnums.USER_NOT_EXIT);
		}
		if (Dict.LOCKED_STATUS.equals(user.getUserStatus())) {
			throw new SystemException(ExceptionEnums.USER_STATUS_IS_LOCK);
		} else if (Dict.INVALID_STATUS.equals(user.getUserStatus())) {
			throw new SystemException(ExceptionEnums.USER_STATUS_IS_INVALID);
		}
		return true;
	}

	/**
	 * 生成shiro加密密码
	 * 
	 * @param source 所需加密的参数 即 密码
	 * @param salt   [盐] 一般为用户名 或 随机数
	 * @return
	 */
	public static String shiroPasswordWithSalt(String source, String salt) {
		int hashIterations = 3;
		// 调用 org.apache.shiro.crypto.hash.Md5Hash.Md5Hash(Object source, Object salt,
		// int hashIterations)构造方法实现MD5盐值加密
		Md5Hash mh = new Md5Hash(source, salt, hashIterations);

		/*
		 * 调用org.apache.shiro.crypto.hash.SimpleHash.SimpleHash(String algorithmName,
		 * Object source, Object salt, int hashIterations) 构造方法实现盐值加密 String
		 * algorithmName 为加密算法 支持md5 base64 等
		 */
//        SimpleHash sh = new SimpleHash("md5", source, salt, hashIterations);
		// 最终结果
		return mh.toString();

	}

	/**
	 * 检查上传图片比例是否一致
	 *
	 * @param images
	 * @return
	 * @throws SystemException
	 */
	public static boolean checkImagesProportion(MultipartFile[] images) throws SystemException {
		if (images.length <= 1) {
			return false;
		}
		BigDecimal proportion = new BigDecimal(0);
		for (int i = 0; i < images.length; i++) {
			BufferedImage bi;
			try {
				bi = ImageIO.read(images[i].getInputStream());
			} catch (IOException e) {
				throw new SystemException(ExceptionEnums.IMAGE_FILE_UPLOAD_FAIL);
			}
			BigDecimal width = new BigDecimal(bi.getWidth());
			BigDecimal height = new BigDecimal(bi.getHeight());
			// 不允许过长的全部展示
			if (height.compareTo(new BigDecimal(300)) > 1) {
				return false;
			}
			// 除非需要定义精确的位数，否则无限循环的会报错
			BigDecimal tempProportion = width.divide(height, 2, RoundingMode.DOWN);
			if (i != 0) {
				if (tempProportion.compareTo(proportion) != 0) {
					return false;
				}
			}
			proportion = tempProportion;
		}

		return true;
	}

	/**
	 * 返回指定位数范围（2~16）的纯数字随机字符串
	 *
	 * @param index 位数
	 * @return
	 */
	public static String randomStr(int index) {
		if (index < 2 || index > 16) {
			return "0000";
		}
		return String.valueOf(Math.random()).substring(2, 2 + index);
	}

	/**
	 * 在指定路径上执行shell命令
	 *
	 * @param path     路径，eg. C:/Users/chunyangwang/Desktop
	 * @param commands 命令（多条）， eg.<br>
	 *                 windows: "cmd", "/c", "hexo generate"<br>
	 *                 Linux: "sh", "/c", "hexo generate"
	 * @return
	 * @throws SystemException
	 */
	public static boolean shellExecute(String path, String... commands) throws SystemException {
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		// 使用cd无法切换目录，指定命令执行路径
		File file = new File(path);
		processBuilder.directory(file);
		Process p;
		try {
			p = processBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("execute command " + Arrays.asList(commands) + "failed!");
			throw new SystemException(ExceptionEnums.COMMAND_EXECUTE_FAILED);
		}
		InputStream in = p.getInputStream();
		// windows默认编码格式为GBK
		BufferedReader bs = null;
		try {
			bs = new BufferedReader(new InputStreamReader(in, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("this error will never appear!");
		}
//        BufferedReader bs = new BufferedReader(new InputStreamReader(in));
		// 阻塞一直到命令执行完毕
		try {
			// TODO 指定阻塞时间，不然会一直在这
			p.waitFor(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("waitFor fail!");
			throw new SystemException(ExceptionEnums.COMMAND_EXECUTE_FAILED);
		}
		StringBuffer buffer = new StringBuffer();
		try {
			while (bs.read() > 0) {
				buffer.append(bs.readLine()).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("read result failed!");
			throw new SystemException(ExceptionEnums.COMMAND_EXECUTE_FAILED);
		}
		logger.error(buffer.toString());
		// 命令执行失败
		if (p.exitValue() != 0) {
			logger.error("command execute failed!path is {},commands is{}", path, Arrays.asList(commands));
			throw new SystemException(ExceptionEnums.COMMAND_EXECUTE_FAILED);
		}
		return true;
	}

	/**
	 * 将首字母大写
	 *
	 * @param str
	 * @return
	 */
	public static String firstUpperCase(String str) {
		String upperChar = str.substring(0, 1).toUpperCase();
		String left = str.substring(1);
		return upperChar + left;
	}

	// 下划线转驼峰命名
	public static String _toCamelCase(String _str) throws SystemException {
		if (!_str.contains("_")) {
			return _str;
		}
		String[] strings = _str.split("_");
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < strings.length; i++) {
			strings[i] = firstUpperCase(strings[i]);
		}
		Stream.of(strings).forEach(x -> sb.append(x));
		return sb.toString();
	}
	
	public static void dateTransfer() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(ldt.format(dtf));
		Properties properties = System.getProperties();
		System.out.println(properties.get("os.name"));
		System.out.println(properties.get("os.arch"));
		System.out.println(properties.get("os.version"));
		System.out.println(properties.get("user.name"));
	}

	public static void readFormFile() throws IOException, SystemException {
		String path = "C:\\Users\\chunyangwang\\Desktop\\aaa.txt";
		String handleStr = "<result property=\"${0}\" column=\"${1}\"/>";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
		String line;
//        while (bufferedReader.read()>0)//此种情况会出现第一个字母丢失的问题
		while ((line = bufferedReader.readLine()) != null) {
//            String str = bufferedReader.readLine();
			String caseStr = _toCamelCase(line);
			System.out.println(handleStr.replace("${0}", caseStr).replace("${1}", line));
//            System.out.println(str);
		}

	}

	public static void main(String[] args) throws CloneNotSupportedException, SystemException, IOException {
//		System.out.println();
//		BigDecimal a = new BigDecimal(5.567);
//		BigDecimal b = new BigDecimal(2.000);
//		System.out.println(a.divide(b).setScale(3,RoundingMode.FLOOR));
//		Integer integer = null;
//		System.out.println("值："+integer);
//		System.out.println(FileSourceEnum.AVATAR.getDynamic());
//        List list = new ArrayList();
//        list = null;
//        System.out.println(list);
//        String str = "hello";
//        System.out.println(Arrays.asList(str));
//        User user = new User();
//        user.setUserName("11111");
//        System.out.println(user);
//        User user1 = (User) user.clone();
//        user1.setUserName("22222");
//        System.out.println(user);
//        User user2 = user;
//        user2.setUserName("33333");
//        System.out.println(user);
//        shellExecute("cd C:\\Users\\chunyangwang\\Desktop","hexo generate");
//        boolean b = shellExecute("C:/Users/chunyangwang/Desktop", "cmd", "/c", "mkdir qwert");

//        Long l = Long.valueOf("-1");
//        System.out.println(l.compareTo(-1L));
//        System.out.println(l.equals(-1));
//        shellExecute("D:/gitbox/hexo-blog/","cmd","/c","hexo generate");
		//readFormFile();
		dateTransfer();
	}

}
