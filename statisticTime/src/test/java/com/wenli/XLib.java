package com.wenli;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.win32.*;
import com.sun.jna.win32.StdCallLibrary;


public interface XLib extends StdCallLibrary {
    XLib INSTANCE = (XLib) Native.loadLibrary("XLib", Psapi.class);

    int XGetInputFocus(X11.Display display, X11.Window focus_return, Pointer revert_to_return);
}
