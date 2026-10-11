package com.ami.iusb;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JListDialog extends JDialog implements ActionListener, ListSelectionListener {
    private static JListDialog dialog;
    private static JTextField manualTextField;
    private static Object selection;
    private static boolean useManualField;
    private JList list;

    public JListDialog(Frame frame, Component component, String str, String str2, String str3, Object[] objArr, Object obj) {
        super(frame, str2, true);
        this.list = new JList(objArr);
        this.list.setSelectionMode(0);
        this.list.setLayoutOrientation(0);
        JButton jButton = new JButton("Cancel");
        jButton.setMnemonic('C');
        jButton.getAccessibleContext().setAccessibleDescription("Cancel this selection operation and close the dialog");
        jButton.addActionListener(this);
        JButton jButton2 = new JButton("OK");
        jButton2.setMnemonic('O');
        jButton2.getAccessibleContext().setAccessibleDescription("Approve the current selection and close the dialog");
        jButton2.addActionListener(this);
        jButton2.setActionCommand("OK");
        getRootPane().setDefaultButton(jButton2);
        JScrollPane jScrollPane = new JScrollPane(this.list);
        JLabel jLabel = new JLabel(str3);
        manualTextField = new JTextField((String) obj);
        manualTextField.getAccessibleContext().setAccessibleDescription("Manually enter a selection for this dialog");
        jLabel.setLabelFor(manualTextField);
        this.list.addListSelectionListener(this);
        jScrollPane.setPreferredSize(new Dimension(200, 200));
        jScrollPane.setAlignmentX(0.0f);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 3));
        JLabel jLabel2 = new JLabel(str);
        jLabel2.setLabelFor(this.list);
        jPanel.add(jLabel2);
        jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        jPanel.add(jScrollPane);
        if (str3 != null) {
            jPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            jPanel.add(jLabel);
            jPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            jPanel.add(manualTextField);
            useManualField = true;
        } else {
            useManualField = false;
        }
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 2));
        jPanel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel2.add(Box.createHorizontalGlue());
        jPanel2.add(jButton2);
        jPanel2.add(Box.createRigidArea(new Dimension(10, 0)));
        jPanel2.add(jButton);
        Container contentPane = getContentPane();
        contentPane.add(jPanel, "Center");
        contentPane.add(jPanel2, "Last");
        if (obj != null) {
            setSelection(obj);
        } else {
            setSelection(objArr[0]);
        }
        pack();
        setLocationRelativeTo(component);
    }

    public static Object showDialog(Component component, Component component2, String str, String str2, Object[] objArr, Object obj) {
        dialog = new JListDialog(JOptionPane.getFrameForComponent(component), component2, str, str2, null, objArr, obj);
        dialog.getAccessibleContext().setAccessibleName(str2 + " list dialog");
        dialog.getAccessibleContext().setAccessibleDescription(str);
        dialog.setVisible(true);
        return selection;
    }

    public static Object showDialog(Component component, Component component2, String str, String str2, String str3, Object[] objArr, Object obj) {
        dialog = new JListDialog(JOptionPane.getFrameForComponent(component), component2, str, str2, str3, objArr, obj);
        dialog.getAccessibleContext().setAccessibleName(str2 + " list dialog");
        dialog.getAccessibleContext().setAccessibleDescription(str);
        dialog.setVisible(true);
        return selection;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (!actionEvent.getActionCommand().equals("OK")) {
            selection = null;
        } else if (useManualField) {
            setSelection(manualTextField.getText());
        } else {
            setSelection(this.list.getSelectedValue());
        }
        setVisible(false);
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        manualTextField.setText((String) this.list.getSelectedValue());
    }

    public void setSelection(Object obj) {
        selection = obj;
        this.list.setSelectedValue(selection, true);
    }
}
