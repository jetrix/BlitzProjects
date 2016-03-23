Function MouseAction()
	If pan_TL = True			
		    DrawImage buttonIN17,17*34,17*2	   	
			msx#=MouseXSpeed()*-.1
			msy#=MouseYSpeed()*.1					
			MoveEntity TL,msx#,msy#,0
			MoveMouse (17*34)+(17/2),(17*2)+(17/2)
	EndIf
	If zoom_TL = True
		    DrawImage buttonIN17,17*35,17*2
			msy=-MouseYSpeed()
			zoomTL#=zoomTL#+(msy*inc#)
			inc#=zoomTL#*2*.01
			If zoomTL#<=.001 Then zoomTL#=.001
			CameraZoom TL,zoomTL#
			MoveMouse (17*35)+(17/2),(17*2)+(17/2)
	EndIf
	If rot_TL = True
		    DrawImage buttonIN17,17*36,17*2
			msx=msx+MouseXSpeed()*.3
			msy=msy-MouseYSpeed()*.3
			If TL<>front_cam And TL<>top_cam And TL<>left_cam
				TurnEntity(pivotTL, msy,msx,0)
			EndIf
			MoveMouse (17*36)+(17/2),(17*2)+(17/2)
	EndIf
	If pan_TR = True
		DrawImage buttonIN17,17*56,17*2
		msx#=MouseXSpeed()*-.1
		msy#=MouseYSpeed()*.1					
		MoveEntity TR,msx#,msy#,0
		MoveMouse (17*56)+(17/2),(17*2)+(17/2)
 
	EndIf
	If zoom_TR = True
		DrawImage buttonIN17,17*57,17*2	
		msy=-MouseYSpeed()
		zoomTR#=zoomTR#+(msy*inc#)
		inc#=zoomTR#*2*.01
		If zoomTR#<=.001 Then zoomTRS#=.001
		CameraZoom TR,zoomTR#
		MoveMouse (17*57)+(17/2),(17*2)+(17/2)
	EndIf
	If rot_TR = True
		DrawImage buttonIN17,17*58,17*2	 
	EndIf
	If rot_BR = True
		    DrawImage buttonIN17,17*58,17*22
			msx#=msx#+MouseXSpeed()*.3
			msy#=msy#-MouseYSpeed()*.3
			If BR<>front_cam And BR<>top_cam And BR<>left_cam
				RotateEntity(pivotBR, EntityPitch#(pivotBR)+msy#,EntityYaw#(pivotBR)+msx#,0)
			EndIf
			MoveMouse (17*58)+(17/2),(17*22)+(17/2)
	EndIf
	
	If BRActive=True
		msz# = MouseZSpeed()
	    If msz# <> 0 Then 
			MoveEntity(BR, 0,0,msz#*3)
		End If
	msz#=0
	EndIf
End Function

Function MouseEvent()
	If MouseDown(1)=True ; if leftclick
		If Not buttonActive
			If MouseX()>17*15 And MouseX()<17*38
				If MouseY()>17*2 And MouseY()<17*22
				TLActive=True
				TRActive=False
				BLActive=False
				BRActive=False
				EndIf
			EndIf
		EndIf 
		If Not buttonActive
			If MouseX()>17*38 And MouseX()<17*60
				If MouseY()>17*2 And MouseY()<17*22
				TLActive=False
				TRActive=True
				BLActive=False
				BRActive=False
				EndIf
			EndIf
		EndIf 
		If Not buttonActive
			If MouseX()>17*15 And MouseX()<17*38
				If MouseY()>17*22 And MouseY()<17*42
				TLActive=False
				TRActive=False
				BLActive=True
				BRActive=False
				EndIf
			EndIf
		EndIf
		If Not buttonActive
			If MouseX()>17*38 And MouseX()<17*60
				If MouseY()>17*22 And MouseY()<17*42
				TLActive=False
				TRActive=False
				BLActive=False
				BRActive=True
				EndIf
			EndIf
		EndIf  
		If Not buttonActive
			If MouseX()>17*34 And MouseX()<17*35
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*34)+(17/2),(17*2)+(17/2)
				pan_TL=True
				EndIf
			EndIf
		EndIf 
		
		If Not buttonActive
			If MouseX()>17*35 And MouseX()<17*36
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*35)+(17/2),(17*2)+(17/2)
				zoom_TL=True			
				EndIf	
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*36 And MouseX()<17*37
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*36)+(17/2),(17*2)+(17/2)
				rot_TL=True			
				EndIf	
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*56 And MouseX()<17*57
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*56)+(17/2),(17*2)+(17/2)
				pan_TR=True
				EndIf
			EndIf
		EndIf 
		
		If Not buttonActive
			If MouseX()>17*57 And MouseX()<17*58
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*57)+(17/2),(17*2)+(17/2)
				zoom_TR=True			
				EndIf	
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*58 And MouseX()<17*59
				If MouseY()>17*2 And MouseY()<17*3
				MoveMouse (17*58)+(17/2),(17*2)+(17/2)
				rot_TR=True			
				EndIf	
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*57 And MouseX()<17*58
				If MouseY()>17*22 And MouseY()<17*23
				MoveMouse (17*57)+(17/2),(17*22)+(17/2)
				zoom_BR=True
				EndIf
			EndIf
		EndIf 
	
		If Not buttonActive
			If MouseX()>17*58 And MouseX()<17*59
				If MouseY()>17*22 And MouseY()<17*23
				MoveMouse (17*58)+(17/2),(17*22)+(17/2)
				rot_BR=True
				EndIf
			EndIf
		EndIf 

		If Not buttonActive
			If MouseX()>34*0 And MouseX()<34*1
				If MouseY()>34*0 And MouseY()<34*1
				DrawImage buttonIN34,34*0,34*0
				EndIf
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*17 And MouseX()<17*18
				If MouseY()>17*2 And MouseY()<17*3
				TLwire=False
				EndIf
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*18 And MouseX()<17*19
				If MouseY()>17*2 And MouseY()<17*3
				TLwire=True
				EndIf
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*39 And MouseX()<17*40
				If MouseY()>17*2 And MouseY()<17*3
				TRwire=False
				EndIf
			EndIf
		EndIf
		
		If Not buttonActive
			If MouseX()>17*40 And MouseX()<17*41
				If MouseY()>17*2 And MouseY()<17*3
				TRwire=True
				EndIf
			EndIf
		EndIf
		
		buttonActive=True
	EndIf
	
	If MouseDown(1)=False
		zoom_TL=False
		pan_TL=False
		rot_TL=False
		max_TL=False
		zoom_TR=False
		pan_TR=False
		rot_TR=False
		max_TR=False
		zoom_BL=False
		pan_BL=False
		rot_BL=False
		max_BL=False
		zoom_BR=False
		pan_BR=False
		rot_BR=False
		max_BR=False
		
		buttonActive=False 
	EndIf
	
	
End Function