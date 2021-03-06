/*
 * Created by Jeremy Brooks, 2011. <jeremyb@whirljack.net>
 * Free for personal or commercial use, with or without modification.
 * No warranty is expressed or implied.
 *
 */
package net.jeremybrooks.knickerexamples.definition;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.jeremybrooks.knicker.Knicker.SourceDictionary;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Definition;


/**
 * An instance of SwingWorker that knows how to look up word definitions.
 *
 * When a definition is found, it is sent to interested parties via the
 * Notifier.
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
	LinkedHashSet<SourceDictionary> source = new LinkedHashSet<SourceDictionary>();
	source.add(SourceDictionary.ahd);
	source.add(SourceDictionary.wordnet);
	source.add(SourceDictionary.wiktionary);
	source.add(SourceDictionary.webster);
	source.add(SourceDictionary.century);

	// get the definitions
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
