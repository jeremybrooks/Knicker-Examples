/*
 * Created by Jeremy Brooks, 2011. <jeremyb@whirljack.net>
 * Free for personal or commercial use, with or without modification.
 * No warranty is expressed or implied.
 *
 */
package net.jeremybrooks.knickerexamples.definition;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;
import javazoom.jl.player.Player;
import net.jeremybrooks.knicker.dto.Definition;
import org.apache.log4j.Logger;

/**
 * Main window for the definition example.
 *
 * This class observes Notifier to be notified when there is data to display.
 * 
 * @author jeremyb
 */
public class DefinitionExampleWindow extends javax.swing.JFrame implements Observer {

    /** Logging is good. */
    private static Logger logger = Logger.getLogger(DefinitionExampleWindow.class);

    /** The audio data to play. */
    private byte[] audio;
    
    /** Creates new form DefinitionExampleWindow */
    public DefinitionExampleWindow() {
        initComponents();
	// Subscribe to event notification
	Notifier.getInstance().addObserver(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtWord = new javax.swing.JTextField();
        btnDefine = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDefinition = new javax.swing.JTextPane();
        btnAudio = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Word:");

        btnDefine.setText("Define");
        btnDefine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefineActionPerformed(evt);
            }
        });

        txtDefinition.setContentType("text/html");
        txtDefinition.setEditable(false);
        jScrollPane1.setViewportView(txtDefinition);

        btnAudio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sound.png"))); // NOI18N
        btnAudio.setEnabled(false);
        btnAudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAudioActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(btnDefine)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnAudio))
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtWord, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtWord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(7, 7, 7)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(btnAudio)
                    .add(btnDefine))
                .add(9, 9, 9)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    /*
     * Define the word.
     *
     * One thread will look up the definition, and another thread will retrieve
     * the audio pronunciation.
     */
    private void btnDefineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDefineActionPerformed
	String word = this.txtWord.getText();
	if (word == null || word.trim().isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Please enter a word to look up.",
		    "No Word", JOptionPane.INFORMATION_MESSAGE);
	} else {
	    this.btnAudio.setEnabled(false);
	    this.txtDefinition.setText("Looking up <i>" + word + "</i>....");
	    word = word.trim();
	    try {
		(new Lookup(word)).execute();
		(new LookupAudio(word)).execute();
	    } catch (Exception e) {
		logger.error("Something went wrong while looking up the word.", e);
	    }
	}
    }//GEN-LAST:event_btnDefineActionPerformed


    /*
     * Use the javazoom library to play the pronunciation.
     */
    private void btnAudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAudioActionPerformed
	ByteArrayInputStream in = null;
	Player player = null;
	if (this.audio != null) {
	    try {
		logger.info("There are " + this.audio.length + " bytes of audio to play.");
		in = new ByteArrayInputStream(this.audio);
		player = new Player(in);
		player.play();
	    } catch (Exception e) {
		logger.error("Error while playing audio data.", e);
		JOptionPane.showMessageDialog(this,
			"Error playing audio: " + e.getMessage(),
			"Could not play audio",
			JOptionPane.ERROR_MESSAGE);
	    } finally {
		if (in != null) {
		    try {
			in.close();
		    } catch (Exception e) {
			logger.warn("Error while closing input stream.", e);
		    }
		}
	    }
	}
    }//GEN-LAST:event_btnAudioActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
	// unsubscribe for event notification
	Notifier.getInstance().deleteObserver(this);

	// get off the screen
	this.setVisible(false);
	
    }//GEN-LAST:event_formWindowClosing


    /**
     * Called by the Notifier when things happen.
     *
     * In this implementation, there are only two events we expect to receive.
     * If arg is a byte array, we assume it is pronunciation data and enable
     * the audio button. If it is a List, we check to be sure it is a List of
     * Definition objects, then build and display an HTML document containing
     * the definitions.
     *
     * There are many ways this could be made more robust, but this suffices
     * for demonstration purposes.
     *
     * @param o the calling object.
     * @param arg argument passed in by the caller.
     */
    public void update(Observable o, Object arg) {
	if (arg instanceof byte[]) {
	    logger.info("Got a byte array.");
	    
	    this.audio = (byte[])arg;
	    this.btnAudio.setEnabled(true);
	    
	} else if (arg instanceof List<?>) {
	    logger.info("Got a List.");
	    try {
		@SuppressWarnings("unchecked")
		List<Definition> defList = (List<Definition>)arg;

		if (defList.isEmpty()) {
		    this.txtDefinition.setText("Could not find definition for <i>"
			    + this.txtWord.getText() + "</i>.");
		} else {
		    StringBuilder sb = new StringBuilder("<html><body><h1>");
		    sb.append(this.txtWord.getText()).append("</h1>");

		    int i = 1;
		    for (Definition def : defList) {
			sb.append("<p><b>").append(i++).append(".</b> ");
			sb.append("<i>").append(def.getPartOfSpeech()).append(": </i>");
			sb.append(def.getText());
		    }

		    sb.append("</body></html>");

		    this.txtDefinition.setText(sb.toString());
		    this.txtDefinition.setCaretPosition(0);
		 }
	    } catch(ClassCastException cce) {
		this.txtDefinition.setText("<b>Wrong list type returned.</b>");
	    }
	}
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAudio;
    private javax.swing.JButton btnDefine;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtDefinition;
    private javax.swing.JTextField txtWord;
    // End of variables declaration//GEN-END:variables




}
