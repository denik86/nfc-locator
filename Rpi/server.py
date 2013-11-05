#!/usr/bin/env python

import socket
import serial

host = ''
port = 9093
backlog = 5
size = 1024
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
ser = serial.Serial('/dev/tty.usbserial', 9600)
s.bind((host,port))
s.listen(backlog)
while 1:
	client, address = s.accept()
	data = client.recv(size)
	if data:
		ser.write(data)
		client.send(data)
	client.close()
