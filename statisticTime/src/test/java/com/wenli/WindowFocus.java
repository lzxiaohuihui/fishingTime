package com.wenli;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;

public class WindowFocus {

    public static void main(String[] args) {
        if(Platform.isLinux()) {  // Possibly most of the Unix systems will work here too, e.g. FreeBSD
            final X11 x11 = X11.INSTANCE;
            final XLib xlib= XLib.INSTANCE;
            X11.Display display = x11.XOpenDisplay(null);
            X11.Window window=new X11.Window();
            xlib.XGetInputFocus(display, window,Pointer.NULL);
            X11.XTextProperty name=new X11.XTextProperty();
            x11.XGetWMName(display, window, name);
            System.out.println(name.toString());
        }
    }
}
