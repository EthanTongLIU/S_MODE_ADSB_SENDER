package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import ui.UI;
import util.BufferReader;
import util.Filter;

public class Task extends TimerTask {

	// 实现暂停/继续操作
	public static boolean suspended = false;
	// 记录发送的次数
	private static int counter;
	// 记录发送的上一个值
	private static int iStart = UDPSender.i;
	// 第一条报文的时间
	public static int startTime;
	// 目标IP
	private static String IP;
	// 目标端口
	private static int port;
	// 发送间隔
	private static int interval;
	// 列表
	private static Table table;

	// table的setter
	public static void setIable(Table table1) {
		table = table1;
	}

	// ip的setter
	public static void setIP(String ip) {
		IP = ip;
	}

	// port的setter
	public static void setPort(int targetPort) {
		port = targetPort;
	}

	// interval的setter
	public static void setInterval(int INTERVAL) {
		interval = INTERVAL;
	}

	public void run() {
		try {

			int timeEnd = counter * interval + startTime; // 设置报文发送截止时间
			int i = iStart; // 从第几条开始发送

			// 将数据包信息添加到列表中
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					UI.list.setItem(7, "\u2708 已发送" + counter + "个数据包");
					UI.list.setItem(9, "\u2708 已发送" + iStart + "条报文");
				}
			});

			// 创建socket
			DatagramSocket socket = new DatagramSocket();

			while (true) {

				// 中断线程控制
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("解码线程已被终止！");
					socket.close();
					break;
				}
				// 暂停/继续线程控制
				synchronized (this) {
					while (suspended) {
						wait();
					}
				}

				String line = BufferReader.getOneLine(UI.getDataFileName(), i);

				// 处理行
				Filter.setTimeAndFrame(line);
				String time = Filter.getTime();
				String frame = Filter.getFrame();

				// 不输出空行
				if ("".equals(time) != true) {
					// 如果报文的时间为"non"，直接输出
					if ("non".equals(time)) {
						System.out.println(frame);
						// **发送**
						DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
								InetAddress.getByName(IP), port);
						socket.send(packet);

						// 显示到表格中
						Display.getDefault().syncExec(new Runnable() {
							public void run() {

								// 表格更新，每500条更新
								if (table.getItemCount() > 500) {
									table.removeAll();
								}

								// 创建Item，并添加到表格中
								TableItem item = new TableItem(table, SWT.NONE);
								item.setText(new String[] { time, frame });

								// 表格相邻两行加颜色以示区分
								if ((table.getItemCount() & 1) != 0) {
									item.setBackground(new Color(null, 185, 227, 217));
								} else {
									item.setBackground(new Color(null, 174, 221, 129));
								}

							}
						});

					}
					// 如果报文时间有更新，则判断其是否应该输出
					else {
						// 对时间进行数值化处理
						if (setTime(time) < timeEnd) {
							System.out.println("捕捉到未超出发送范围的时刻：" + time);
							System.out.println(frame);
							// **发送**
							DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
									InetAddress.getByName(IP), port);
							socket.send(packet);

							// 显示到表格中
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									TableItem item = new TableItem(table, SWT.NONE);
									item.setText(new String[] { time, frame });
									if ((table.getItemCount() & 1) != 0) {
										item.setBackground(new Color(null, 185, 227, 217));
									} else {
										item.setBackground(new Color(null, 174, 221, 129));
									}
								}
							});

						} else {
							break;
						}
					}
				}
				i++;
			}

			iStart = i;
			System.out.println("已发送以上报文，以上是第" + counter + "次发射");
			counter++;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将时间从字符串转换为整形
	 * 
	 * @param time
	 * @return
	 */

	public static int setTime(String time) {

		return Integer.parseInt(time.substring(11, 13)) * 60 * 60 * 1000
				+ Integer.parseInt(time.substring(14, 16)) * 60 * 1000 + Integer.parseInt(time.substring(17, 19)) * 1000
				+ Integer.parseInt(time.substring(20, 23));

	}

	/**
	 * 暂停
	 */
	public void suspend() {
		suspended = true;
	}

	/**
	 * 继续
	 */
	public synchronized void resume() {
		suspended = false;
		notify();
	}

}
