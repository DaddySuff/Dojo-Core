package ranked.dojo.broadcast;

import lombok.Getter;

import java.util.List;


@Getter
public class Broadcast {
    private final List<String> lines;

    /**
     * Constructor for the Broadcast class.
     *
     * @param lines the lines of the broadcast
     */
    public Broadcast(List<String> lines) {
        this.lines = lines;
    }
}