/*
 * Created by Jeremy Brooks, 2011. <jeremyb@whirljack.net>
 * Free for personal or commercial use, with or without modification.
 * No warranty is expressed or implied.
 *
 */
package net.jeremybrooks.knickerexamples.definition;

import java.util.Observable;

/**
 * Singleton Observable class.
 *
 * Observers that are interested in knowing about events can add themselves:
 * <code>Notifier.getInstance().addObserver(this);</code>
 * 
 * @author jeremyb
 */
public class Notifier extends Observable {

    private static Notifier instance;

    private Notifier() {
    }

    public static Notifier getInstance() {
	if (instance == null) {
	    instance = new Notifier();
	}

	return instance;
    }

    public void sendMessage(Object message) {
	setChanged();
	notifyObservers(message);
    }
}
