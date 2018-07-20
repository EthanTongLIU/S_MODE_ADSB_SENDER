package util;

import java.io.FileReader;
import java.io.IOException;

/**
 * �ļ���ȡ��������ʵ�ֵõ��е�������ָ����һ��
 * 
 * @author ��ͨ
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
	 * �õ�һ��ָ�����У�ע���е�������0��ʼ
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param lineNo
	 *            �õ�ָ����һ��
	 * @return ����һ������
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
	 * �õ����ļ����е���Ŀ
	 * 
	 * @param fileName
	 * @return ����������
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
