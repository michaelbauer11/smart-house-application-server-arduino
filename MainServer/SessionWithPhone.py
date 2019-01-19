#region ----------   ABOUT   -----------------------------
"""
##################################################################
# Created By:  Michael                                           #
# Date: 20/09/2014                                               #
# Name: Server session with any clients                          #
# Version: 1.0                                                   #
# Windows Tested Versions: Win 7 32-bit                          #
# Python Tested Versions: 2.7 32-bit                             #
# Python Environment  : PyCharm                                  #
# pyCrypto Tested Versions: Python 2.7 32-bit                    #
##################################################################
"""
#endregion

#region ----------   IMPORTS   -----------------------------
import threading, json, ast
import time
from HouseToDevicesTable import updatingJsonFileOfHouseData
import HouseToDevicesTable
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
class  SessionWithPhone(threading.Thread):

    # -----  F U N C T I O N S  -----
    #-----------------------------------------------------------------------------------------------
    #  class definition function
    #-----------------------------------------------------------------------------------------------
    def __init__(self, phoneManager, phone_sock, addr ):
        threading.Thread.__init__(self)
        self.phoneManager = phoneManager
        # new open socket  for client
        self.phone_sock = phone_sock
        # address connection : IP and Port
        self.addr = addr

    #-----------------------------------------------------------------------------------------------
    # the main function of the THREAD sessionWithClient class  
    #-----------------------------------------------------------------------------------------------  
    def run(self):
        try:

            #user login
            with open('users.json') as users_info:
                users_table = json.load(users_info)
                users_table = ast.literal_eval(users_table)

            identity_verified = False
            while not identity_verified:
                input = self.phone_sock.recv(LEN_UNIT_BUF)
                print input
                input = input.split('#')
                #self.phone_sock.send("confirmed"+"\n")
                #identity_verified = True
                if input[0] == "signin":
                    for user_name, user_passward in users_table.iteritems():
                        if user_name == input[1] and user_passward == input[2]:
                            identity_verified = True
                            self.phone_sock.send("confirmed"+"\n")
                    if not identity_verified:
                        self.phone_sock.send("unconfirmed"+"\n")
                elif input[0] == "signup":
                    if input[3] == "secretkey":
                        self.phone_sock.send("vconfirmed"+"\n")
                        identity_verified = True
                        users_table[str(input[1])] = str(input[2])
                        with open('users.json', 'w') as users_table_out:
                            json.dump(str(users_table), users_table_out)
                    else:
                        self.phone_sock.send("xunconfirmed"+"\n")
            time.sleep(1)
            #setup data for connected phone
            print "--------------------------------"
            with open('data.json') as outfile:
                dataStringed=""
                dataStringed += json.load(outfile)
                dataStringed += "\n"
                print dataStringed
                self.phone_sock.send(dataStringed)

            while True:
                # Wait message beginning of communication from client
                data = self.phone_sock.recv(LEN_UNIT_BUF)
                #print self.phoneManager.deviicesManager.houseToDevicesTable.houseToDevices
                print data
                data = data.split('#')
                if  data[0].startswith('device'):
                    #send action to controller and update his status:
                    print data[0]
                    print data[1]
                    self.phoneManager.deviicesManager.houseToDevicesTable.houseToDevices.get(data[0] , "not working").get('communication', "not working").send(data[1])
                    print 'sent'
                    #update dictionary:
                    self.phoneManager.deviicesManager.houseToDevicesTable.updatingDeviceStatusInJsonFile(data[1], data[0])
                    act = str(data[1:])
                    self.phone_sock.send(act+"\n")

                elif data[0].startswith('update'):
                    updatingJsonFileOfHouseData(data[1])
                    self.phoneManager.deviicesManager.houseToDevicesTable.updating_house_data_table_relev_for_phone()
                    print "updated"

                elif data[0].startswith('disconnect'):
                    self.phone_sock.close()





            self.phone_sock.close()

        except Exception as e:
            print str(e) + ERROR_EXCEPT + "  from " + str(self.addr[0])



   
#endregion   

