package cn.android.common.util;

/**
 * @author JonyZhang E-mail:xxpgd2@gmail.com
 * @version Create time��2014-5-5
 * @description
 */
public class RegexUtils {

	/**
	 * ��� email�����Ƿ���ȷ ��ȷ����д�� ʽΪ username@domain
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkEmail(String value, int length) {
	return value.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")&&value.length()<=length;
	}

	/**
	 * ���绰���� �Ƿ���ȷ ��ȷ�� ʽ 012-87654321��0123-87654321��0123��7654321
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkTel(String value) {
	return value.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)");
	}

	/**
	 * ����ֻ����� �Ƿ���ȷ
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkMobile(String value) {
	return value.matches("^[1][3,5]+\\d{9}");
	}

	/**
	 * ����������� ���Ƿ���ȷ
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkChineseName(String value, int length) {
	return value.matches("^[u4e00-u9fa5]+$")&&value.length()<=length;
	}

	/**
	 * ���HTML ����β���л�ո�
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkBlank(String value){
	return value.matches("^\\s*|\\s*$");
	}

	/**
	 * ����ַ����� ����HTML��ǩ
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkHtmlTag(String value){
	return value.matches("<(\\S*?)[^>]*>.*?</\1>|<.*? />");
	}

	/**
	 * ���URL�� ��Ϸ�
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkURL(String value){
	return value.matches("[a-zA-Z]+://[^\\s]*");
	}

	/**
	 * ���IP�Ƿ� �Ϸ�
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkIP(String value){
	return value.matches("\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}");
	}

	/**
	 * ���ID�Ƿ� �Ϸ�����ͷ�����Ǵ�Сд��ĸ������λ�����д�Сд�ַ������֡��»���
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkID(String value){
	return value.matches("[a-zA-Z][a-zA-Z0-9_]{4,15}$");
	}

	/**
	 * ���QQ�Ƿ� �Ϸ������������֣�����λ����Ϊ0���15λ
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkQQ(String value){
	return value.matches("[1-9][0-9]{4,13}");
	}

	/**
	 * ����ʱ��Ƿ� �Ϸ�
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkPostCode(String value){
	return value.matches("[1-9]\\d{5}(?!\\d)");
	}

	/**
	 * ������֤�� ��Ϸ�,15λ��18λ
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkIDCard(String value){
	return value.matches("\\d{15}|\\d{18}");
	}

}
