/*
 * Copyright 2011 Jeremy Brooks. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted with no restriction.
 *
 */
package net.jeremybrooks.knickerexamples.lookup;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.jeremybrooks.knicker.Knicker.SourceDictionary;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Definition;


/**
 *
 * @author jeremyb
 */
public class Lookup extends javax.swing.SwingWorker<List<Definition>, Object> {

    /** The word to look up. */
    private String word;

    /** Require use of the non-default constructor */
    private Lookup() {
    }


    /**
     * Construct a new instance of Lookup.
     *
     * @param word the word to define.
     */
    public Lookup(String word) {
	this.word = word;
    }


    /**
     * Look up the definition of the word.
     *
     * @return list of definitions for the word.
     * @throws Exception if there are any errors.
     */
    @Override
    protected List<Definition> doInBackground() throws Exception {

	// look in these dictionaries, in this order
	EnumSet<SourceDictionary> source = EnumSet.of(
		SourceDictionary.ahd,
		SourceDictionary.wordnet,
		SourceDictionary.wiktionary,
		SourceDictionary.webster,
		SourceDictionary.century);

	List<Definition> retList = WordApi.definitions(word, source);

	// if nothing was found, try a lower case verison of the word
	if (retList.isEmpty()) {
	    this.word = this.word.toLowerCase();
	    retList = WordApi.definitions(word, source);
	}

	return retList;
    }


    /**
     * Notify observers that there is some data.
     */
    @Override
    protected void done() {
	List<Definition> list = null;
	try {
	    list = get();
	} catch (Exception e) {
	    // return an empty list so that some message will be displayed
	    list = new ArrayList<Definition>();
	} finally {
	    Notifier.getInstance().sendMessage(list);
	}
    }

}
