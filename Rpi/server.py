#!/usr/bin/env python

import socket
import serial
from time import sleep

host = ''
port = 9093
backlog = 5
size = 1024
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ser = serial.Serial('/dev/tty.usbmodem1411', 9600)
sleep(3)
ser.write('s');
s.bind((host,port))
s.listen(backlog)
while 1:
	client, address = s.accept()
	data = client.recv(size)
	if data:
		ser.write(data)
		client.send(data)
	client.close()
