/*
 * Copyright 2011 Jeremy Brooks. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted with no restriction.
 *
 */
package net.jeremybrooks.knickerexamples.lookup;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.AudioFileMetadata;
import net.jeremybrooks.knicker.logger.KnickerLogger;
import org.apache.log4j.Logger;


/**
 *
 * @author jeremyb
 */
public class LookupAudio extends javax.swing.SwingWorker<byte[], Object> {

    /** The word to look up. */
    private String word;

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

	List<AudioFileMetadata> list = WordApi.audio(word);

	if (list.size() > 0) {
	    logger.info(list.get(0).getFileUrl());
	    audio = WordApi.getAudioData(list.get(0));
	}

	return audio;
    }


    /**
     * Display the definitions when the lookup is finished.
     *
     * <p>If there were no definitions found, display a sane message.</p>
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


    public byte[] getAudioData(AudioFileMetadata audioFileMetadata) throws KnickerException {


	byte[] data = null;
	int contentLength = 0;
	BufferedInputStream in = null;
	URL page = null;
	URLConnection conn = null;

	try {
	    page = new URL(audioFileMetadata.getFileUrl());
	    conn = page.openConnection();
	    contentLength = conn.getContentLength();
	    in = new BufferedInputStream(conn.getInputStream());
	    data = new byte[contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    while (offset < contentLength) {
		bytesRead = in.read(data, offset, data.length - offset);
		if (bytesRead == -1) {
		    break;
		}
		offset += bytesRead;
	    }

	} catch (Exception e) {
	    throw new KnickerException("There was an error while getting audio data from URL "
		    + audioFileMetadata.getFileUrl(), e);
	} finally {
	    try {
		if (in != null) {
		    in.close();
		}
	    } catch (Exception e) {
		// not critical, just log warning
		KnickerLogger.getLogger().log("WARN: Error closing input stream.", e);
	    }
	}

	return data;
    }



}
