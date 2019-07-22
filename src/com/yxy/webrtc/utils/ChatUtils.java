package com.yxy.webrtc.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class ChatUtils {
    public static final String OFFER="offer";
    public static final String ANSWER="answer";

    public static final String CANDIDATE="candidate";
    public static final String ROOM_CHAT="room_chat";

    public static final String EXIT="exit";
    public static final String JOIN="join";
    public static final String ALERT="alert";
    public static final String END_CHAT="end_chat";
    public static final String ONLINE="online";

    public static final Set<String> chatTypes = new HashSet<String>();

    /**
     * 以下是用户可以发的消息  其余为系统消息
     */
    static {
        chatTypes.add(OFFER);
        chatTypes.add(ANSWER);
        chatTypes.add(CANDIDATE);
        chatTypes.add(ROOM_CHAT);
        chatTypes.add(ONLINE);
    }

}
