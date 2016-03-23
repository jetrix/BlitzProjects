Graphics 640,480,16,1
font=LoadFont("Courier",10)
SetFont font
file=WriteFile("dump")
ClsColor 0,0,100
Cls
For a = 1 To 3000000
WriteInt file,0
WriteInt file,1
WriteInt file,0
WriteInt file,1
Next
Printinfo()

WaitKey()
WaitKey()
WaitKey()

For a = 1 To 6000000
WriteInt file,0
WriteInt file,0
WriteInt file,1
WriteInt file,0
WriteInt file,1
Next
CloseFile file

Graphics 1024,768,16,1
Graphics 640,480,16,1
For a = 1 To 100
ClsColor 255,0,0
Cls
Delay 1
ClsColor 0,255,0
Cls
Delay 1
Cls
ClsColor 0,0,255
Cls
Delay 1
printinfo()
Next

For a = 1 To 100
ClsColor 100,0,0
Cls
Delay 2
ClsColor 0,100,0
Cls
Delay 1
Cls
ClsColor 0,0,100
Cls
Delay 1
Next

For a = 1 To 50
ClsColor 50,0,0
Cls
Delay 1
Cls
ClsColor 0,50,0
Cls
Delay 1
Cls
ClsColor 0,0,50
Cls
Delay 1
Cls
Next

For a = 255 To 1 Step -1
ClsColor 255/100,255/100,255/100
Cls
Delay 1
ClsColor a,a,a
Cls
Delay 1
ClsColor 10,10,10
Cls
Delay 1
Next
Delay 600

Graphics 480,360,16,1
font=LoadFont("Courier",10)
SetFont font
Print "Restarting...."
Delay 2000

Function PrintInfo()
Print 
Print "A problem has been detected and windows has been shutdown to prevent damage"
Print "to your computer."
Print
Print "If this is your first time you've seen this Stop error screen,"
Print "restart your computer. If this screen appears again, follow"
Print "these steps:"
Print 
Print "Check to be sure you have the adequate disk space. If the driver is"
Print "identified in the Stop message, disable the driver or check"
Print "with the manufacturer for driver updates. Try changing the video"
Print "adapters."
Print 
Print "Check with your hardware vendor for BIOS updates. Disable"
Print "BIOS memory options such as caching or shadowing. If you need"
Print "to use Safe Mode to remove or disable components, restart your"
Print "computer, press F8 to select Advanced Startup options, and then"
Print "select Safe Mode."
Print
Print "Technical Information:"
Print
Print "*** STOP: 0x00D8007E (0xC0000005,0x8056CDCE,0xF8F35B9C,0xF8F35B9C)"
Print
Print
Print "*** kernel32.dll - Address F8F35B9C base at 0xF8F35B9C, Datestamp 00000000"
Print "*** user32.dll - Address F8F35B9C base at 0xF8F35B9C, Datestamp 00000000"
Print 
Print "Beginning dump of physical memory"
Print "Physical memory dumped complete."
Print "Contact your system administrator or technical support group for further"
Print "assistance."
Print
Print "System halted."
End Function 