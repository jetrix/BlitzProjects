;modified from Marks original dragon sample 
;this it just to test normal mapping
;need GFX card that supports DOT3, Mod2, and multitextureing

;Cube2.png = a 12 by 2 pixel cubemap containing normals converted to colors
;Lcube.png = a 12 by 2 pixel cubemap containing lighting colors
;rockbump.bmp = a normal map of a rocky surface
;Normit.bmp = a normal map of a sci-fi-ish surface
;mossy.jpg = a regular texturemap used on the landscape.3ds mesh
;dragon.bmp = regular texturemap used on the dragon.md2 mesh 

Global info1$="Dragon Normalmapping Demo"
Global info2$="Use arrows keys to pan, A/Z to zoom"
Global info3$="MD2 Dragon model courtesy of Polycount"

Include "start.bb"

; If user's graphics card does not support cubic mapping then quit example 
If GfxDriverCaps3D()<110 Then RuntimeError "Sorry, your graphics card does not support cubic environemnt maps." 

;camera
camera=CreateCamera()
cam_xr#=30:cam_yr#=0:cam_zr#=0:cam_z#=-100

CameraFogMode camera,1
CameraFogRange camera,1,375
CameraFogColor camera,128,128,200
CameraClsColor camera,128,128,200

;cubemap for normals
nmap=LoadTexture( "cube2.png",1+128 )
SetCubeMode nmap,2
TextureBlend nmap,2

;cubemap for lightcolor
lmap=LoadTexture( "lcube.png",1+128 )
SetCubeMode lmap,2
TextureBlend lmap,2


;environment 
land=LoadMesh("landscape.3ds")
FitMesh land, -400,-100,-400,800,200,800
EntityFX land,1

landtex=LoadTexture("mossy.jpg")
TextureBlend landtex,5

landbump=LoadTexture("rockbump.bmp")
TextureBlend landbump,4

ScaleTexture landtex,.5,.5
ScaleTexture landbump,.05,.05

EntityTexture land,nmap,0,0
EntityTexture land,landbump,0,1
EntityTexture land,landtex,0,2
EntityTexture land,lmap,0,3


;cool dragon model!
dragon=LoadMD2( "dragon.md2" )
EntityFX dragon,1


dragonbump = LoadTexture("normit.bmp")
TextureBlend dragonbump,4
ScaleTexture dragonbump,.25,.25

dragonskin = LoadTexture("dragon.bmp")
TextureBlend dragonskin,5


EntityTexture dragon,nmap,0,0
EntityTexture dragon,dragonbump,0,1
EntityTexture dragon,dragonskin,0,2
EntityTexture dragon,lmap,0,3

AnimateMD2 dragon,1,.09,40,46



;main loop
While Not KeyHit(1)


;turning the meshes to see the lighting effect it
;TurnEntity land,0,.1,0
;TurnEntity dragon,0,.1,0


;change dragon animation seqence
    If KeyDown(57) Then AnimateMD2 dragon,1,.1115,0,40
    If KeyDown(2) Then AnimateMD2 dragon,1,.1115,40,46
    If KeyDown(3) Then AnimateMD2 dragon,1,.1115,46,54
    If KeyDown(4) Then AnimateMD2 dragon,1,.1115,54,58
    If KeyDown(5) Then AnimateMD2 dragon,1,.1115,58,62
    If KeyDown(6) Then AnimateMD2 dragon,1,.1115,62,66
    If KeyDown(7) Then AnimateMD2 dragon,1,.1115,66,72
    If KeyDown(8) Then AnimateMD2 dragon,1,.1115,72,84

;camera controls
	If KeyDown(203)
		cam_yr=cam_yr-1.5
	Else If KeyDown(205)
		cam_yr=cam_yr+1.5
	EndIf
	
	If KeyDown(200)
		cam_xr=cam_xr+1.5
		If cam_xr>90 cam_xr=90
	Else If KeyDown(208)
		cam_xr=cam_xr-1.5
		If cam_xr<5 cam_xr=5
	EndIf
	
	If KeyDown(26)
		cam_zr=cam_zr+1.5
	Else If KeyDown(27)
		cam_zr=cam_zr-1.5
	EndIf
	
	If KeyDown(30)
		cam_z=cam_z+1:If cam_z>-10 cam_z=-10
	Else If KeyDown(44)
		cam_z=cam_z-1:If cam_z<-280 cam_z=-280
	EndIf
	
;take a screen shot	
	If KeyDown(88)
	  ss$ = "screenshot" + Str MilliSecs() + ".bmp"
	 SaveBuffer(FrontBuffer(),ss$)
	End If
	
	PositionEntity camera,0,0,0
	RotateEntity camera,cam_xr,cam_yr,cam_zr
	MoveEntity camera,0,0,cam_z

	UpdateWorld
	RenderWorld
	Flip
Wend

End

