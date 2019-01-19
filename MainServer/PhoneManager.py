#region ----------   ABOUT   -----------------------------
"""
##################################################################
# Created By:  Michael Bauer                                     #
# Date: 20/09/2014                                               #
# Name: Server  multiclients                                     #
# Version: 1.0                                                   #
# Windows Tested Versions: Win 7 32-bit                          #
# Python Tested Versions: 2.7 32-bit                             #
# Python Environment  : PyCharm                                  #
##################################################################
"""
#endregion

#region ----------   IMPORTS   -----------------------------
import threading,socket, sys, os
from SessionWithPhone import *
#endregion

#region -----  CONSTANTS  -----
# For every client to been thread
THREAD_LIMIT = 50
SERVER_ABORT = "Aborting the server..."
PORT_PHONE = 20000
#endregion


class PhoneManager(threading.Thread):
    # -----  DATA  -----
    listenerSock = None
    # Dictionary for client connctions : Key - ip  Value - SessionWithPhones
    open_phones = {}


    def __init__(self, deviicesManager):
        threading.Thread.__init__(self)
        self.deviicesManager = deviicesManager

    def run(self):
        #print "Server running...Waiting for a  phones connection..."
        try:
            # Listener socket
            listenerSock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            listenerSock.bind(("0.0.0.0",PORT_PHONE))
            listenerSock.listen(5)
            print "Listening to phones..."

            while True:
                phoneSock, addr = listenerSock.accept()
                print "Connected..."
                # Thread creating loop
                while True:
                    if threading.activeCount() < THREAD_LIMIT:
                        phoneIP = addr[0]  # key - IP client
                        # First Connection by IP  clientIP
                        sessionWithPhone = SessionWithPhone(self, phoneSock, addr)
                        self.open_phones[phoneIP] = sessionWithPhone
                        sessionWithPhone.start()
                        break
        except socket.error , er_msg:
            error_code = er_msg[0]
            if error_code == 10048:
                print "Port " + str(PORT_PHONE) + " is busy#"
            else:
                print str(er_msg) + "#"
        except Exception as er_msg:
            print str(er_msg) + "#"

#endregion
