# -*- coding: utf-8 -*-

import json, threading
import datetime
from DevicesManager import *
import json, ast
import statsManager, time

import  socket, os, string


def updatingJsonFileOfHouseData(updated_string_for_json):
    print updated_string_for_json
    with open('data.json', 'w') as outfile:
        json.dump(str(updated_string_for_json), outfile)
    #just checking:
    with open('data.json') as infile:
       print json.load(infile)


def updatingDeviceStatusInJsonFile(houseToDevices, current_status, device):
    print current_status
    houseToDevices.get(device).get("status").update("current_status")
    with open('data.json') as infile:
       file = json.load(infile)
       file.get(device).get("status").update(current_status)
    with open('data.json', 'w') as outfile:
        json.dump(str(file), outfile)
    #just checking:
    with open('data.json') as infile:
       print json.load(infile)


def clean_string(string_given):
    cleanstring = ""
    for letter in string_given:
        if ('a' < letter < 'z') or letter == '_':
            cleanstring += letter
    return cleanstring


class HouseToDevicesTable(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        #dictionary storage controller info
        self.houseToDevices = {}
        self.controllerNumConnected = 0
        #0 means off, 1 means on:
        self.houseToDevicesDataRelevantForPhone = {}
        self.mode = 0

    #controller

    def run(self):
        while True:
            for device, fields in self.houseToDevicesDataRelevantForPhone.iteritems():
                for key, field in fields.iteritems():
                    print key
                    print field
                    if key == 'temp_activate_outside' and field != 'none':
                        #return the temperature in the server area
                        #print statsManager.get_temperature()
                        if int(statsManager.get_temperature()) > int(field):
                            self.houseToDevices.get(device, "default").get('communication', "default").send('1')
                            updatingDeviceStatusInJsonFile(self,"1", device)
                            print int(field)

                    elif key == 'turning_on_diary':
                        time_limits = str(field).split(",")
                        print time_limits
                        time_limits = time_limits[statsManager.get_index_according_date()]
                        print time_limits
                        time_limits = time_limits.split(".")
                        print time_limits
                        for time_lim in time_limits:
                            print time_lim
                            #clean up string from what is not number and checks if there are any number:
                            if filter(str.isdigit, time_lim) != '':
                                time_lim = time_lim.split("-")
                                print time_lim[0] +" "+ time_lim[1]
                                if int(time_lim[0]) < statsManager.get_current_time() < int(time_lim[1]):
                                    print "sendedddddddd"
                                    self.houseToDevices.get(device, "default").get('communication', "default").send('1')
                                    updatingDeviceStatusInJsonFile(self,"1", device)
            #loop will run each 300 sec - 5 min
            print "runningggggggggg"
            time.sleep(50)


    def addDevice(self, sessionWithDevice):
        self.controllerNumConnected += 1
        default_name = 'device'+str(self.controllerNumConnected)
        #build dictionary for each controller which include session with device and other properties
        self.houseToDevices.update({default_name: {'communication': sessionWithDevice, 'name': default_name, 'status': sessionWithDevice.status, 'temp_activate_outside': 'none', 'turning_on_diary': " , , , , , , , "}})
        self.houseToDevicesDataRelevantForPhone.update({default_name: {'name': default_name,'status': sessionWithDevice.status, 'temp_activate_outside': 'none', 'turning_on_diary': " , , , , , , , "}})
        updatingJsonFileOfHouseData(self.houseToDevicesDataRelevantForPhone)

    def updatingDeviceStatusInJsonFile(self, current_status, device):
        print current_status
        self.houseToDevices[device]["status"] = current_status
        self.houseToDevicesDataRelevantForPhone[device]["status"] = current_status
        print "yeahhh"
        updatingJsonFileOfHouseData(self.houseToDevicesDataRelevantForPhone)

    def updating_house_data_table_relev_for_phone(self):
        with open('data.json') as infile:
            json_object = json.load(infile)

        json_object = ast.literal_eval(json_object)
        for device, fields in json_object.iteritems():
                for key, field in fields.iteritems():
                    x = json_object.get(device,"not work").get(key, "not work")
                    print x
                    self.houseToDevicesDataRelevantForPhone[device][key] = x
                    print self.houseToDevicesDataRelevantForPhone[device][key]