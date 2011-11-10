import math
import urllib
import Image, ImageDraw
def deg2num(lat_deg, lon_deg, zoom):
 lat_rad = math.radians(lat_deg)
 n = 2.0 ** zoom
 xtile = int((lon_deg + 180.0) / 360.0 * n)
 ytile = int((1.0 - math.log(math.tan(lat_rad) + (1 / math.cos(lat_rad))) / math.pi) / 2.0 * n)
 return (xtile, ytile)

def num2deg(xtile, ytile, zoom):
 n = 2.0 ** zoom
 lon_deg = xtile / n * 360.0 - 180.0
 lat_rad = math.atan(math.sinh(math.pi * (1 - 2 * ytile / n)))
 lat_deg = math.degrees(lat_rad)
 return (lat_deg, lon_deg)


def getFileFromWeb(url, localfile):
 u = urllib.urlopen(url)
 localfile = open(localfile, 'w')
 localfile.write(u.read())
 localfile.close()

def getTileUrl(x, y, zoom):
 return 'http://tile.openstreetmap.org/' + str(zoom) +  '/' + str(x) + '/' + str(y) + '.png'

def getImage(x,y,zoom):
 localfile = str( str(x) + '-' + str(y) + '.png')
 print('Getting image: ' + getTileUrl(x,y,zoom))
 getFileFromWeb(getTileUrl(x,y,zoom),localfile)
 
def getSeveralImages(latMin, latMax, lonMin, lonMax, zoom):
 print(str(lonMin))
 print(str(lonMax))
 print(str(latMin))
 print(str(latMax))



 lonMin, latMin = deg2num(lonMin, latMin, zoom)
 lonMax, latMax = deg2num(lonMax, latMax, zoom)
  
 print('getting From Lat: ' + str(latMin) + ' - ' + str(latMax))
 print('getting From Long: ' + str(lonMin) + ' - ' + str(lonMax))
 a = lonMin
 b = latMax
 while a <= lonMax:
  while b <= latMin:
   getImage(a,b,zoom)
   print (str(b) + '-' + str(latMin))
   b += 1
 a+=1
 
