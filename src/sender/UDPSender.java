package sender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ui.UI;
import util.BufferReader;
import util.Filter;

/**
 * UDP��ʽ���ķ���������ʱ�䷢��
 * 
 * @author ��ͨ
 */
public class UDPSender {

	// ��ʱ��
	public static Timer timer;

	// ��������
	public static Task task;

	// ���ͼ��������ִ�м��
	private static int interval;

	// interval��setter
	public static void setInterval(int INTERVAL) {
		interval = INTERVAL;
	}

	// ��һ��ʱ����֮ǰ�ı�������
	public static int i = 0;

	// ����UDPSender
	public static void launch() throws IOException {

		// �ļ��е�һ��ʱ����
		String startTime0 = null;

		/*
		 * ��ȡ��һ�����ĵ�ʱ�䣬����һ��ʱ����֮ǰ�ı���ȫ�����ͳ�ȥ
		 */

		// �����ļ������
		FileInputStream fis = new FileInputStream(UI.getDataFileName());
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		// ����socket
		DatagramSocket socket = new DatagramSocket();

		while (true) {
			String line = br.readLine();
			Filter.setTimeAndFrame(line);
			String time = Filter.getTime();
			String frame = Filter.getFrame();

			// �������ʱ����Ϣ�����˳�����
			if (("".equals(time) != true) && ("non".equals(time) != true)) {
				startTime0 = time;
				break;
			}

			// �����Ϳ���
			if ("".equals(time) != true) {
				DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
						InetAddress.getByName(UI.receiveIP), Integer.parseInt(UI.receivePort));
				socket.send(packet);
			}

			i++;

		}

		Task.startTime = Task.setTime(startTime0); // ����ʼʱ��ת��Ϊ����

		timer = new Timer();
		task = new Task();
		timer.schedule(task, new Date(), interval);

	}

}
