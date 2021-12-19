package com.example.sym.labo3;

import java.util.List;

/**
 * @author  Eric Bousbaa & Ilias Goujgali & Guillaume Laubscher
 * Interface de callback utilisé pour NdefReaderTask
 */
public interface Callback {
    void execute(List<String> results);
}
