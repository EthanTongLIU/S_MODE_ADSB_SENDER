package util;

import java.io.FileReader;
import java.io.IOException;

/**
 * 文件读取器，可以实现得到行的数量和指定的一行
 * 
 * @author 刘通
 *
 */
public class BufferReader {

	private FileReader fb;

	BufferReader(FileReader fb) {
		this.fb = fb;
	}

	public String BufferReader() throws IOException {
		StringBuilder s1 = new StringBuilder();
		int ch = 0;
		while ((ch = fb.read()) != -1) {
			if (ch == '\r') {
				continue;
			}
			if (ch == '\n') {
				return s1.toString();
			} else {
				s1.append((char) ch);
			}
		}

		if (s1.length() != 0) {
			return s1.toString();
		}

		return null;
	}

	public void myClose() throws IOException {
		fb.close();
	}

	/**
	 * 得到一条指定的行，注意行的索引从0开始
	 * 
	 * @param fileName
	 *            文件名
	 * @param lineNo
	 *            得到指定的一行
	 * @return 返回一条报文
	 * @throws IOException
	 */
	public static String getOneLine(String fileName, int lineNo) throws IOException {
		String line = null;
		FileReader f1 = new FileReader(fileName);
		BufferReader myb = new BufferReader(f1);
		for (int i = 0; i < lineNo; i++) {
			myb.BufferReader();
		}
		line = myb.BufferReader();
		myb.myClose();
		return line;
	}

	/**
	 * 得到该文件中行的数目
	 * 
	 * @param fileName
	 * @return 返回行数量
	 * @throws IOException
	 */
	public static int getLineNum(String fileName) throws IOException {
		int i = 0;
		FileReader f1 = new FileReader(fileName);
		BufferReader myb = new BufferReader(f1);
		while ((myb.BufferReader()) != null) {
			i += 1;
		}
		return i - 1;
	}

}
