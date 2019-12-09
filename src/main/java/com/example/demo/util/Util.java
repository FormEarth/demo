package com.example.demo.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.demo.common.FileSourceEnum;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;

import com.example.demo.common.Dict;
import com.example.demo.entity.User;
import com.example.demo.exception.ExceptionEnums;
import com.example.demo.exception.SystemException;
import com.example.demo.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

/**
 * 公共方法类
 *
 * @author raining_heavily
 * @Description
 * @date 2019年2月17日
 */
public class Util {

    /**
     * 图片访问的路径前缀 @Value 无法给static进行赋值
     **/
    @Value("${image.access.url}")
    public String imageAccessPref;
    @Autowired
    UserService userService;

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
     * 获取系统时间
     *
     * @return
     */
    public static String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 获取本地真实IP地址
     *
     * @throws SocketException
     */
    public static String getRealIP() throws SocketException {

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

    public static void shiroPasswordWithSalt() {
        // 所需加密的参数 即 密码
        String source = "123456";
        // [盐] 一般为用户名 或 随机数
        String salt = "Shiro";
        // 加密次数
        int hashIterations = 3;

        // 调用 org.apache.shiro.crypto.hash.Md5Hash.Md5Hash(Object source, Object salt,
        // int hashIterations)构造方法实现MD5盐值加密
        Md5Hash mh = new Md5Hash(source, salt, hashIterations);
        // 打印最终结果
        System.out.println(mh.toString());

        /*
         * 调用org.apache.shiro.crypto.hash.SimpleHash.SimpleHash(String algorithmName,
         * Object source, Object salt, int hashIterations) 构造方法实现盐值加密 String
         * algorithmName 为加密算法 支持md5 base64 等
         */
        SimpleHash sh = new SimpleHash("md5", source, salt, hashIterations);
        // 打印最终结果
        System.out.println(sh.toString());

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
            bi.getType();
            BigDecimal width = new BigDecimal(bi.getWidth());
            BigDecimal height = new BigDecimal(bi.getHeight());
            //除法需要定义精确的位数，否则无限循环的会报错
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

    public static void main(String[] args) throws CloneNotSupportedException {
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
        User user = new User();
        user.setUserName("11111");
        System.out.println(user);
        User user1 = (User) user.clone();
        user1.setUserName("22222");
        System.out.println(user);
        User user2 = user;
        user2.setUserName("33333");
        System.out.println(user);


    }

}
