Import "bbtype.bmx"
Import "bbvkey.bmx"

Graphics3D 1024,768,32,1
Include "constants.bmx"
Include "globals.bmx"
Include "mouseevents.bmx"
Include "viewport.bmx"


tex=CreateTexture( 64,64 )
SetBuffer TextureBuffer( tex )
Color 255,0,0;Rect 0,0,32,32;Rect 32,32,32,32
Color 255,128,0;Rect 32,0,32,32;Rect 0,32,32,32
SetBuffer BackBuffer()
Color 255,255,255

Global font=LoadFont("Microsoft Sans Serif",12,True)

cone=CreateCone(20)
EntityTexture cone,tex

sphere=CreateSphere(10)
PositionEntity sphere,2,0,0
EntityTexture sphere,tex

cylinder=CreateCylinder(20)
PositionEntity cylinder,-2,0,0
EntityTexture cylinder,tex

light=CreateLight()
TurnEntity light,45,45,0


LoadSplash()

Reset()
While Not VKeyHit(1)


	
	Render()
    


Wend

End

Function Reset()
	BRActive=True
	zoomTL#=.1;zoomTR#=.1;zoomBL#=.1;zoomBR#=1;inc#=.01
	CameraZoom TL,zoomTL#;CameraZoom TR,zoomTR#;CameraZoom BL,zoomBL#CameraZoom BR,zoomBR#
	RenderWorld
	TurnEntity pivotBR,30,45,0
End Function

Function LoadSplash()
	splash=LoadImage("splash.png")
	DrawImage interface,0,0
	MidHandle splash
	DrawImage splash,dev.blitz3d.GraphicsWidth()/2,dev.blitz3d.GraphicsHeight()/2
	Flip
	Delay 4000
	FreeImage splash
End Function 

Function Render2D()
	Color 255,255,255
	Text 272,53,"Front"
	Text 272,63,MouseX()+" "+MouseY()+" "+zoom#
	Text 651,53,"Top"
	Text 272,394,"Left"
	Text 651,394,"Perspective"
    Text 272,80,TrisRendered()
	If TLActive=True Text 272,90,"Active"
	If TRActive=True Text 651,90,"Active"
	If BLActive=True Text 272,414,"Active"
	If BRActive=True Text 651,414,"Active"	
	
	DrawImage interface,0,0
	If TLwire=True
		DrawImage buttonIN17,17*18,17*2
	Else
		DrawImage buttonIN17,17*17,17*2
	EndIf
	If TRwire=True
		DrawImage buttonIN17,17*40,17*2
	Else
		DrawImage buttonIN17,17*39,17*2
	EndIf
	If BLwire=True
		DrawImage buttonIN17,17*18,17*22
	Else
		DrawImage buttonIN17,17*17,17*22
	EndIf
	If BRwire=True
		DrawImage buttonIN17,17*40,17*22
	Else
		DrawImage buttonIN17,17*39,17*22
	EndIf
	MouseEvent()
	MouseAction()	
	
	DrawImage arrow,MouseX(),MouseY()
	SetFont font
	Color 0,0,0
	Text 0,0,"Test"
End Function

Function Render()

	
	If TLActive = True
		CameraClsMode TL,1,1
		RenderWorld
		CameraClsMode TL,0,0
		RenderWorld
		DrawGridY(TL)
	Else
		CameraClsMode TL,1,1
		RenderWorld
	EndIf
	
	If TRActive = True
		CameraClsMode TR,1,1
		RenderWorld
		CameraClsMode TR,0,0
		DrawGridX(TR)
	Else
		CameraClsMode TR,1,1
		RenderWorld
	EndIf
	
	If BLActive = True
		CameraClsMode BL,1,1
		RenderWorld
		CameraClsMode BL,0,0
		DrawGridZ(BL)
	Else
		CameraClsMode BL,1,1
		RenderWorld
	EndIf
	
	If BRActive = True
		CameraClsMode BR,1,1
		RenderWorld
		CameraClsMode BR,0,0
		RenderWorld
		DrawGridX(BR)
	Else
		CameraClsMode BR,1,1
		RenderWorld
	EndIf
	

	RenderWorld
	Render2D()
	Flip
	SetBuffer FrontBuffer()
	WireFrame 1
	ShowEntity TL
	HideEntity TR
	HideEntity BL
	HideEntity BR
	RenderWorld
	Flip
	WireFrame 0
	HideEntity TL
	ShowEntity TR
	ShowEntity BL
	ShowEntity BR
	RenderWorld
	SetBuffer BackBuffer()
End Function

