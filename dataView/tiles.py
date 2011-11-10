import math
import urllib
import Image, ImageDraw
import sys
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
 localfile = str(str(zoom) + '-' + str(x) + '-' + str(y) + '.png')
 #print('Getting image: ' + getTileUrl(x,y,zoom))
 getFileFromWeb(getTileUrl(x,y,zoom),localfile)
 
def getSeveralImages(latMin, latMax, lonMin, lonMax, zoom):

 xMin, yMin = deg2num(latMin, lonMin, zoom)
 xMax, yMax = deg2num(latMax, lonMax, zoom)
  
 a = xMin
 b = yMax
 nr = 0
 while a <= xMax:
  while b <= yMin:
   nr +=1
   print"Download: %d of %d" % (nr, (xMax-xMin)*(yMin-yMax) + 1)
   getImage(a,b,zoom)
   b += 1
  else: 
   b = yMax
   a +=1
#Switch yMin/yMax order. This is introduced by the conversion from Lat / Long to X/Y-Coordinate System. 
 temp = yMin
 yMin = yMax
 yMax = temp
 return xMin,xMax,yMin,yMax

def stitchImages(xMin, xMax,yMin,yMax,zoom):
 dimX=(xMax - xMin + 1)*256
 dimY=(yMax - yMin + 1)*256
 print'Dimensions: dimX: %d dimY: %d' % (dimX,dimY)	
 finalImage = Image.new("RGB", (dimX,dimY))
 
 xOffset = 0;
 yOffset = 0;
 a = xMin
 b = yMin
 while a <= xMax:
  while b <=yMax:
   tempImage = Image.open( str(zoom) + '-' + str(a) + '-' + str(b) + '.png')
   finalImage.paste(tempImage, (xOffset,yOffset))
   print"pasted: x=%d y=%d xOffset=%d yOffset=%d" % (a,b,xOffset,yOffset)
   yOffset += 256
   b +=1
  else:
   xOffset += 256
   yOffset = 0
   b=yMin
   a+= 1
 
 finalImage.save('out.png', 'PNG')



