package ranked.dojo.chat;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatService {
    private boolean isChatMuted;

    /**
     * Constructor for the ChatRepository class.
     *
     * @param isChatMuted boolean
     */
    public ChatService(boolean isChatMuted) {
        this.isChatMuted = isChatMuted;
    }
}