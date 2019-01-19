# -*- coding: utf-8 -*-
from DevicesManager import *
from PhoneManager import *
import datetime


def main():
    """
    Add Documentation here
    """
    deviicesManager = DevicesManager()
    deviicesManager.start()
    phoneManager = PhoneManager(deviicesManager)
    phoneManager.start()


if __name__ == '__main__':
    main()