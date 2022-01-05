package com.yl.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.Collator;
import java.util.*;



/**
 * @author jerry
 *
 */
@Slf4j
public class SignUtil {

	public static String publicKey; // 公钥
	public static String privateKey; // 私钥

	private static final String SIGN_FILTER_KEYS = "|sign|";

	private final static String[] strDigits =
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final Random RANDOM = new SecureRandom();

	/**
	 * 方法用途：key-value按key的ascii码升序拼接
	 * */
	public static String getKeyValueStr(Map<String, Object> params) {

		StringBuilder sb = new StringBuilder();
		Set<String> keySets = params.keySet();
		List<String> keys = new ArrayList<String>(keySets);
//		Collections.sort(keys);			//首字母排序
//		全名排序
		Comparator<Object> CHINA_COMPARE = Collator.getInstance(Locale.CHINA);
		keys.sort((o1, o2) -> ((Collator) CHINA_COMPARE).compare(o1, o2));

		int n = 0;
		for (String key : keys) {
			if (key.equals("sign")) {
				continue;
			}
			if (null == params.get(key)) {
				continue;
			}
			String val = params.get(key).toString();
			if (StringUtils.isEmpty(val)) {
				continue;
			}
			if (SIGN_FILTER_KEYS.indexOf("|" + key + "|") >= 0) {
				continue;
			}
			if (n > 0) {
				sb.append("&");
//				sb.append(val);
			}
			sb.append(key).append("=").append(val);
			n++;
		}
		return sb.toString();
	}

