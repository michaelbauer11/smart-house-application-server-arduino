#region ----------   ABOUT   -----------------------------
"""
##################################################################
# Created By:  Michael Chernovilski                              #
# Date: 20/09/2014                                               #
# Name: Server  multiclients                                     #
# Version: 1.0                                                   #
# Windows Tested Versions: Win 7 32-bit                          #
# Python Tested Versions: 2.6 32-bit                             #
# Python Environment  : PyCharm                                  #
##################################################################
"""
#endregion

#region ----------   IMPORTS   -----------------------------
import threading,socket, sys, os
from SessionWithDevice import *
from HouseToDevicesTable import *
#endregion


#region -----  CONSTANTS  -----
# For every client to been thread
THREAD_LIMIT = 50
SERVER_ABORT = "Aborting the server..."
PORT_DEVICE = 8820
#endregion

#region ----------   CLASSES   -----------------------------
#region -----  PythonServer CLASS  -----
class DevicesManager(threading.Thread):
    # -----  DATA  -----
    listenerSocket = None
    # Dictionary for client connctions : Key - ip  Value - SessionWithClient
    open_controllers = {}

    # constructor
    def __init__(self): #listenerPort
        self.listenerPort = PORT_DEVICE
        self.sessionWithDevice = None
        threading.Thread.__init__(self)
        self.houseToDevicesTable = HouseToDevicesTable()

    #the main thread function
    def  run(self):
        print "Server running...Waiting for a connection...#"
        try:
            # Listener socket
            listenerSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            listenerSocket.bind(("0.0.0.0", self.listenerPort))
            listenerSocket.listen(5)

            while True:
                controllerSock, addr = listenerSocket.accept()
                print "after accept "+addr[0]

                # Thread creating loop
                while True:
                    if threading.activeCount() < THREAD_LIMIT:
                        controllerIP = addr[0]  # key - IP client
                        # First Connection by IP  clientIP
                        self.sessionWithDevice = SessionWithDevice(self, controllerSock, addr)
                        self.houseToDevicesTable.addDevice(self.sessionWithDevice)
                        print 'hear------'
                        self.sessionWithDevice.start()
                        self.houseToDevicesTable.start()
                        break
        except socket.error , er_msg:
            error_code = er_msg[0]
            if error_code == 10048:
                print "Port " + str(self.listenerPort) + " is busy#"
            else:
                print str(er_msg) + "#"
        except Exception as er_msg:
            print str(er_msg) + "#"



#endregion


