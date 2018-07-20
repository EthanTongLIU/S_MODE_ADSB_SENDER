package ui;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import sender.Task;
import sender.UDPSender;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TableColumn;

public class UI {

	protected Shell shlDf;

	// 报文文件选择对话框
	private static FileDialog fileDialog;

	// 本地报文文件路径
	private static String dataFileName;

	// dataFileName的getter
	public static String getDataFileName() {
		return dataFileName;
	}

	// 系统信息显示列表
	public static List list;

	// 正在发送表格
	public static Table table;

	// 目标IP文本框
	public static Text text;
	// 目标IP
	public static String receiveIP = null;

	// 目标端口文本框
	public static Text text_1;
	// 目标端口
	public static String receivePort = null;

	// 模拟发送间隔
	public static Text text_2;

	// 本地IP
	public static String localIP = null;

	// receiveIP的getter
	public static String getIP() {
		return text.getText();
	}

	// port的getter
	public static int getPort() {
		return Integer.parseInt(text_1.getText());
	}

	// time的getter
	public static double getTime() {
		return Double.parseDouble(text_2.getText());
	}

	// 系统图标路径
	private static String systemLogoPath = System.getProperty("user.dir") + "\\src\\Img\\icon_1.ico";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			UI window = new UI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlDf.open();
		shlDf.layout();
		while (!shlDf.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlDf = new Shell(SWT.MIN | SWT.CLOSE);
		String path = systemLogoPath;
		shlDf.setImage(SWTResourceManager.getImage(path));
		shlDf.setSize(1032, 851);
		shlDf.setText("DF17\u62A5\u6587\u53D1\u5C04\u673A\u6A21\u62DF\u5668");

		Composite composite = new Composite(shlDf, SWT.BORDER);
		composite.setBounds(0, 34, 1024, 62);

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {

					// 设置发送间隔
					UDPSender.setInterval((int) UI.getTime());

					// 设置发送间隔
					Task.setInterval((int) UI.getTime());

					// 设置IP
					receiveIP = text.getText();
					Task.setIP(receiveIP);
					
					// 更新list
					list.setItem(3, "\u2708 接收方IP: " + receiveIP);

					// 设置port
					receivePort = text_1.getText();
					Task.setPort(Integer.valueOf(receivePort));

					// 更新list
					list.setItem(5, "\u2708 接收端口: " + receivePort);
					
					// 设置table
					Task.setIable(UI.table);

					// 开始发送
					UDPSender.launch();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(0, 0, 58, 58);
		button.setText("\u5F00\u59CB");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UDPSender.task.suspend();
			}
		});
		button_1.setBounds(64, 0, 58, 58);
		button_1.setText("\u6682\u505C");

		Canvas canvas = new Canvas(composite, SWT.NONE);
		canvas.setBounds(952, 0, 58, 58);

		Button button_3 = new Button(composite, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				UDPSender.task.resume();
			}
		});
		button_3.setBounds(128, 0, 58, 58);
		button_3.setText("\u7EE7\u7EED");

		Button button_4 = new Button(composite, SWT.NONE);
		button_4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				table.removeAll();
			}
		});
		button_4.setBounds(192, 0, 58, 58);
		button_4.setText("\u6E05\u7A7A");
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				String path1 = systemLogoPath;
				Image icon = new Image(null, path1);
				e.gc.drawImage(icon, 0, 0, icon.getBounds().width, icon.getBounds().height, 0, 0, 58, 58);
				e.gc.dispose();
				icon.dispose();
			}
		});

		ToolBar toolBar = new ToolBar(shlDf, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		toolBar.setBounds(0, 0, 1024, 28);

		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setText("\u5173\u4E8E");

		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.setText("\u5E2E\u52A9");

		Group group = new Group(shlDf, SWT.NONE);
		group.setText("\u6B63\u5728\u53D1\u9001");
		group.setBounds(10, 102, 705, 692);

		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(10, 23, 685, 659);

		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(254);
		tableColumn_1.setText("\u65F6\u95F4");

		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setText("\u62A5\u6587");
		tableColumn.setWidth(420);

		Group group_1 = new Group(shlDf, SWT.NONE);
		group_1.setText("\u7F51\u7EDC\u8BBE\u7F6E");
		group_1.setBounds(721, 218, 281, 126);

		Label lblip = new Label(group_1, SWT.NONE);
		lblip.setBounds(27, 36, 66, 20);
		lblip.setText("\u76EE\u6807IP\uFF1A");

		text = new Text(group_1, SWT.BORDER);
		try {
			text.setText(InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		text.setBounds(99, 33, 144, 26);

		Label label = new Label(group_1, SWT.NONE);
		label.setBounds(27, 80, 66, 20);
		label.setText("\u7AEF\u53E3\uFF1A");

		text_1 = new Text(group_1, SWT.BORDER);
		text_1.setText("8888");
		text_1.setBounds(99, 77, 144, 26);

		Group group_2 = new Group(shlDf, SWT.NONE);
		group_2.setText("\u53D1\u9001\u95F4\u9694");
		group_2.setBounds(721, 350, 281, 78);

		text_2 = new Text(group_2, SWT.BORDER);
		text_2.setText("2000");
		text_2.setBounds(136, 30, 89, 26);

		Label label_1 = new Label(group_2, SWT.NONE);
		label_1.setBounds(25, 33, 105, 20);
		label_1.setText("\u6A21\u62DF\u53D1\u9001\u95F4\u9694\uFF1A");

		Label lblMs = new Label(group_2, SWT.NONE);
		lblMs.setBounds(231, 33, 40, 20);
		lblMs.setText("ms");

		Group group_3 = new Group(shlDf, SWT.NONE);
		group_3.setText("\u7CFB\u7EDF\u4FE1\u606F");
		group_3.setBounds(721, 434, 281, 360);

		list = new List(group_3, SWT.BORDER);
		list.setBounds(10, 22, 261, 328);
		receiveIP = text.getText();
		receivePort = text_1.getText();
		try {
			localIP = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		list.add("+++++++++++++++++++++++++++++++++++++++++++");
		list.add("\u2708 发送方IP: " + localIP);
		list.add("+++++++++++++++++++++++++++++++++++++++++++");
		list.add("\u2708 接收方IP: " + receiveIP);
		list.add("+++++++++++++++++++++++++++++++++++++++++++");
		list.add("\u2708 接收端口: " + receivePort);
		list.add("+++++++++++++++++++++++++++++++++++++++++++");
		list.add("\u2708 已发送0个数据包");
		list.add("+++++++++++++++++++++++++++++++++++++++++++");
		list.add("\u2708 已发送0条报文");
		list.add("+++++++++++++++++++++++++++++++++++++++++++");

		Group group_4 = new Group(shlDf, SWT.NONE);
		group_4.setText("\u6DFB\u52A0\u6587\u4EF6");
		group_4.setBounds(721, 102, 281, 110);

		Button button_5 = new Button(group_4, SWT.NONE);
		button_5.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				fileDialog = new FileDialog(shlDf, SWT.OPEN | SWT.MULTI);
				fileDialog.setFilterPath("C:/");
				fileDialog.setFilterNames(new String[] { "txt文件(*.txt)" });
				fileDialog.setFilterExtensions(new String[] { "*.txt" });
				fileDialog.setText("选择报文文件");
				dataFileName = fileDialog.open();
			}
		});
		button_5.setBounds(65, 45, 152, 30);
		button_5.setText("\u5BFC\u5165\u539F\u59CB\u62A5\u6587\u6587\u4EF6");

		// 在关闭窗口时关闭所有线程
		shlDf.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) {
				System.exit(0);
			}
		});

	}
}