	/**
	 *  MD5签名,UTF8编码格式
	 * */
	public static String enCode(String strObj)
	{
		String resultString = null;
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes("utf-8")));
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("MD5出现异常,加密失败.", e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * key-value按key的ascii码升序拼接，&key=MD5密钥的形式拼接密钥
	 * */
	public static String genMd5AndKey( Map<String, Object> params,String key) {
		String signSrc = getKeyValueStr(params);
		log.info("key-value按key的ascii码升序拼接结果："+signSrc);
		String verifyVal = enCode(signSrc+"&key="+key);
		return verifyVal;
	}

	/**
	 * 字节转字符串
	 */
	private static String byteToString(byte[] bByte)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++)
		{
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString().toLowerCase();
		// return sBuffer.toString().toUpperCase();
	}

	/**
	 * 字节转字符串数组
	 */
	private static String byteToArrayString(byte bByte)
	{
		int iRet = bByte;
		if (iRet < 0)
		{
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}


	//取当前时间戳
	public static String generateNonceStr() {
		char[] nonceChars = new char[32];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}
	/*-----------------------------------------单向散列加密--------------------------------------------------------*/
	/**
	 * MD5加密方法
	 * 加密算法：单向散列加密
	 * 概述：是RSA数据安全公司开发的一种单向散列算法，非可逆，相同的明文产生相同的密文。md5（标准密钥长度128位）
	 * 优点：在相同的硬件上，MD5的运行速度比SHA-1快
	 * 缺点：由于MD5的设计，易受密码分析的攻击
	 * */
	public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		//确定计算方法
		MessageDigest md5=MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		//加密后的字符串
		String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}

	/**
	 * MD5加密验证方法
	 * */
	public static boolean verifyMd5(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if(EncoderByMd5(newpasswd).equals(oldpasswd))
			return true;
		else
			return false;
	}

	/**
	 * SHA1加密方法
	 * 加密算法：单向散列加密
	 * 概述：可以对任意长度的数据运算生成一个160位的数值，定长输出和不可逆，sha1（标准密钥长度160位）
	 * 优点：SHA-1跟MD5相比：SHA-1对强行攻击有更大的强度;SHA-1显得不易受密码分析的攻击
	 * 缺点：在相同的硬件上，SHA-1的运行速度比MD5慢
	 * */
	public static String getSha1(String str){
		if(str==null||str.length()==0){
			return null;
		}
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
				'a','b','c','d','e','f'};
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j*2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * SHA1验证方法
	 * */
	public static boolean verifySHA1(String newpasswd,String oldpasswd) {
		if (getSha1(newpasswd).equals(oldpasswd))
			return true;
		else
			return false;
	}


	/*------------------------------------------DESC对称加密------------------------------------------------------*/
	/**
	 * DES加密
	 * 加密算法：对称加密
	 * 概述：加密解密使用同一个密钥、数据的机密性双向保证、加密效率高、适合加密于大数据大文件、加密强度不高(相对于非对称加密)
	 * 优点：与公钥加密相比运算速度快。
	 * 缺点：不能作为身份验证，密钥发放困难
	 * */
	public static byte[] encrypt(byte[] datasource, String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象,ENCRYPT_MODE用于将 Cipher 初始化为加密模式的常量
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(datasource); // 按单部分操作加密或解密数据，或者结束一个多部分操作
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *  DES解密
	 * */
	public static byte[] decrypt(byte[] src, String password) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom random = new SecureRandom();
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(password.getBytes());
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 返回实现指定转换的
		// Cipher
		// 对象
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, random);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	/*------------------------------------------RSA非对称加密------------------------------------------------------*/
	/**
	 * RSA加密
	 * 加密算法：非对称加密
	 * 概述：非对称加密算法需要两个密钥：公开密钥（publickey:简称公钥）和私有密钥（privatekey:简称私钥）
	 * 		公钥对数据进行加密，只有用对应的私钥才能解密；私钥对数据进行加密，只有用对应的公钥才能解密
	 * 优点：算法强度复杂，保密性比较好
	 * 缺点：加密解密速度没有对称加密解密的速度快。
	 * */
	/**
	 * RSA生成公钥和私钥
	 */
	public static void generateKey() {
		// 1.初始化秘钥
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			SecureRandom sr = new SecureRandom(); // 随机数生成器
			keyPairGenerator.initialize(512, sr); // 设置512位长的秘钥
			KeyPair keyPair = keyPairGenerator.generateKeyPair(); // 开始创建
			RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
			// 进行转码
			publicKey = Base64.encodeBase64String(rsaPublicKey.getEncoded());
			// 进行转码
			privateKey = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * RSA私钥匙加密或解密
	 * */
	public static String encryptByprivateKey(String content, String privateKeyStr, int opmode) {
		// 私钥要用PKCS8进行处理
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr));
		KeyFactory keyFactory;
		PrivateKey privateKey;
		Cipher cipher;
		byte[] result;
		String text = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			// 还原Key对象
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			cipher = Cipher.getInstance("RSA");
			cipher.init(opmode, privateKey);
			if (opmode == Cipher.ENCRYPT_MODE) { // 加密
				result = cipher.doFinal(content.getBytes());
				text = Base64.encodeBase64String(result);
			} else if (opmode == Cipher.DECRYPT_MODE) { // 解密
				result = cipher.doFinal(Base64.decodeBase64(content));
				text = new String(result, "UTF-8");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * RSA公钥匙加密或解密
	 * */
	public static String encryptByPublicKey(String content, String publicKeyStr, int opmode) {
		// 公钥要用X509进行处理
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
		KeyFactory keyFactory;
		PublicKey publicKey;
		Cipher cipher;
		byte[] result;
		String text = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			// 还原Key对象
			publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			cipher = Cipher.getInstance("RSA");
			cipher.init(opmode, publicKey);
			if (opmode == Cipher.ENCRYPT_MODE) { // 加密
				result = cipher.doFinal(content.getBytes());
				text = Base64.encodeBase64String(result);
			} else if (opmode == Cipher.DECRYPT_MODE) { // 解密
				result = cipher.doFinal(Base64.decodeBase64(content));
				text = new String(result, "UTF-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}

	/**
	 * map转JSON
	 * */
	public static String mapToJson(Map<String, String> map) {
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			json.append("\"").append(key).append("\"");
			json.append(":");
			json.append("\"").append(value).append("\"");
			if (it.hasNext()) {
				json.append(",");
			}
		}
		json.append("}");
		System.out.println("mapToJson=" + json.toString());
		return json.toString();
	}



	public static void main(String[] args) throws Exception {
		try {
			//todo md5
			//假设passwd是从数据库取出来的
			String passwd = EncoderByMd5("zhaojun98");		//MD5加密方法
			System.out.println("MD5加密----->>>>"+passwd);
			boolean check = verifyMd5("zhaojun98",passwd);
			System.out.println("MD5验证结果----->>>>"+check);

			//todo SHA1
			//假设passwd是从数据库取出来的密码
			String pwd = getSha1("abc");
			System.out.println("SHA1--->>"+passwd);
			boolean checkSHA1 = verifySHA1("abc", pwd);
			System.out.println("SHA1验证结果：--->>"+check);

			//todo ESC
			//待加密内容
			String str = "123456";
			// 密码，长度要是8的倍数 密钥随意定
			String password = "12345678";
			byte[] encrypt = encrypt(str.getBytes(), password);
			System.out.println("ESC加密前:" +str);
			System.out.println("ESC加密后:" + new String(encrypt));
			// 解密
			byte[] decrypt = decrypt(encrypt, password);
			System.out.println("ESC解密后:" + new String(decrypt));


			//todo RSA
			/**
			 * 注意： 私钥加密必须公钥解密 公钥加密必须私钥解密
			 *  正常在开发中的时候,后端开发人员生成好密钥对，服务器端保存私钥 客户端保存公钥
			 */
			System.out.println("-------------生成两对秘钥，分别发送方和接收方保管-------------");
			generateKey();			//RSA生成公钥和私钥
			System.out.println("公钥:" + publicKey);
			System.out.println("私钥:" + privateKey);

			System.out.println("-------------私钥加密公钥解密-------------");
			String textsr = "11111111";
			// 私钥加密
			String cipherText = encryptByprivateKey(textsr, privateKey, Cipher.ENCRYPT_MODE);
			System.out.println("私钥加密后：" + cipherText);
			// 公钥解密
			String text = encryptByPublicKey(cipherText, publicKey, Cipher.DECRYPT_MODE);
			System.out.println("公钥解密后：" + text);

			System.out.println("-------------公钥加密私钥解密-------------");
			// 公钥加密
			String textsr2 = "222222";

			String cipherText2 = encryptByPublicKey(textsr2, publicKey, Cipher.ENCRYPT_MODE);
			System.out.println("公钥加密后：" + cipherText2);
			// 私钥解密
			String text2 = encryptByprivateKey(cipherText2, privateKey, Cipher.DECRYPT_MODE);
			System.out.print("私钥解密后：" + text2 );
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
