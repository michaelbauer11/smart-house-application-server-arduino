#region ----------   ABOUT   -----------------------------
"""
##################################################################
# Created By:  Michael Chernovilski                              #
# Date: 20/09/2014                                               #
# Name: Server session with any clients                          #
# Version: 1.0                                                   #
# Windows Tested Versions: Win 7 32-bit                          #
# Python Tested Versions: 2.6 32-bit                             #
# Python Environment  : PyCharm                                  #
# pyCrypto Tested Versions: Python 2.7 32-bit                    #
##################################################################
"""
#endregion

#region ----------   IMPORTS   -----------------------------
import threading
import os
import socket
import string
#endregion

#region ----------   C O N S T A N T S  ------------------------------------------------------
LEN_UNIT_BUF = 2048                                       # Min len of buffer for receive from server socket
ERROR_SOCKET = "Socket_Error"                             # Error message If you happened socket error
ERROR_EXCEPT = "Exception"                                # Error message If you happened exception
SERVER_ABORT = "Aborting the server..."
#endregion

#region  -----  SessionWithClient C L A S S  -----
class  SessionWithDevice(threading.Thread):

    # -----  F U N C T I O N S  -----
    #-----------------------------------------------------------------------------------------------
    #  class definition function
    #-----------------------------------------------------------------------------------------------
    def __init__(self, deviceServer, clientSock, addr ):
        threading.Thread.__init__(self)
        self.deviceServer = deviceServer
        # new open socket  for client
        self.clientSock = clientSock
        # address connection : IP and Port
        self.addr = addr
        # device's status
        self.status = 0

    #-----------------------------------------------------------------------------------------------
    # the main function of the THREAD sessionWithClient class  
    #-----------------------------------------------------------------------------------------------  
    def run(self):
        try:
            data = self.clientSock.recv(1024)
            #data = data.split('#')
            #print str(data)
            self.status = data


        except Exception as e:
            print str(e) + ERROR_EXCEPT + "  from " + str(self.addr[0])
            self.clientSock.close()

    def send(self, data):
        self.clientSock.send(data[0].rstrip())


   
#endregion   

