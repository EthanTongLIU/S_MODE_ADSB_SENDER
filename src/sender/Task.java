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

	// ʵ����ͣ/��������
	public static boolean suspended = false;
	// ��¼���͵Ĵ���
	private static int counter;
	// ��¼���͵���һ��ֵ
	private static int iStart = UDPSender.i;
	// ��һ�����ĵ�ʱ��
	public static int startTime;
	// Ŀ��IP
	private static String IP;
	// Ŀ��˿�
	private static int port;
	// ���ͼ��
	private static int interval;
	// �б�
	private static Table table;

	// table��setter
	public static void setIable(Table table1) {
		table = table1;
	}

	// ip��setter
	public static void setIP(String ip) {
		IP = ip;
	}

	// port��setter
	public static void setPort(int targetPort) {
		port = targetPort;
	}

	// interval��setter
	public static void setInterval(int INTERVAL) {
		interval = INTERVAL;
	}

	public void run() {
		try {

			int timeEnd = counter * interval + startTime; // ���ñ��ķ��ͽ�ֹʱ��
			int i = iStart; // �ӵڼ�����ʼ����

			// �����ݰ���Ϣ��ӵ��б���
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					UI.list.setItem(7, "\u2708 �ѷ���" + counter + "�����ݰ�");
					UI.list.setItem(9, "\u2708 �ѷ���" + iStart + "������");
				}
			});

			// ����socket
			DatagramSocket socket = new DatagramSocket();

			while (true) {

				// �ж��߳̿���
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("�����߳��ѱ���ֹ��");
					socket.close();
					break;
				}
				// ��ͣ/�����߳̿���
				synchronized (this) {
					while (suspended) {
						wait();
					}
				}

				String line = BufferReader.getOneLine(UI.getDataFileName(), i);

				// ������
				Filter.setTimeAndFrame(line);
				String time = Filter.getTime();
				String frame = Filter.getFrame();

				// ���������
				if ("".equals(time) != true) {
					// ������ĵ�ʱ��Ϊ"non"��ֱ�����
					if ("non".equals(time)) {
						System.out.println(frame);
						// **����**
						DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
								InetAddress.getByName(IP), port);
						socket.send(packet);

						// ��ʾ�������
						Display.getDefault().syncExec(new Runnable() {
							public void run() {

								// �����£�ÿ500������
								if (table.getItemCount() > 500) {
									table.removeAll();
								}

								// ����Item������ӵ������
								TableItem item = new TableItem(table, SWT.NONE);
								item.setText(new String[] { time, frame });

								// ����������м���ɫ��ʾ����
								if ((table.getItemCount() & 1) != 0) {
									item.setBackground(new Color(null, 185, 227, 217));
								} else {
									item.setBackground(new Color(null, 174, 221, 129));
								}

							}
						});

					}
					// �������ʱ���и��£����ж����Ƿ�Ӧ�����
					else {
						// ��ʱ�������ֵ������
						if (setTime(time) < timeEnd) {
							System.out.println("��׽��δ�������ͷ�Χ��ʱ�̣�" + time);
							System.out.println(frame);
							// **����**
							DatagramPacket packet = new DatagramPacket(frame.getBytes(), frame.getBytes().length,
									InetAddress.getByName(IP), port);
							socket.send(packet);

							// ��ʾ�������
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
			System.out.println("�ѷ������ϱ��ģ������ǵ�" + counter + "�η���");
			counter++;

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʱ����ַ���ת��Ϊ����
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
	 * ��ͣ
	 */
	public void suspend() {
		suspended = true;
	}

	/**
	 * ����
	 */
	public synchronized void resume() {
		suspended = false;
		notify();
	}

}
