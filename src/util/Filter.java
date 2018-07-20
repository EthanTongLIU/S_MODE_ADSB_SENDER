package util;

/**
 * 处理行，得到时间和报文，将每条报文构造成特定的带有时间标记的新报文
 * 
 * @author 刘通
 *
 */
public class Filter {

	private static String time; // 报文接收时间，如果没有，返回"non"
	private static String frame; // 纯净的报文信息

	public static String getTime() {
		return time;
	}

	public static String getFrame() {
		return frame;
	}

	public static void setTimeAndFrame(String line) {

		if ("".equals(line) || (line.contains(";") != true)) { // 如果为空行或者不含有;的行，则输出空字符串
			time = "";
			frame = "";
		} else {
			// 检测时间信息特征
			// 如果含有时间信息，则将时间提取出来并输出
			if (line.contains("【")) {
				time = line.substring(line.indexOf("【") + 1, line.indexOf("】"));
			} else {
				time = "non";
			}

			// 提取纯净报文信息
			// 如果该行中不含有*，则将frame赋值为""
			if (line.contains("*") != true) {
				frame = "";
			} else {
				frame = line.substring(line.indexOf("*") + 1, line.indexOf(";"));
			}

		}

	}

}
