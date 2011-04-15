/*
 * Copyright 2011 Jeremy Brooks. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted with no restriction.
 *
 */

package net.jeremybrooks.knickerexamples;

import java.util.Observable;

/**
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
