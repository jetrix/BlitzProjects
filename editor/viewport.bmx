Function Text3d(x#,y#,z#,txt$, camera)
	CameraProject camera,x#,y#,z# 
	If camera=TL
		If ProjectedZ#( ) Then Text ProjectedX#()+(17*16),ProjectedY#()+(17*3),txt$,True,True
	ElseIf camera=TR
		If ProjectedZ#( ) Then Text ProjectedX#()+(17*38),ProjectedY#()+(17*3),txt$,True,True
	ElseIf camera=BL
		If ProjectedZ#( ) Then Text ProjectedX#()+(17*16),ProjectedY#()+(17*23),txt$,True,True
	ElseIf camera=BR
		If ProjectedZ#( ) Then Text ProjectedX#()+(17*38),ProjectedY#()+(17*23),txt$,True,True
	ElseIf camera=Full
		If ProjectedZ#( ) Then Text ProjectedX#()+(17*16),ProjectedY#()+(17*3),txt$,True,True
	EndIf
End Function

Function Line3d(x0#,y0#,z0#,x1#,y1#,z1#, camera)
	CameraProject (camera,x0,y0,z0)
	px0#=ProjectedX# ( ) 
	py0#=ProjectedY# ( ) 
	pz0#=ProjectedZ# ( ) 

	CameraProject (camera,x1,y1,z1)
	If camera=TL
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*3),ProjectedX#()+(17*16),ProjectedY#()+(17*3)
	ElseIf camera=TR
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*38),py0+(17*3),ProjectedX#()+(17*38),ProjectedY#()+(17*3)
	ElseIf camera=BL
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*23),ProjectedX#()+(17*16),ProjectedY#()+(17*23)
	ElseIf camera=BR
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*38),py0+(17*23),ProjectedX#()+(17*38),ProjectedY#()+(17*23)
	ElseIf camera=Full
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*3),ProjectedX#()+(17*16),ProjectedY#()+(17*3)
	EndIf
End Function

Function Segments3d(x0#,y0#,z0#,x1#,y1#,z1#, camera, segments=1)

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
		If camera=TL
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*3),ProjectedX#()+(17*16),ProjectedY#()+(17*3)
	ElseIf camera=TR
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*38),py0+(17*3),ProjectedX#()+(17*38),ProjectedY#()+(17*3)
	ElseIf camera=BL
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*23),ProjectedX#()+(17*16),ProjectedY#()+(17*23)
	ElseIf camera=BR
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*38),py0+(17*23),ProjectedX#()+(17*38),ProjectedY#()+(17*23)
	ElseIf camera=Full
		If ProjectedZ#( ) >0 And pz0>0 Then Line px0+(17*16),py0+(17*3),ProjectedX#()+(17*16),ProjectedY#()+(17*3)
	EndIf
		x0 = x1 ; y0 = y1 ; z0 = z1
		x1 = x0+stepx# ; y1 = y0+stepy# ; z1 = z0+stepz#		
	Next
End Function


Function DrawGridY(view)
	size=25
	Color 30,30,30
 	For y = size To -size Step -1
		Segments3d(-size,y,0,size,y,0,view,15)
		Segments3d(y,-size,0,y,size,0,view,15)	
	Next
	Color 60,60,60
	For y = size To -size Step -5
		Segments3d(-size,y,0,size,y,0,view,15)
		Segments3d(y,-size,0,y,size,0,view,15)	
	Next
	Color 100,100,100
	For y = size To -size Step -10
		Segments3d(-size,y,0,size,y,0,view,15)
		Segments3d(y,-size,0,y,size,0,view,15)	
	Next
	Color 255,0,0
    Line3d(0,0,0,5,0,0,view)
	Text3d (4.5,.5,0,"X",view)
	Color 0,255,0
	Line3d(0,0,0,0,5,0,view)
	Text3d (.5,4.5,0,"Y",view)
	Color 0,0,255
	Line3d(0,0,0,0,0,5,view)
	Text3d (.5,.5,4.5,"Z",view)

End Function 

Function DrawGridX(view)
	size=25
	Color 30,30,30
 	For x = size To -size Step -1
		Segments3d(-size,0,x,size,0,x,view,15)
		Segments3d(x,0,-size,x,0,size,view,15)	
	Next
	Color 60,60,60
 	For x = size To -size Step -5
		Segments3d(-size,0,x,size,0,x,view,15)
		Segments3d(x,0,-size,x,0,size,view,15)	
	Next
	Color 100,100,100
 	For x = size To -size Step -10
		Segments3d(-size,0,x,size,0,x,view,15)
		Segments3d(x,0,-size,x,0,size,view,15)	
	Next
	Color 255,0,0
    Line3d(0,0,0,5,0,0,view)
	Text3d (4.5,.5,.5,"X",view)
	Color 0,255,0
	Line3d(0,0,0,0,5,0,view)
	Text3d (.5,4.5,.5,"Y",view)
	Color 0,0,255
	Line3d(0,0,0,0,0,5,view)
	Text3d (.5,.5,4.5,"Z",view)
End Function 

Function DrawGridZ(view)
	size=25
	Color 30,30,30
 	For z = size To -size Step -1
		Segments3d(0,size,z,0,-size,z,view,15)
		Segments3d(0,z,size,0,z,-size,view,15)	
	Next
	Color 60,60,60
	For z = size To -size Step -5
		Segments3d(0,size,z,0,-size,z,view,15)
		Segments3d(0,z,size,0,z,-size,view,15)		
	Next
	Color 100,100,100
	For z = size To -size Step -10
		Segments3d(0,size,z,0,-size,z,view,15)
		Segments3d(0,z,size,0,z,-size,view,15)
	Next
	Color 255,0,0
    Line3d(0,0,0,5,0,0,view)
	Text3d (4.5,.5,.5,"X",view)
	Color 0,255,0
	Line3d(0,0,0,0,5,0,view)
	Text3d (1,4.5,.5,"Y",view)
	Color 0,0,255
	Line3d(0,0,0,0,0,5,view)
	Text3d (0,.5,4.5,"Z",view)

End Function 
