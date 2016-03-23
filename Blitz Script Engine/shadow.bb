;Example 1
;-Light behind bars. (It's a pun. Deal with it.)
;By Moby dick.
Include "Omni.bb"
Graphics3D 640,480,0,2
SetBuffer BackBuffer()
;----- [ Create a scene. This is for the purposes of the example
;you do not have to use primitives, you may load meshes]
;-

flr =CreateCube()
FitMesh flr,-12,0,-12,25,30,25
FlipMesh flr
s =CreateCube()
FitMesh s,-2.5,0,-2.5,5,5,5

For t=-12 To 12 Step 8
;	st =CreateCube()
;	FitMesh st,t,5,0,0.2,10,0.2
;	addCaster( st)

;	UpdateNormals st

Next

vcam =CreateCamera()
PositionEntity vcam,0,5,10
UpdateNormals s
UpdateNormals flr
addReciever( flr,128,128)

addcaster( s)
 

addOmniLight(0,2.5,10)
;addOmniLight(0,2.5,-10)


ms=MilliSecs()
tr =renderLightMap(vcam)
ms=MilliSecs()-ms

;--


;create base texture, this is JUST for the exampe, not the lightmapping process.(You don't have to create textures for that.)
test =LoadTexture("back.bmp")
ScaleTexture test,0.06,0.06
If Not test End
back=CreateImage(GraphicsWidth(),GraphicsHeight())
EntityTexture s,test,0,0
te =textoimage( lmap\texmap)
EntityTexture flr,test,0,0
Color 255,255,255
;HideEntity flr
Repeat
Cls
	UpdateWorld
	RenderWorld
	Text 1,1,"Render Time>"+ms+" Seconds."+" tr>"+tr
	Text 1,20,"W=Forward, S=BacK, A=left,D=Right."
	Text 1,40,"Use the mouse to look around"
	If KeyDown(17) MoveEntity vcam,0,0,1
	If KeyDown(30) MoveEntity vcam,-1,0,0
	If KeyDown(31) MoveEntity vcam,0,0,-1
	If KeyDown(32) MoveEntity vcam,1,0,0
	
	TurnEntity vcam,MouseYSpeed()*0.2,-MouseXSpeed()*0.2,0
	RotateEntity vcam,EntityPitch(vcam),EntityYaw(vcam),0
	MoveMouse 320,240
	FlushMouse
	
	
	If MouseDown(1) shadowShimmer()
	If KeyDown(57) 
		GrabImage back,0,0
		SaveImage back,"lmapA.bmp"
		tmp =CopyImage(back)
		ResizeImage tmp,256,256 
		SaveImage tmp,"lmapB.bmp"
		FreeImage tmp
	EndIf
;	DrawImage te,0,0
	Flip
Until KeyDown(1)

;------ [ End of example code]