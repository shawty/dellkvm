package com.ami.kvm.jviewer.gui;

import com.ami.kvm.jviewer.kvmpkts.IVTPPktHdr;
import com.ami.kvm.jviewer.kvmpkts.VideoEngineConfigs;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VideoEngineConfigsDlg extends JDialog implements ActionListener {
    private JPanel DialogPanel = null;
    private JButton bCancel = null;
    private JButton bOk = null;
    private JComboBox chkcompression_mode = null;
    private JCheckBox chkdifferential_setting = null;
    private JCheckBox chkdisable_hw_cursor = null;
    private JCheckBox chksharp_mode_selection = null;
    private JComboBox cmbdct_quant_quality = null;
    private JComboBox cmbdct_quant_tbl_select = null;
    private JComboBox cmbsharp_quant_quality = null;
    private JComboBox cmbsharp_quant_tbl_select = null;
    private JDialog dlgVideoEngineConfig = null;
    private JLabel lblcompression_mode = null;
    private JLabel lbldct_quant_quality = null;
    private JLabel lbldct_quant_tbl_select = null;
    private JLabel lbldifferential_setting = null;
    private JLabel lblsdisable_hw_cursor = null;
    private JLabel lblsharp_mode_selection = null;
    private JLabel lblsharp_quant_quality = null;
    private JLabel lblsharp_quant_tbl_select = null;
    private VideoEngineConfigs m_videngcfgs;
    private JPanel panelButtons = null;
    private JPanel panelSettings = null;

    public VideoEngineConfigsDlg() {
        System.out.println("inside OnOptionsVideoSettings");
        JViewerApp.getInstance().getKVMClient().getVideoEngineConfigs();
    }

    public void setVideoConfigs(VideoEngineConfigs videoEngineConfigs) {
        this.m_videngcfgs = videoEngineConfigs;
        System.out.println("vidcfgs." + ((int) this.m_videngcfgs.compression_mode));
        initialize();
    }

    private JButton getbCancel() {
        if (this.bCancel == null) {
            try {
                this.bCancel = new JButton();
                this.bCancel.setName("bCancel");
                this.bCancel.setMnemonic('c');
                this.bCancel.setText("Cancel");
                this.bCancel.setBounds(230, 10, 85, 25);
                this.bCancel.addActionListener(this);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.bOk;
    }

    private JButton getbOk() {
        if (this.bOk == null) {
            try {
                this.bOk = new JButton();
                this.bOk.setName("bOk");
                this.bOk.setMnemonic('o');
                this.bOk.setText("Ok");
                this.bOk.setBounds(80, 10, 85, 25);
                this.bOk.addActionListener(this);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.bOk;
    }

    private JCheckBox getchkdisable_hw_cursor() {
        if (this.chkdisable_hw_cursor == null) {
            try {
                this.chkdisable_hw_cursor = new JCheckBox();
                this.chkdisable_hw_cursor.setName("chkdisable_hw_cursor");
                this.chkdisable_hw_cursor.setBounds(273, 25, 130, 23);
                if (this.m_videngcfgs.disable_hw_cursor == 1) {
                    this.chkdisable_hw_cursor.setSelected(true);
                } else {
                    this.chkdisable_hw_cursor.setSelected(false);
                }
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.chkdisable_hw_cursor;
    }

    private JCheckBox getchkdifferential_setting() {
        if (this.chkdifferential_setting == null) {
            try {
                this.chkdifferential_setting = new JCheckBox();
                this.chkdifferential_setting.setName("chkdifferential_setting");
                this.chkdifferential_setting.setBounds(273, 63, 130, 23);
                if (this.m_videngcfgs.differential_setting == 1) {
                    this.chkdifferential_setting.setSelected(true);
                } else {
                    this.chkdifferential_setting.setSelected(false);
                }
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.chkdifferential_setting;
    }

    private JComboBox getchkcompression_mode() {
        if (this.chkcompression_mode == null) {
            try {
                this.chkcompression_mode = new JComboBox();
                this.chkcompression_mode.setName("Compression Mode");
                this.chkcompression_mode.setBounds(273, 100, 130, 23);
                this.chkcompression_mode.addItem("YUV 420");
                this.chkcompression_mode.addItem("YUV 444_JPEG");
                this.chkcompression_mode.addItem("YUV 444_2_CLRVQ");
                this.chkcompression_mode.addItem("YUV 444_4_CLRVQ");
                this.chkcompression_mode.setSelectedIndex(this.m_videngcfgs.compression_mode);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.chkcompression_mode;
    }

    private JComboBox getcmbdct_quant_quality() {
        if (this.cmbdct_quant_quality == null) {
            try {
                this.cmbdct_quant_quality = new JComboBox();
                this.cmbdct_quant_quality.setName("cmbdct_quant_quality");
                this.cmbdct_quant_quality.setBounds(273, 139, 130, 23);
                this.cmbdct_quant_quality.addItem("1 Worst Quality");
                for (int i = 2; i < 30; i++) {
                    if (i == 16) {
                        this.cmbdct_quant_quality.addItem("16 Best Quality");
                    } else {
                        this.cmbdct_quant_quality.addItem(new Integer(i));
                    }
                }
                this.cmbdct_quant_quality.addItem("30 Worst Quality");
                this.cmbdct_quant_quality.setSelectedIndex(this.m_videngcfgs.dct_quant_quality - 1);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.cmbdct_quant_quality;
    }

    private JComboBox getcmbdct_quant_tbl_select() {
        if (this.cmbdct_quant_tbl_select == null) {
            try {
                this.cmbdct_quant_tbl_select = new JComboBox();
                this.cmbdct_quant_tbl_select.setName("cmbdct_quant_tbl_select");
                this.cmbdct_quant_tbl_select.setBounds(273, 186, 130, 23);
                this.cmbdct_quant_tbl_select.addItem("0 Worst Quality");
                for (int i = 1; i < 7; i++) {
                    this.cmbdct_quant_tbl_select.addItem(new Integer(i));
                }
                this.cmbdct_quant_tbl_select.addItem("7 Best Quality");
                this.cmbdct_quant_tbl_select.setSelectedIndex(this.m_videngcfgs.dct_quant_tbl_select);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.cmbdct_quant_tbl_select;
    }

    private JComboBox getcmbsharp_quant_quality() {
        if (this.cmbsharp_quant_quality == null) {
            try {
                this.cmbsharp_quant_quality = new JComboBox();
                this.cmbsharp_quant_quality.setName("cmbsharp_quant_quality");
                this.cmbsharp_quant_quality.setBounds(273, 230, 130, 23);
                this.cmbsharp_quant_quality.addItem("1 Worst Quality");
                for (int i = 2; i < 30; i++) {
                    if (i == 16) {
                        this.cmbsharp_quant_quality.addItem("16 Best Quality");
                    } else {
                        this.cmbsharp_quant_quality.addItem(new Integer(i));
                    }
                }
                this.cmbsharp_quant_quality.addItem("30 Worst Quality");
                this.cmbsharp_quant_quality.setSelectedIndex(this.m_videngcfgs.sharp_quant_quality - 1);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.cmbsharp_quant_quality;
    }

    private JComboBox getcmbsharp_quant_tbl_select() {
        if (this.cmbsharp_quant_tbl_select == null) {
            try {
                this.cmbsharp_quant_tbl_select = new JComboBox();
                this.cmbsharp_quant_tbl_select.setName("cmbsharp_quant_tbl_select");
                this.cmbsharp_quant_tbl_select.setBounds(273, 270, 130, 23);
                this.cmbsharp_quant_tbl_select.addItem("0 Worst Quality");
                for (int i = 1; i < 7; i++) {
                    this.cmbsharp_quant_tbl_select.addItem(new Integer(i));
                }
                this.cmbsharp_quant_tbl_select.addItem("7 Best Quality");
                this.cmbsharp_quant_tbl_select.setSelectedIndex(this.m_videngcfgs.sharp_quant_tbl_select);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.cmbsharp_quant_tbl_select;
    }

    private JDialog getdlgVideoEngineConfig() {
        if (this.dlgVideoEngineConfig == null) {
            try {
                setName("this");
                setDefaultCloseOperation(2);
                setTitle("Video Engine Configuration");
                setModal(true);
                setBounds(77, 62, 450, 400);
                setContentPane(this.DialogPanel);
                setLocationRelativeTo(null);
                setVisible(true);
                setResizable(false);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.dlgVideoEngineConfig;
    }

    private JPanel getDialogPanel() {
        if (this.DialogPanel == null) {
            try {
                this.DialogPanel = new JPanel();
                this.DialogPanel.setName("DialogPanel");
                this.DialogPanel.setLayout((LayoutManager) null);
                this.DialogPanel.setBorder(BorderFactory.createEtchedBorder(1, Color.GRAY, Color.GRAY));
                this.DialogPanel.add(this.panelSettings, this.panelSettings.getName());
                this.DialogPanel.add(this.panelButtons, this.panelButtons.getName());
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.DialogPanel;
    }

    private JLabel getlblsdisable_hw_cursor() {
        if (this.lblsdisable_hw_cursor == null) {
            try {
                this.lblsdisable_hw_cursor = new JLabel();
                this.lblsdisable_hw_cursor.setName("lblsdisable_hw_cursor");
                this.lblsdisable_hw_cursor.setText("Hide Hardware Cursor :");
                this.lblsdisable_hw_cursor.setBounds(26, 29, 200, 20);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lblsdisable_hw_cursor;
    }

    private JLabel getlbldifferential_setting() {
        if (this.lbldifferential_setting == null) {
            try {
                this.lbldifferential_setting = new JLabel();
                this.lbldifferential_setting.setName("lblsharp_mode_selection");
                this.lbldifferential_setting.setText("Transmit Video deltas Only :");
                this.lbldifferential_setting.setBounds(26, 68, 200, 14);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lbldifferential_setting;
    }

    private JLabel getlblcompression_mode() {
        if (this.lblcompression_mode == null) {
            try {
                this.lblcompression_mode = new JLabel();
                this.lblcompression_mode.setName("lblcompression_mode");
                this.lblcompression_mode.setText("Compression Mode :");
                this.lblcompression_mode.setBounds(26, 107, 200, 14);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lblcompression_mode;
    }

    private JLabel getlbldct_quant_quality() {
        if (this.lbldct_quant_quality == null) {
            try {
                this.lbldct_quant_quality = new JLabel();
                this.lbldct_quant_quality.setName("lbldct_quant_quality");
                this.lbldct_quant_quality.setText("DCT Quant Quality :");
                this.lbldct_quant_quality.setBounds(26, 146, 200, 14);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lbldct_quant_quality;
    }

    private JLabel getlbldct_quant_tbl_select() {
        if (this.lbldct_quant_tbl_select == null) {
            try {
                this.lbldct_quant_tbl_select = new JLabel();
                this.lbldct_quant_tbl_select.setName("lbldct_quant_tbl_select");
                this.lbldct_quant_tbl_select.setText("DCT Quant Table Select :");
                this.lbldct_quant_tbl_select.setBounds(26, 186, 200, 14);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lblsharp_mode_selection;
    }

    private JLabel getlblsharp_quant_quality() {
        if (this.lblsharp_quant_quality == null) {
            try {
                this.lblsharp_quant_quality = new JLabel();
                this.lblsharp_quant_quality.setName("lblsharp_quant_quality");
                this.lblsharp_quant_quality.setText("Sharp Block DCT Quant Quality :");
                this.lblsharp_quant_quality.setBounds(26, 230, 200, 14);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lblsharp_quant_quality;
    }

    private JLabel getlblsharp_quant_tbl_select() {
        if (this.lblsharp_quant_tbl_select == null) {
            try {
                this.lblsharp_quant_tbl_select = new JLabel();
                this.lblsharp_quant_tbl_select.setName("lblsharp_quant_tbl_select");
                this.lblsharp_quant_tbl_select.setText("Sharp Block DCT Quant Table Select :");
                this.lblsharp_quant_tbl_select.setText("<html><body>Sharp Block DCT Quant<br>Table Select :</body></html>");
                this.lblsharp_quant_tbl_select.setBounds(26, 270, 200, 25);
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.lblsharp_quant_tbl_select;
    }

    private JPanel getpanelButtons() {
        if (this.panelButtons == null) {
            try {
                this.panelButtons = new JPanel();
                this.panelButtons.setName("panelButtons");
                this.panelButtons.setLayout((LayoutManager) null);
                this.panelButtons.setBounds(10, 317, 425, 50);
                this.panelButtons.setBorder(BorderFactory.createRaisedBevelBorder());
                this.panelButtons.add(this.bOk, this.bOk.getName());
                this.panelButtons.add(this.bCancel, this.bCancel.getName());
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.panelButtons;
    }

    private JPanel getpanelSettings() {
        if (this.panelSettings == null) {
            try {
                this.panelSettings = new JPanel();
                this.panelSettings.setName("panelSettings");
                this.panelSettings.setLayout((LayoutManager) null);
                this.panelSettings.setBounds(10, 5, 425, 310);
                this.panelSettings.setBorder(BorderFactory.createTitledBorder("Settings"));
                this.panelSettings.add(this.chkdisable_hw_cursor, this.chkdisable_hw_cursor.getName());
                this.panelSettings.add(this.chkdifferential_setting, this.chkdifferential_setting.getName());
                this.panelSettings.add(this.chkcompression_mode, this.chkcompression_mode.getName());
                this.panelSettings.add(this.cmbdct_quant_quality, this.cmbdct_quant_quality.getName());
                this.panelSettings.add(this.cmbdct_quant_tbl_select, this.cmbdct_quant_tbl_select.getName());
                this.panelSettings.add(this.cmbsharp_quant_quality, this.cmbsharp_quant_quality.getName());
                this.panelSettings.add(this.cmbsharp_quant_tbl_select, this.cmbsharp_quant_tbl_select.getName());
                this.panelSettings.add(this.lblsdisable_hw_cursor, this.lblsdisable_hw_cursor.getName());
                this.panelSettings.add(this.lbldifferential_setting, this.lbldifferential_setting.getName());
                this.panelSettings.add(this.lblcompression_mode, this.lblcompression_mode.getName());
                this.panelSettings.add(this.lbldct_quant_quality, this.lbldct_quant_quality.getName());
                this.panelSettings.add(this.lbldct_quant_tbl_select, this.lbldct_quant_tbl_select.getName());
                this.panelSettings.add(this.lblsharp_quant_quality, this.lblsharp_quant_quality.getName());
                this.panelSettings.add(this.lblsharp_quant_tbl_select, this.lblsharp_quant_tbl_select.getName());
            } catch (Throwable th) {
                handleException(th);
            }
        }
        return this.panelSettings;
    }

    private void handleException(Throwable th) {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        th.printStackTrace(System.out);
    }

    private void initialize() {
        try {
            getbCancel();
            getbOk();
            getchkdisable_hw_cursor();
            getchkdifferential_setting();
            getchkcompression_mode();
            getcmbdct_quant_quality();
            getcmbdct_quant_tbl_select();
            getcmbsharp_quant_quality();
            getcmbsharp_quant_tbl_select();
            getlblsdisable_hw_cursor();
            getlbldifferential_setting();
            getlblcompression_mode();
            getlbldct_quant_quality();
            getlbldct_quant_tbl_select();
            getlblsharp_quant_quality();
            getlblsharp_quant_tbl_select();
            getpanelButtons();
            getpanelSettings();
            getDialogPanel();
            getdlgVideoEngineConfig();
        } catch (Throwable th) {
            handleException(th);
        }
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.bOk) {
            if (this.chkdisable_hw_cursor.isSelected()) {
                this.m_videngcfgs.disable_hw_cursor = 1;
            } else {
                this.m_videngcfgs.disable_hw_cursor = 0;
            }
            if (this.chkdifferential_setting.isSelected()) {
                this.m_videngcfgs.differential_setting = 1;
            } else {
                this.m_videngcfgs.differential_setting = 0;
            }
            this.m_videngcfgs.sharp_mode_selection = 1;
            this.m_videngcfgs.dct_quant_quality = (byte) (this.cmbdct_quant_quality.getSelectedIndex() + 1);
            this.m_videngcfgs.dct_quant_tbl_select = (byte) this.cmbdct_quant_tbl_select.getSelectedIndex();
            this.m_videngcfgs.sharp_quant_quality = (byte) (this.cmbsharp_quant_quality.getSelectedIndex() + 1);
            this.m_videngcfgs.sharp_quant_tbl_select = (byte) this.cmbsharp_quant_tbl_select.getSelectedIndex();
            this.m_videngcfgs.compression_mode = (byte) this.chkcompression_mode.getSelectedIndex();
            IVTPPktHdr iVTPPktHdr = new IVTPPktHdr(IVTPPktHdr.ADVISER_AST2K_SET_VIDEO_ENGINE_CONFIGS, this.m_videngcfgs.size(), 0);
            JViewerApp.getInstance().getKVMClient().sendMessage(iVTPPktHdr.array(), iVTPPktHdr.size());
            JViewerApp.getInstance().getKVMClient().sendMessage(this.m_videngcfgs.array(), this.m_videngcfgs.size());
            System.out.println("After sending the values to server");
            dispose();
            return;
        }
        dispose();
    }
}
