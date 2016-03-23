

	Graphics3D 1024,768,32,1
	SetBuffer BackBuffer()
	
camPiv = CreatePivot()
camera = CreateCamera(camPiv)
	light=CreateLight()
	PositionEntity camera,0,0,-30

	
	tex=CreateTexture( 64,64 )
SetBuffer TextureBuffer( tex )
Color 255,0,0:Rect 0,0,32,32:Rect 32,32,32,32
Color 255,128,0:Rect 32,0,32,32:Rect 0,32,32,32
SetBuffer BackBuffer()
Color 255,255,255
	
	cone=CreateCone(20)
EntityTexture cone,tex

sphere=CreateSphere(10)
PositionEntity sphere,2,0,0
EntityTexture sphere,tex

cylinder=CreateCylinder(20)
PositionEntity cylinder,-2,0,0
EntityTexture cylinder,tex


WireFrame 0
	  
	size=10
	zoom#=.1
;*** The loop begins
   While Not KeyHit(1)
   
   	
   	CameraControl(camera,camPiv)
   	;reset mouse vars
   	mx#=0 : mz#=0 : my#=0
   	RenderWorld

;*** NOW USE THE FUNCTIONS

	
	Color 30,30,30
 	For z = size To -size Step -1
		Segments3d(0,size,z,0,-size,z,camera,15)
		Segments3d(0,z,size,0,z,-size,camera,15)	
	Next
	Color 60,60,60
	For z = size To -size Step -5
		Segments3d(0,size,z,0,-size,z,camera,15)
		Segments3d(0,z,size,0,z,-size,camera,15)		
	Next
	Color 100,100,100
	For z = size To -size Step -10
		Segments3d(0,size,z,0,-size,z,camera,15)
		Segments3d(0,z,size,0,z,-size,camera,15)
	Next
	Color 255,0,0
    Line3d(0,0,0,5,0,0,camera)
	text3d (4.5,.5,0,"X",camera)
	Color 0,255,0
	Line3d(0,0,0,0,5,0,camera)
	text3d (.5,4.5,0,"Y",camera)
	Color 0,0,255
	Line3d(0,0,0,0,0,5,camera)
	text3d (.5,0,4.5,"Z",camera)

   	Flip
   Wend
;*** end loop


;*********************************************************************************

End

Function CameraControl(camera,camPiv)
	; --- camera controls
	scrollwheel = MouseZSpeed()
	If MouseDown(1) Then 
		TurnEntity(camPiv, MouseYSpeed(),-MouseXSpeed(),0)
	Else If scrollwheel <> 0 Then 
		MoveEntity(camera, 0,0,scrollwheel*3)
	Else
		dummy = MouseYSpeed():dummy = MouseXSpeed():dummy = MouseZSpeed() ; prevent mousespeed blips.
	End If
	MoveMouse GraphicsWidth()/2,GraphicsHeight()/2
End Function


;*********************************************************************************
;*** Plot a 3d Pixel
;*********************************************************************************
Function plot3d(x#,y#,z#, camera)
	CameraProject camera,x#,y#,z# 
	If ProjectedZ() Then Plot ProjectedX# ( ) ,ProjectedY# ( )
End Function

Function text3d(x#,y#,z#,txt$, camera)
	CameraProject camera,x#,y#,z# 
	If ProjectedZ() Then Text ProjectedX# ( ) ,ProjectedY# ( ),txt$,True,True
End Function

;*********************************************************************************
;*** Draw a fast single 3d line 
;*********************************************************************************
Function Line3d(x0#,y0#,z0#,x1#,y1#,z1#, camera)
	CameraProject (camera,x0,y0,z0)
	px0#=ProjectedX# ( )
	py0#=ProjectedY# ( ) 
	pz0#=ProjectedZ# ( ) 

	CameraProject (camera,x1,y1,z1)
	If ProjectedZ#( ) >0 And pz0>0 Then Line px0,py0,ProjectedX# ( ) ,ProjectedY# ( )
	End Function

;*********************************************************************************
;*** Draw a multi segments 3d line 
;*********************************************************************************
Function segments3d(x0#,y0#,z0#,x1#,y1#,z1#, camera, segments=1)

	stepx#=(x1-x0)/segments
	stepy#=(y1-y0)/segments
	stepz#=(z1-z0)/segments
	x1 = x0+stepx#
	y1 = y0+stepy#
	z1 = z0+stepz#

	For t= 1 To segments
		CameraProject (camera,x0,y0,z0)
		px0#=ProjectedX( ) 
		py0#=ProjectedY( ) 
		pz0#=ProjectedZ( ) 

		CameraProject (camera,x1,y1,z1)
		If ProjectedZ#( )>0 And pz0>0 Then Line px0,py0,ProjectedX#( ) ,ProjectedY#( ) 
		x0 = x1 : y0 = y1 : z0 = z1
		x1 = x0+stepx# : y1 = y0+stepy# : z1 = z0+stepz#		
	Next
End Function