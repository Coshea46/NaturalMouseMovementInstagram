import csv
import math
import time
import math
from pynput.mouse import Controller, Listener, Button

click_pos = (0, 0)

coordsOfTargetButtonCenter = (1848, 612)  # replace with x,y the correct coords on desktop
radiusTargetButton = 18 # likewise as line above
csvPath = "ouput.csv" # desired csv name here

mouse = Controller()
click_detected = False

# --- CLICK LISTENER SETUP ---
def on_click(x, y, button, pressed):
    global click_detected, click_pos
    if pressed:
        click_detected = True
        click_pos = (x, y)  # store the coordinates

listener = Listener(on_click=on_click)
listener.start()  # Start listener in background


# check in circle

def inTargetCircle(xCoord, yCoord):
    a, b = coordsOfTargetButtonCenter
    distance = math.sqrt((xCoord - a)**2 + (yCoord - b)**2)
    print(f"Clicked at ({xCoord}, {yCoord}) — Target at ({a}, {b}) — Distance: {distance:.2f}")
    return distance <= radiusTargetButton

# writing to csv functions

def writePostBreak(csvPath,clickCount):
    with open(csvPath, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(["clickcount: "+str(clickCount)])
        writer.writerow(["nextpost"])

def writePoint(csvPath,x,y,timeStamp):
    with open(csvPath, 'a', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow([x,y,timeStamp])
        



foreverLoop = True
clickCount = 0

while foreverLoop:
    point_x, point_y = mouse.position 
    writePoint(csvPath,point_x, point_y,time.time())
    if click_detected:
        # If clicking the nextpost button
        xClick, yClick = click_pos
        if inTargetCircle(xClick,yClick):
            writePostBreak(csvPath, clickCount)
            clickCount = 0
        else:
            clickCount = clickCount + 1

    click_detected = False
        
    time.sleep(0.05) # sleep of 0.05 seconds between point recordings so I don't kill the cpu
            
            
        