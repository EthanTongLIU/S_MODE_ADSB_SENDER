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
 * UDP格式报文发送器，按时间发送
 * 
 * @author 刘通
 */
public class UDPSender {

	// 定时器
	public static Timer timer;

	// 发送任务
	public static Task task;

	// 发送间隔，任务执行间隔
	private static int interval;

	// interval的setter
	public static void setInterval(int INTERVAL) {
		interval = INTERVAL;
	}

	// 第一个时间标记之前的报文数量
	public static int i = 0;

	// 启动UDPSender
	public static void launch() throws IOException {

		// 文件中第一个时间标记
		String startTime0 = null;

		/*
		 * 获取第一条报文的时间，将第一个时间标记之前的报文全部发送出去
		 */

		// 创建文件输出流
		FileInputStream fis = new FileInputStream(UI.getDataFileName());
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader br = new BufferedReader(isr);

		// 创建socket
		DatagramSocket socket = new DatagramSocket();

		while (true) {
			String line = br.readLine();
			Filter.setTimeAndFrame(line);
			String time = Filter.getTime();
			String frame = Filter.getFrame();

			// 如果读到时间信息，则退出发送
			if (("".equals(time) != true) && ("non".equals(time) != true)) {
				startTime0 = time;
				break;
			}

			// 不发送空行
			if ("".equals(time) != true) {
				DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
						InetAddress.getByName(UI.receiveIP), Integer.parseInt(UI.receivePort));
				socket.send(packet);
			}

			i++;

		}

		Task.startTime = Task.setTime(startTime0); // 将初始时间转化为整形

		timer = new Timer();
		task = new Task();
		timer.schedule(task, new Date(), interval);

	}

}
