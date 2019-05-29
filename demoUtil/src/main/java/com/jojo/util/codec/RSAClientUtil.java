/**
 * 
 */
package com.jojo.util.codec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * 工具类 - RSA
 */
public final class RSAClientUtil {

	/**
	 * RSA算法标识
	 */
	public static final String KEY_ALGORITHM_RSA = "RSA";

	/**
	 * RSA&MD5签名算法
	 */
	private static final String SIGNATURE_ALGORITHM_RSA_MD5 = "MD5withRSA";

	/**
	 * UTF-8编码标识
	 */
	private static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 将字符串通过RSA公钥字符串加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param pubKey
	 *            RSA公钥字符串
	 * @return 加密结果
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptByRSAPublicKeyString(String data, String pubKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSAPublicKey publicKey = getRSAPublicKey(pubKey);
		return encryptByRSAPublicKey(data, publicKey);
	}

	/**
	 * 将字符串通过RSA私钥字符串解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param priKey
	 *            RSA私钥字符串
	 * @return 解密结果
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String decryptByRSAPrivateKeyString(String data, String priKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		RSAPrivateKey privateKey = getRSAPrivateKey(priKey);
		return decryptByRSAPrivateKey(data, privateKey);
	}

	/**
	 * 用私钥字符串对信息生成数字签名
	 * 
	 * @param content
	 *            加密数据
	 * @param priKey
	 *            私钥字符串
	 * @return 签名
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	public static String sign(String content, String priKey) throws NoSuchAlgorithmException, InvalidKeySpecException,
			InvalidKeyException, SignatureException, UnsupportedEncodingException {
		// 取私钥匙对象
		PrivateKey privateKey = getRSAPrivateKey(priKey);
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_RSA_MD5);
		signature.initSign(privateKey);
		byte[] data = content.getBytes(CHARSET_UTF8);
		signature.update(data);
		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * 根据RSA公钥字符串获取RSA公钥
	 * 
	 * @param pubKey
	 *            RSA公钥字符串
	 * @return RSA公钥
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static RSAPublicKey getRSAPublicKey(String pubKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 使用Base64解码值构造X509EncodedKeySpec对象
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		// 返回RSA公钥对象
		return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
	}

	/**
	 * 根据RSA私钥字符串获取RSA私钥
	 * 
	 * @param priKey
	 *            RSA私钥字符串
	 * @return RSA私钥
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static RSAPrivateKey getRSAPrivateKey(String priKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 使用Base64解码值构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
		// 指定加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM_RSA);
		// 返回RSA私钥对象
		return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
	}

	/**
	 * 将字符串通过RSA公钥加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param publicKey
	 *            RSA公钥
	 * @return 加密结果
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 */
	private static String encryptByRSAPublicKey(String data, RSAPublicKey publicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 模长
		int key_len = publicKey.getModulus().bitLength() / 8;
		// 加密数据长度 <= 模长-11
		String[] datas = splitString(data, key_len - 11);
		String mi = "";
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			mi += bcd2Str(cipher.doFinal(s.getBytes(CHARSET_UTF8)));
		}
		return mi;
	}

	/**
	 * 将字符串通过RSA私钥解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param privateKey
	 *            RSA私钥
	 * @return 解密结果
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws UnsupportedEncodingException
	 */
	private static String decryptByRSAPrivateKey(String data, RSAPrivateKey privateKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[] bytes = data.getBytes(CHARSET_UTF8);
		byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
		// 如果密文长度大于模长则要分组解密
		String ming = "";
		byte[][] arrays = splitArray(bcd, key_len);
		for (byte[] arr : arrays) {
			ming += new String(cipher.doFinal(arr));
		}
		return ming;
	}

	/**
	 * ASCII码转BCD码
	 * 
	 * @param ascii
	 * @param asc_len
	 * @return
	 */
	private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	/**
	 * ASCII码转BCD码
	 * 
	 * @param asc
	 * @return
	 */
	private static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9')) {
			bcd = (byte) (asc - '0');
		} else if ((asc >= 'A') && (asc <= 'F')) {
			bcd = (byte) (asc - 'A' + 10);
		} else if ((asc >= 'a') && (asc <= 'f')) {
			bcd = (byte) (asc - 'a' + 10);
		} else {
			bcd = (byte) (asc - 48);
		}
		return bcd;
	}

	/**
	 * BCD转字符串
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	/**
	 * 拆分字符串
	 * 
	 * @param string
	 * @param len
	 * @return
	 */
	private static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 * 
	 * @param data
	 * @param len
	 * @return
	 */
	private static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}
}
