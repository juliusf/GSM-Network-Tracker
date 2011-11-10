import tiles
#filehandle = urllib.urlopen('http://tile.openstreetmap.org/18/137751/90309.png'
latMax, longMax  = 48.74296201, 9.172278145
latMin, longMin = 48.6, 8.9

#x, y = tiles.deg2num(lat, lang, 18)

#tiles.getFileFromWeb(tiles.getTileUrl(x, y, 18), str( str(x) + '-' + str(y) + '.png'))
#tiles.getFileFromWeb(tiles.getTileUrl(x+1,y,18), str( str(x+1) + '-' + str(y) + '.png'))


tiles.getSeveralImages(latMin, latMax, longMin, longMax, 18)


