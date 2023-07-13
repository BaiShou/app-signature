package com.arnold.channel.ui;

import com.arnold.NotificationUtil;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;

public class SelectApkChannelDialogView extends JDialog {
    private JPanel contentPane;
    private JButton cancel;
    private JButton submit;
    private JTextField etApkPath;
    private JButton btnSelect;
    private DialogCallback callback;

    public SelectApkChannelDialogView() {
        this(null);
    }

    public SelectApkChannelDialogView(DialogCallback callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submit);
        setTitle("选择Apk进行渠道打包");
        setLocation(500, 200);//距离屏幕左上角的其实位置
        setSize(500, 200);//对话框的长宽

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        btnSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileAndSetPath();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void openFileAndSetPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(".apk", "apk"));

        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedApk = fileChooser.getSelectedFile();
            if (selectedApk != null) {
                etApkPath.setText(selectedApk.getAbsolutePath());
            }
        }


    }

    private void onOK() {
        String apkPath = etApkPath.getText();

        if (StringUtil.isEmpty(apkPath)) {
            NotificationUtil.warning("请选择APK");
            return;
        }

        if (callback != null) {
            callback.onOkBtnClicked(apkPath);
        }
        dispose();
    }

    private void onCancel() {
        if (callback != null) {
            callback.onCancelBtnClicked();
        }
        dispose();
    }

    public static void main(String[] args) {
        SelectApkChannelDialogView dialog = new SelectApkChannelDialogView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public interface DialogCallback {
        void onOkBtnClicked(String apkPath);

        void onCancelBtnClicked();
    }
}
