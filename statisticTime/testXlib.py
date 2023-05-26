from contextlib import contextmanager
from typing import Any, Dict, Optional, Tuple, Union  # noqa

from Xlib import X
from Xlib.display import Display
from Xlib.error import XError
from Xlib.xobject.drawable import Window
from Xlib.protocol.rq import Event
import time
import socket
import json

HOST = '127.0.0.1'
PORT = 1413

# 创建socket对象
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# 连接服务器
s.connect((HOST, PORT))

# Connect to the X server and get the root window
disp = Display()
root = disp.screen().root


# Prepare the property names we use so they can be fed into X11 APIs
NET_ACTIVE_WINDOW = disp.intern_atom('_NET_ACTIVE_WINDOW')
NET_WM_NAME = disp.intern_atom('_NET_WM_NAME')  # UTF-8
WM_NAME = disp.intern_atom('WM_NAME')           # Legacy encoding

last_seen = {'xid': None, 'title': None, 'app': None, 'timestamp': None}  # type: Dict[str, Any]
black_seen = {'xid': 0, 'title': None, 'app': None, 'timestamp': None}

@contextmanager
def window_obj(win_id: Optional[int]) -> Window:
    """Simplify dealing with BadWindow (make it either valid or None)"""
    window_obj = None
    if win_id:
        try:
            window_obj = disp.create_resource_object('window', win_id)
        except XError:
            pass
    yield window_obj


def get_active_window() -> Tuple[Optional[int], bool]:
    """Return a (window_obj, focus_has_changed) tuple for the active window."""
    response = root.get_full_property(NET_ACTIVE_WINDOW,
                                      X.AnyPropertyType)
    if not response:
        return None, False
    win_id = response.value[0]

    focus_changed = (win_id != last_seen['xid'])
    if focus_changed:
        with window_obj(last_seen['xid']) as old_win:
            if old_win:
                old_win.change_attributes(event_mask=X.NoEventMask)

        last_seen['xid'] = win_id
        with window_obj(win_id) as new_win:
            if new_win:
                new_win.change_attributes(event_mask=X.PropertyChangeMask)

    return win_id, focus_changed


def _get_window_name_inner(win_obj: Window) -> str:
    """Simplify dealing with _NET_WM_NAME (UTF-8) vs. WM_NAME (legacy)"""
    for atom in (NET_WM_NAME, WM_NAME):
        try:
            window_name = win_obj.get_full_property(atom, 0)
            app_name = win_obj.get_wm_class()[0]
        except UnicodeDecodeError:  # Apparently a Debian distro package bug
            title = "<could not decode characters>"
        else:
            if window_name:
                win_name = window_name.value  # type: Union[str, bytes]
                if isinstance(win_name, bytes):
                    # Apparently COMPOUND_TEXT is so arcane that this is how
                    # tools like xprop deal with receiving it these days
                    win_name = win_name.decode('utf8', 'replace')
                return win_name, app_name
            else:
                title = "<unnamed window>"
    return "{} (XID: {})".format(title, win_obj.id)


def get_window_name(win_id: Optional[int]) -> Tuple[Optional[str], bool]:
    """Look up the window name for a given X11 window ID"""
    if not win_id:
        last_seen['title'] = None
        return last_seen['title'], True

    title_changed = False
    with window_obj(win_id) as wobj:
        if wobj:
            try:
                win_title, app = _get_window_name_inner(wobj)
            except XError:
                pass
            else:
                title_changed = (win_title != last_seen['title'])
                last_seen['title'] = win_title
                last_seen['app'] = app

    return last_seen['title'], title_changed


def handle_xevent(event: Event):
    """Handler for X events which ignores anything but focus/title change"""
    if event.type != X.PropertyNotify:
        return

    changed = False
    if event.atom == NET_ACTIVE_WINDOW:
        if get_active_window()[1]:
            get_window_name(last_seen['xid'])  # Rely on the side-effects
            changed = True
    elif event.atom in (NET_WM_NAME, WM_NAME):
        changed = changed or get_window_name(last_seen['xid'])[1]
    
    changed = changed or int(time.time()) - last_seen['timestamp'] > 300

    if changed:
        last_seen['timestamp'] = int(time.time())
        handle_change(last_seen)


def handle_change(new_state: dict):
    """Replace this with whatever you want to actually do"""
    print(new_state)
    
    s.sendall(json.dumps(new_state).encode('utf-8'))
    s.sendall(b'\n')
    # last_time = int(time.time())

if __name__ == '__main__':

    # Listen for _NET_ACTIVE_WINDOW changes
    root.change_attributes(event_mask=X.PropertyChangeMask)

    # Prime last_seen with whatever window was active when we started this
    get_window_name(get_active_window()[0])
    last_seen['timestamp'] = int(time.time())
    handle_change(last_seen)

    while True:  # next_event() sleeps until we get an event
        handle_xevent(disp.next_event())

    s.close()