/*
 * Created by Jeremy Brooks, 2011. <jeremyb@whirljack.net>
 * Free for personal or commercial use, with or without modification.
 * No warranty is expressed or implied.
 *
 */
package net.jeremybrooks.knickerexamples.definition;

import java.util.List;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.AudioFileMetadata;
import org.apache.log4j.Logger;


/**
 * An instance of SwingWorker that knows how to look up word definitions.
 *
 * When a definition is found, it is sent to interested parties via the
 * Notifier.
 * 
 * @author jeremyb
 */
public class LookupAudio extends javax.swing.SwingWorker<byte[], Object> {

    /** The word to look up. */
    private String word;

    /** Logging */
    Logger logger = Logger.getLogger(LookupAudio.class);


    /** Require use of the non-default constructor */
    private LookupAudio() {
    }


    /**
     * Construct a new instance of Lookup.
     *
     * The audioObserver object will get a message when audio is available.
     * If the message is null, no audio is available.
     * 
     * @param word the word to define.
     */
    public LookupAudio(String word) {
	this.word = word;
    }


    /**
     * Look up audio pronunciation for the word.
     *
     * @return list of definitions for the word.
     * @throws Exception if there are any errors.
     */
    @Override
    protected byte[] doInBackground() throws Exception {
	byte[] audio = null;

	// get a list of all available audio pronunciations
	List<AudioFileMetadata> list = WordApi.audio(word);

	// if a pronunciation is available...
	if (list.size() > 0) {
	    logger.info(list.get(0).getFileUrl());

	    // ...get the first available audio pronunciation
	    audio = WordApi.getAudioData(list.get(0));
	}

	return audio;
    }


    /**
     * Send data to interested observers.
     */
    @Override
    protected void done() {
	byte[] audio = null;
	try {
	    audio = get();
	} catch (Exception e) {
	    // doesn't matter; will return null
	} finally {
	    Notifier.getInstance().sendMessage(audio);
	}
    }
}
