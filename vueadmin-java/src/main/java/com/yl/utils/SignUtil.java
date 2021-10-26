package com.yl.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Collator;
import java.util.*;


/**
 * @author zeming.fan@swiftpass.cn
 *
 */
public class SignUtil {
    //private final static Logger log = LoggerFactory.getLogger(SignUtil.class);
	private static Logger LOG = LoggerFactory.getLogger(SignUtil.class);


	private static final String SIGN_FILTER_KEYS = "|sign|";

	private final static String[] strDigits =
			{ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };


	//请求时根据不同签名方式去生成不同的sign
//    public static String getSign(String signType,String preStr){
//    	if("RSA_1_256".equals(signType)){
//        	try {
//        		return SignUtil.sign(preStr,"RSA_1_256","");
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//        }else{
//        	return MD5.sign(preStr, "&key=" + "809a1b299debdb4d96c8c8640718af56", "utf-8");
//        }
//    	return null;
//    }
    
    //对返回参数的验证签名
//    public static boolean verifySign(String sign,String signType,Map<String,String> resultMap) throws Exception{
//    	if("RSA_1_256".equals(signType)){
//    		Map<String,String> Reparams = SignUtils.paraFilter(resultMap);
//            StringBuilder Rebuf = new StringBuilder((Reparams.size() +1) * 10);
//            SignUtils.buildPayParams(Rebuf,Reparams,false);
//            String RepreStr = Rebuf.toString();
//            if(com.yl.utils.wxpay.SignUtil.verifySign(RepreStr,sign, "RSA_1_256", "")){
//            	return true;
//            }
//    	}else if("MD5".equals(signType)){
//    		if(SignUtils.checkParam(resultMap, "809a1b299debdb4d96c8c8640718af56")){
//    			return true;
//    		}
//    	}
//    	return false;
//    }
//	public static boolean verifySign(String preStr,String sign,String signType, String platPublicKey) throws Exception {
//		// 调用这个函数前需要先判断是MD5还是RSA
//		// 商户的验签函数要同时支持MD5和RSA
//		RSAUtil.SignatureSuite suite = null;
//
//		if ("RSA_1_1".equals(signType)) {
//			suite = RSAUtil.SignatureSuite.SHA1;
//		} else if ("RSA_1_256".equals(signType)) {
//			suite = RSAUtil.SignatureSuite.SHA256;
//		} else {
//			throw new Exception("不支持的签名方式");
//		}
//
//		boolean result = RSAUtil.verifySign(suite, preStr.getBytes("UTF8"), Base64.decodeBase64(sign.getBytes("UTF8")),
//                platPublicKey);
//
//		return result;
//    }

//    public static String sign(String preStr, String signType, String mchPrivateKey) throws Exception {
//		RSAUtil.SignatureSuite suite = null;
//		if ("RSA_1_1".equals(signType)) {
//			suite = RSAUtil.SignatureSuite.SHA1;
//		} else if ("RSA_1_256".equals(signType)) {
//			suite = RSAUtil.SignatureSuite.SHA256;
//		} else {
//			throw new Exception("不支持的签名方式");
//		}
//        byte[] signBuf = RSAUtil.sign(suite, preStr.getBytes("UTF8"),
//                mchPrivateKey);
//        return new String(Base64.encodeBase64(signBuf), "UTF8");
//    }

	/**
	 * keyvalue按key的ascii码升序拼接
	 *
	 * @return
	 */
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
	/*
	 * MD5签名
	 * */
	public static String enCode1(String strObj)
	{
		String resultString = null;
		try
		{
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes("utf-8")));
		}
		catch (NoSuchAlgorithmException e)
		{
			LOG.error("MD5出现异常,加密失败.", e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resultString;
	}

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


	private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static final Random RANDOM = new SecureRandom();

	public static String generateNonceStr() {
		char[] nonceChars = new char[32];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}


}
