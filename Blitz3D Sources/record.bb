Graphics 640,480,16,2


Global rec_filehandle
num=0
timer=CreateTimer(60)
Repeat 
 If KeyHit(59) ;F1
  Record("File",0,"test_.txt") 
  isRec=True 
 EndIf  

 If KeyHit(60)
 	If isRec
   		Record("Stop")  
   		isRec=False
  		EndIf
  
 	If isPlay
    	Play("Stop")
  	End
  	EndIf
 EndIf  

 If KeyHit(61)
 	Play("File","test_.txt")
    isPlay=True
 EndIf

 
 If isRec
	If MouseXSpeed()=0
		If MouseYSpeed()=0
			num=num+1
		EndIf
	Else
		click=0
		msx=MouseX()
		msy=MouseY()
		If MouseDown(1) click=1
		If MouseDown(2) click=2
		If MouseDown(3) click=3
	    key=GetScanCode()
		Record("Mouse",CombABC(msx,num,key))
		Record("Mouse",CombABC(msy,click))
		num=0
		
	EndIf
 EndIf

 If isPlay

	If KeyDown(62)
		Play("FF")
 	EndIf

 	If Not Eof(rec_filehandle)	
		If idle = 0 MoveMouse msx,msy
		If idle <>0		
			For i = 0 To idle
				WaitTimer(timer)
				MoveMouse msx,msy
				DebugLog idle+" "+msx+" "+msy
			Next
		EndIf
		tempa=Play("Mouse")
		tempb=Play("Mouse")
		msx=GetA(tempa):idle=GetB(tempa):key=GetC(tempa)
		msy=GetA(tempb):click=GetB(tempb)
		DebugLog idle+" "+msx+" "+msy+" "+click		
	Else
		Play("Stop")
		MoveMouse msx,msy
 		MoveMouse MouseX(),MouseY()
		isPlay=False
    EndIf

 EndIf  

 Flip
 WaitTimer(timer)
Until KeyHit(1)

Function Record(cmd$,param_num#=0,param$="")
 If cmd$="File" rec_filehandle=WriteFile(param$)
 If cmd$="Stop"
    WriteInt(rec_filehandle,CombABC(MouseX())) 
    WriteInt(rec_filehandle,CombABC(MouseY())) 
 	CloseFile(rec_filehandle)
 EndIf
 If cmd$="Mouse"
 	WriteInt(rec_filehandle,param_num#) 
 EndIf

End Function 


Function Play(cmd$,param$="")
 If cmd$="File" rec_filehandle=ReadFile(param$)
 If cmd$="Mouse" Return ReadInt(rec_filehandle)
 If cmd$="Stop"
    Return 999999 
 	CloseFile(rec_filehandle)
 EndIf
 If cmd$="FF"
    For f=1 To 5
    ReadInt(rec_filehandle)
	ReadInt(rec_filehandle)
    Next
 EndIf
End Function

Function GetScanCode()
	For a = 1 To 88
		If KeyDown(a) Then Return a
	Next
	Return False
End Function

Function GetA(n)
	Return Int(n/1000000)
End Function 

Function GetB(n)
	Return Int((n-GetA(n)*1000000)/100)
End Function

Function GetC(n)
	Return n-(GetA(n)*1000000)-(GetB(n)*100)
End Function

Function CombABC(a=0,b=0,c=0)
	Return (a*1000000)+(b*100)+c
End Function 
	