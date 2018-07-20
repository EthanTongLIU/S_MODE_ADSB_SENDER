package util;

/**
 * �����У��õ�ʱ��ͱ��ģ���ÿ�����Ĺ�����ض��Ĵ���ʱ���ǵ��±���
 * 
 * @author ��ͨ
 *
 */
public class Filter {

	private static String time; // ���Ľ���ʱ�䣬���û�У�����"non"
	private static String frame; // �����ı�����Ϣ

	public static String getTime() {
		return time;
	}

	public static String getFrame() {
		return frame;
	}

	public static void setTimeAndFrame(String line) {

		if ("".equals(line) || (line.contains(";") != true)) { // ���Ϊ���л��߲�����;���У���������ַ���
			time = "";
			frame = "";
		} else {
			// ���ʱ����Ϣ����
			// �������ʱ����Ϣ����ʱ����ȡ���������
			if (line.contains("��")) {
				time = line.substring(line.indexOf("��") + 1, line.indexOf("��"));
			} else {
				time = "non";
			}

			// ��ȡ����������Ϣ
			// ��������в�����*����frame��ֵΪ""
			if (line.contains("*") != true) {
				frame = "";
			} else {
				frame = line.substring(line.indexOf("*") + 1, line.indexOf(";"));
			}

		}

	}

}
