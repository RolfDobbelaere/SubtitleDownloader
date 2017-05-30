/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template files, choose Tools | Templates
 * and open the template in the editor.
 */
package subtitlemanager;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import static subtitlemanager.SubtitleManager.HASH_ALGO;

/**
 *
 * @atulgpt <atlgpt@gmail.com>
 */
public class SubtitleDownloaderUI extends javax.swing.JFrame {

    private String[] mFilePathArray;
    private String mLangs;
    private String[] mLangArray;
    private String mSavePath;
    private JFileChooser mFileChooser = null;

    /**
     * Creates new form SubtitleDownloaderUI
     */
    public SubtitleDownloaderUI() {
        initComponents();
    }

    public SubtitleDownloaderUI(String[] langArray) {
        this.mLangArray = langArray;
        initComponents();
        initComponents1();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pathNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        langComboBox = new javax.swing.JComboBox<>();
        openFileManagerButton = new javax.swing.JButton();
        langsTextField = new javax.swing.JTextField();
        uploadButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        menuBar = new javax.swing.JMenuBar();
        settingsMenuButton = new javax.swing.JMenu();
        aboutMenuButton = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Subtitle Downloader");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Choose File(s) Path:");
        jLabel1.setToolTipText("");

        pathNameTextField.setToolTipText("File path with file name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Choose Language(s):");

        submitButton.setText("Downlaod");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        langComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                langComboBoxActionPerformed(evt);
            }
        });

        openFileManagerButton.setText("Open");
        openFileManagerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileManagerButtonActionPerformed(evt);
            }
        });

        langsTextField.setEditable(false);
        langsTextField.setBorder(null);

        uploadButton.setText("Upload");
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });

        settingsMenuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        settingsMenuButton.setLabel("Settings ");
        settingsMenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsMenuButtonMouseClicked(evt);
            }
        });
        menuBar.add(settingsMenuButton);

        aboutMenuButton.setLabel("About ");
        aboutMenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aboutMenuButtonMouseClicked(evt);
            }
        });
        menuBar.add(aboutMenuButton);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(24, 24, 24)
                                .addComponent(pathNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(langsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(langComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(openFileManagerButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(uploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(submitButton)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pathNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openFileManagerButton))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(langComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(langsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(submitButton)
                        .addComponent(uploadButton))
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        String pathNamesString;
        pathNamesString = pathNameTextField.getText();
        if (pathNamesString.equals("")) {
            showDialog("File path shouldn't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (mLangs.equals("")) {
                showDialog("Atleast one language should be selected!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                mFilePathArray = pathNamesString.split("; ");
                BackgroundTasks.UploadSubtitles uploadSubtitles = new BackgroundTasks().new UploadSubtitles(mFilePathArray, mLangs, this);
                uploadSubtitles.execute();
            }
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void openFileManagerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileManagerButtonActionPerformed
        String dirName = UserPreferences.getDefaultFileLoc();
        if (mFileChooser == null) {
            mFileChooser = new JFileChooser(dirName);
            SwingUtilities.updateComponentTreeUI(mFileChooser);
            mFileChooser.setMultiSelectionEnabled(true);
        }
        mFileChooser.setCurrentDirectory(new File(dirName));
        mFileChooser.showOpenDialog(this);
        File[] files = mFileChooser.getSelectedFiles();
        if (files.length > 0) {
            pathNameTextField.setText(getFilesName(files));
        }
    }//GEN-LAST:event_openFileManagerButtonActionPerformed

    private void langComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_langComboBoxActionPerformed
        JComboBox jComboBox = (JComboBox) evt.getSource();
        String lang = (String) jComboBox.getSelectedItem();
        String langs = langsTextField.getText();
        if (langs.equals("")) {
            langsTextField.setText(lang);
            mLangs = lang;
        } else {
            if (langs.contains(lang)) {
                String tempLangs1 = langs.replace("," + lang, "");
                String tempLangs2 = tempLangs1.replace(lang + ",", "");
                String tempLangs3 = tempLangs2.replace(lang, "");
                mLangs = tempLangs3;
                langsTextField.setText(tempLangs3);
            } else {
                langsTextField.setText(langs + "," + lang);
                mLangs = langs + "," + lang;
            }
        }
        System.out.println("Selected langs: " + mLangs);

    }//GEN-LAST:event_langComboBoxActionPerformed

    private void settingsMenuButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsMenuButtonMouseClicked
        Settings settingsUI = new Settings();
    }//GEN-LAST:event_settingsMenuButtonMouseClicked

    private void aboutMenuButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aboutMenuButtonMouseClicked
        About aboutUI = new About();
    }//GEN-LAST:event_aboutMenuButtonMouseClicked

    private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed
        String pathNamesString;
        pathNamesString = pathNameTextField.getText();
        ArrayList<String> videoHashArray;
        if (pathNamesString.equals("")) {
            showDialog("File path shouldn't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            mFilePathArray = pathNamesString.split("; ");
            if (mFilePathArray.length > 2) {
                showDialog("Choose only two files!\nOne video file and corresponding subtitle(.srt)", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                VideoHashCalc videoHashCalc = new VideoHashCalc(this);
                String subtitlePath;
                String videoPath;
                if (mFilePathArray[0].contains(".srt")) {
                    subtitlePath = mFilePathArray[0];
                    videoPath = mFilePathArray[1];
                } else {
                    subtitlePath = mFilePathArray[1];
                    videoPath = mFilePathArray[0];
                }
                ArrayList<String> mVideoFilePathArray = new ArrayList<>();
                mVideoFilePathArray.add(videoPath);
                ArrayList<String> mSubtitleFilePathArray = new ArrayList<>();
                mSubtitleFilePathArray.add(subtitlePath);
                videoHashArray = videoHashCalc.getHash(mVideoFilePathArray.toArray(new String[mVideoFilePathArray.size()]), HASH_ALGO);
                System.out.println("Hash array: " + videoHashArray.toString());
                if (videoHashArray.size() > 0) {
                    HTTPRequest httpRequest = new HTTPRequest(this);
                    httpRequest.sendUploadRequests(videoHashArray, mSubtitleFilePathArray, mLangs);
                }
            }
        }
    }//GEN-LAST:event_uploadButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubtitleDownloaderUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SubtitleDownloaderUI().setVisible(true);
        });
    }

    public String[] getFilePath() {
        return mFilePathArray;
    }

    public String getLang() {
        return mLangs;
    }

    public String getSavePath() {
        return mSavePath;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenuButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox<String> langComboBox;
    private javax.swing.JTextField langsTextField;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton openFileManagerButton;
    private javax.swing.JTextField pathNameTextField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenu settingsMenuButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JButton uploadButton;
    // End of variables declaration//GEN-END:variables

    private void initComponents1() {
        for (String lang : mLangArray) {
            langComboBox.addItem(lang);
        }
    }

    private String getFilesName(File[] files) {
        StringBuilder filesStringBuilder = new StringBuilder();
        for (File file : files) {
            filesStringBuilder.append(file.getAbsoluteFile());
            filesStringBuilder.append("; ");
        }
        return filesStringBuilder.toString();
    }

    public void showDialog(String msg, String title, int type) {
        JOptionPane.showMessageDialog(this, msg, title, type);
    }

    public void setProgressBar(int cmd) {
        if (cmd == 1) {
            progressBar.setIndeterminate(true);
        } else if (cmd == 0) {
            progressBar.setIndeterminate(false);
        }
    }
}
