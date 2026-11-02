package net.pcal.splitscreen.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.pcal.splitscreen.common.WindowStyle.FULLSCREEN;
import static net.pcal.splitscreen.common.WindowStyle.SPLITSCREEN;
import static net.pcal.splitscreen.common.WindowStyle.WINDOWED;

record WindowMode(
        String name,
        WindowStyle style,
        Function<MinecraftWindow, Integer> xFn,
        Function<MinecraftWindow, Integer> yFn,
        Function<MinecraftWindow, Integer> widthFn,
        Function<MinecraftWindow, Integer> heightFn) {

    WindowStyle getStyle() {
        return style;
    }


    String getName() {
        return name;
    }

    MinecraftWindow.Rectangle getRepositionedBoundsFor(MinecraftWindow screenBounds) {
        return new MinecraftWindow.Rectangle(xFn.apply(screenBounds), yFn.apply(screenBounds), widthFn.apply(screenBounds), heightFn.apply(screenBounds));
    }

    static List<WindowMode> getModes(int gap) {
        final List<WindowMode> modes = new ArrayList<>();
        addMode(modes, "WINDOWED", WINDOWED, r -> r.getWindowBounds().x(), r -> r.getWindowBounds().y(),
                r -> r.getWindowBounds().width(), r -> r.getWindowBounds().height());
        // Positions are computed in monitor-relative coordinates and offset by the
        // monitor's origin so split windows tile edge-to-edge on any monitor.
        addMode(modes, "LEFT", SPLITSCREEN, r -> r.getScreenBounds().x(), r -> r.getScreenBounds().y(),
                r -> half(r, true, gap), r -> r.getScreenBounds().height());
        addMode(modes, "RIGHT", SPLITSCREEN, r -> r.getScreenBounds().x() + half(r, true, 0), r -> r.getScreenBounds().y(),
                r -> r.getScreenBounds().width() - half(r, true, 0), r -> r.getScreenBounds().height());
        addMode(modes, "TOP", SPLITSCREEN, r -> r.getScreenBounds().x(), r -> r.getScreenBounds().y(),
                r -> r.getScreenBounds().width(), r -> half(r, false, gap));
        addMode(modes, "BOTTOM", SPLITSCREEN, r -> r.getScreenBounds().x(), r -> r.getScreenBounds().y() + half(r, false, 0),
                r -> r.getScreenBounds().width(), r -> r.getScreenBounds().height() - half(r, false, 0));
        addMode(modes, "TOP_LEFT", SPLITSCREEN, r -> r.getScreenBounds().x(), r -> r.getScreenBounds().y(),
                r -> half(r, true, gap), r -> half(r, false, gap));
        addMode(modes, "TOP_RIGHT", SPLITSCREEN, r -> r.getScreenBounds().x() + half(r, true, 0), r -> r.getScreenBounds().y(),
                r -> r.getScreenBounds().width() - half(r, true, 0), r -> half(r, false, gap));
        addMode(modes, "BOTTOM_LEFT", SPLITSCREEN, r -> r.getScreenBounds().x(), r -> r.getScreenBounds().y() + half(r, false, 0),
                r -> half(r, true, gap), r -> r.getScreenBounds().height() - half(r, false, 0));
        addMode(modes, "BOTTOM_RIGHT", SPLITSCREEN, r -> r.getScreenBounds().x() + half(r, true, 0), r -> r.getScreenBounds().y() + half(r, false, 0),
                r -> r.getScreenBounds().width() - half(r, true, 0), r -> r.getScreenBounds().height() - half(r, false, 0));
        addMode(modes, "FULLSCREEN", FULLSCREEN, no(), no(), no(), no());
        return modes;
    }

    // Half of the screen extent, rounded so that the two halves sum exactly to the
    // full extent; the gap is shaved off the first half only.
    private static int half(MinecraftWindow r, boolean width, int gap) {
        int extent = width ? r.getScreenBounds().width() : r.getScreenBounds().height();
        return (extent + 1) / 2 - gap;
    }

    private static void addMode(List<WindowMode> modes,
                                String name,
                                WindowStyle style,
                                Function<MinecraftWindow, Integer> xFn,
                                Function<MinecraftWindow, Integer> yFn,
                                Function<MinecraftWindow, Integer> widthFn,
                                Function<MinecraftWindow, Integer> heightFn) {
        modes.add(new WindowMode(name, style, xFn, yFn, widthFn, heightFn));
    }

    private static Function<MinecraftWindow, Integer> no() {
        return r -> -1;
    }

}
