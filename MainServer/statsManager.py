# -*- coding: utf-8 -*-
import datetime, time
from datetime import date
import pywapi
import json
import urllib2

#return the city of given ip


def get_city():
    ip_of_the_controller = '46.120.8.162'#may be replaced in the future
    j = json.loads(urllib2.urlopen('http://ip-api.com/json/'+ip_of_the_controller).read())
    return j['city']


def get_temperature():
    #return object with weather conditions in specific city - add it to the general database:
    ####################################################################################################################
    cityName = str(get_city())
    #this will give you a dictionary of all cities in the world with this city's name Be specific (city, country)!
    lookup = pywapi.get_location_ids(cityName)
    #workaround to access last item of dictionary
    for i in lookup:
        location_id = i
    #location_id now contains the city's code
    weather_com_result = pywapi.get_weather_from_weather_com(location_id)
    print ("Weather.com says: It is " + str(weather_com_result['current_conditions']['text'].upper()) + " and " + str(weather_com_result['current_conditions']['temperature']) + "°C now in " + cityName)
    ####################################################################################################################
    return weather_com_result['current_conditions']['temperature']

#find current season - can be upload in database when needed:
def get_season(now):
    # dummy leap year to allow input X-02-29 (leap day)
    Y = 2017
    seasons = [('winter', (date(Y,  1,  1),  date(Y,  3, 20))),
           ('spring', (date(Y,  3, 21),  date(Y,  6, 20))),
           ('summer', (date(Y,  6, 21),  date(Y,  9, 22))),
           ('autumn', (date(Y,  9, 23),  date(Y, 12, 20))),
           ('winter', (date(Y, 12, 21),  date(Y, 12, 31)))]

    if isinstance(now, datetime):
        now = now.date()
    now = now.replace(year=Y)
    return next(season for season, (start, end) in seasons
                if start <= now <= end)

def get_index_according_date():
        day = time.strftime("%A")
        if day == "Sunday":
            return 0
        elif day == "Monday":
            return 1
        elif day == "Tuesday":
            return 2
        elif day == "Wednesday":
            return 3
        elif day == "Thursday":
            return 4
        elif day == "Friday":
            return 5
        return 6

def get_current_time():
    current_time = int(time.strftime("%H"))*100
    current_time += int(time.strftime("%M"))
    print current_time
    return  current_time