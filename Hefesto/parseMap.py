#!/usr/bin/env python
# -*- coding: utf-8 -*-

f = open('mapaMarines.map', 'r')
for line in f:
    print line.replace(',','.'),