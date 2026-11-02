package net.pcal.splitscreen.common;

import java.util.ArrayList;
import java.util.List;

public enum SplitscreenLayout {
    HORIZONTAL("LEFT", "RIGHT"),
    VERTICAL("TOP", "BOTTOM"),
    QUAD("TOP_LEFT", "TOP_RIGHT", "BOTTOM_LEFT", "BOTTOM_RIGHT"),
    FULLSCREEN("FULLSCREEN"),
    WINDOWED("WINDOWED");

    private final String[] positions;

    SplitscreenLayout(String... positions) {
        this.positions = positions;
    }

    public String[] getPositions() {
        return positions;
    }

    public String[] getRotatedPositions(int offset) {
        if (positions.length <= 1) return positions;
        String[] result = new String[positions.length];
        for (int i = 0; i < positions.length; i++) {
            result[i] = positions[(i + offset) % positions.length];
        }
        return result;
    }

    public String getPrimaryPosition() {
        return positions[0];
    }

    public static String[] getTriplePositions(int offset) {
        List<String> base = new ArrayList<>();
        base.add("TOP_LEFT");
        base.add("TOP_RIGHT");
        base.add("BOTTOM");
        String[] result = new String[3];
        for (int i = 0; i < 3; i++) {
            result[i] = base.get((i + offset) % 3);
        }
        return result;
    }
}
