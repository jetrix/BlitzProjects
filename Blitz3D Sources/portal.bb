 


Include "mouselook.bb"
Graphics3D 640,480,16,2
SetBuffer BackBuffer()

Global camera = CreateCamera()		;standard camera
CameraRange camera, .1, 9999

MoveEntity camera, 0, 0, -100
sky = create_skybox()
Global ball = CreateSphere()
ScaleEntity ball ,.1, .1,.1
PositionEntity ball ,0,0,-2
Global Portal = CreateSphere()		;this is the portal mesh (could be a plane etc
ScaleEntity Portal, 1,1,.01
Global PortalTexture = CreateTexture( 256, 256 )	;portal texture

Global PortalCamera = CreateCamera()				;this is the camera that is placed where the

PositionEntity PortalCamera, 0, 0, -100
RotateEntity PortalCamera ,0,180,0


Global Room = CreateCube()							;this is a test room to put portal camera into
ScaleEntity Room, -14, -14, -14
PositionEntity Room, 0, 0, -100

Global Tex = LoadTexture( "gothic3.jpg" )	;basic texture could be any image :)
EntityTexture Room, Tex

Global temp=CreateImage( 256, 256 )					;temp image (camera image)
Global Angle# = 0



;----------------------------------
; Load Media
;----------------------------------
Global gate=LoadMesh("my_stargate3.3ds")
g_tex=LoadTexture("gate1.bmp",65)
tex=LoadAnimTexture( "water_anim.bmp",4,128,128,0,8 )
floor_t=LoadTexture("stone.bmp",2)
Const segs=40,width#=4,depth#=.9
time=MilliSecs()+8000

;----------------------------------
; Create Gate
;----------------------------------
FitMesh gate,-1,-1,-1,2,2,2,True
ScaleMesh gate,1.1,1.1,1.1

PositionEntity gate,0,0,0
EntityTexture gate,g_tex
UpdateNormals gate

MoveMouse 400, 300
;----------------------------------
; Create Water Mesh
;----------------------------------
mesh1=CreateMesh()		   ;Create water mesh
mesh2=CreateMesh()	
mesh3=CreateMesh()	
mesh4=CreateMesh()	
surf=CreateSurface( mesh1 ) ;Create a suface For mesh
surf2=CreateSurface( mesh2 ) 
surf3=CreateSurface( mesh3 ) 
surf4=CreateSurface( mesh4 ) 

For k=0 To segs						;Loop through segments of suface
	x#=Float(k)*width/segs-width/2  ;Get x position as F. Point
	u#=Float(k)/segs                ;Get u pos. as F. Point
	AddVertex surf,x,1,0,u,0        ;Add a vertex/index (texture) to surface
	AddVertex surf,x,-1,0,u,1
Next

For k=0 To segs-1                    ;Loop through segments of surface
	AddTriangle surf,k*2,k*2+2,k*2+3 ;Add triangles to suface texture front and back
	AddTriangle surf,k*2,k*2+3,k*2+1
	AddTriangle surf,k*2,k*2+3,k*2+2
	AddTriangle surf,k*2,k*2+1,k*2+3
Next

For k=0 To segs						;Loop through segments of suface
	x#=Float(k)*width/segs-width/2  ;Get x position as F. Point
	u#=Float(k)/segs                ;Get u pos. as F. Point
	AddVertex surf2,x,1,0,u,0        ;Add a vertex/index (texture) to surface
	AddVertex surf2,x,-1,0,u,1
Next

For k=0 To segs-1                    ;Loop through segments of surface
	AddTriangle surf2,k*2,k*2+2,k*2+3 ;Add triangles to suface texture front and back
	AddTriangle surf2,k*2,k*2+3,k*2+1
	AddTriangle surf2,k*2,k*2+3,k*2+2
	AddTriangle surf2,k*2,k*2+1,k*2+3
Next

For k=0 To segs						;Loop through segments of suface
	x#=Float(k)*width/segs-width/2  ;Get x position as F. Point
	u#=Float(k)/segs                ;Get u pos. as F. Point
	AddVertex surf3,x,1,0,u,0        ;Add a vertex/index (texture) to surface
	AddVertex surf3,x,-1,0,u,1
Next

For k=0 To segs-1                    ;Loop through segments of surface
	AddTriangle surf3,k*2,k*2+2,k*2+3 ;Add triangles to suface texture front and back
	AddTriangle surf3,k*2,k*2+3,k*2+1
	AddTriangle surf3,k*2,k*2+3,k*2+2
	AddTriangle surf3,k*2,k*2+1,k*2+3
Next

For k=0 To segs						;Loop through segments of suface
	x#=Float(k)*width/segs-width/2  ;Get x position as F. Point
	u#=Float(k)/segs                ;Get u pos. as F. Point
	AddVertex surf4,x,1,0,u,0        ;Add a vertex/index (texture) to surface
	AddVertex surf4,x,-1,0,u,1
Next

For k=0 To segs-1                    ;Loop through segments of surface
	AddTriangle surf4,k*2,k*2+2,k*2+3 ;Add triangles to suface texture front and back
	AddTriangle surf4,k*2,k*2+3,k*2+1
	AddTriangle surf4,k*2,k*2+3,k*2+2
	AddTriangle surf4,k*2,k*2+1,k*2+3
Next

EntityTexture mesh1,tex     ;Texture water mesh
EntityTexture mesh2,tex 
EntityTexture mesh3,tex 
EntityTexture mesh4,tex 

ScaleMesh mesh1,.5,1,0;Scale
PositionMesh mesh1,0,.05,-.01  ;Position
EntityAlpha mesh1,.2        ;Adjust mesh alpha (see through)
EntityFX mesh1,.7
UpdateNormals mesh1         ;Update mesh normals

ScaleMesh mesh2,.5,1,0;Scale
PositionMesh mesh2,0,.02,-.01  ;Position
EntityAlpha mesh2,.1      ;Adjust mesh alpha (see through)
EntityFX mesh2,.7
UpdateNormals mesh2

ScaleMesh mesh3,.5,1,0;Scale
PositionMesh mesh3,0,.01,-.01  ;Position
EntityAlpha mesh3,.2        ;Adjust mesh alpha (see through)
EntityFX mesh3,.7
UpdateNormals mesh3

ScaleMesh mesh4,.5,1,0;Scale
PositionMesh mesh4,0,.03,-.01  ;Position
EntityAlpha mesh4,.1        ;Adjust mesh alpha (see through)
EntityFX mesh4,.7
UpdateNormals mesh4






AmbientLight 255,255,255			
tex=CreateTexture(64,64,1+128+256)

;MAIN LOOP
While Not KeyDown(1)
					;special function to render the portal

	If KeyDown(203)=1 Then Angle# = Angle# -1		;left and right cursor to turn camera
	If KeyDown(205)=1 Then Angle# = Angle# +1
	If KeyDown(200)=1 Then  campos# =  campos# +.1
	If KeyDown(208)=1 Then  campos# =  campos# -.1
	If KeyDown(30)=1 Then  ballpos# =  ballpos# +.1
	If KeyDown(44)=1 Then  ballpos# =  ballpos# -.1



ph#=MilliSecs()/1.3        ;Frequency of waves (time)
	cnt=CountVertices(surf)-1  ;Count all of the Vertices in water mesh
		
	For k=0 To cnt             ;Loop through mesh and update vertices
		x#=VertexX(surf,k)
		y#=VertexY(surf,k)	
		z#=Sin(ph+x*200)*depth ;Create wave
		VertexCoords surf,k,x,y,z ;Update
	Next
	EntityTexture mesh1,tex
	For k=0 To cnt             ;Loop through mesh and update vertices
		x#=VertexX(surf2,k)
		y#=VertexY(surf2,k)	
		z#=Cos(ph+x*150)*.2 ;Create wave
		VertexCoords surf2,k,x,y,z ;Update
	Next
	
	For k=0 To cnt             ;Loop through mesh and update vertices
		x#=VertexX(surf3,k)
		y#=VertexY(surf3,k)	
		z#=Sin(ph+x*100)*.5 ;Create wave
		VertexCoords surf3,k,x,y,z ;Update
	Next
	
	For k=0 To cnt             ;Loop through mesh and update vertices
		x#=VertexX(surf4,k)
		y#=VertexY(surf4,k)	
		z#=Cos(ph+x*50)*2 ;Create wave
		VertexCoords surf4,k,x,y,z ;Update
	Next
	EntityTexture mesh2,tex
	EntityTexture mesh2,tex
	EntityTexture mesh2,tex



	UpdateNormals mesh1 
	UpdateNormals mesh2
	UpdateNormals mesh3
	UpdateNormals mesh4      
CameraZoom PortalCamera, ballpos#
	PositionEntity Camera, 0,0, campos#
	If EntityZ#  (ball) => -1 Then ballpos# =-101
	PositionEntity ball, 0,0, ballpos#
	
	RotateEntity Camera, 0, Angle#, 0
	MoveEntity Camera, 0, 0,  -2.5
	MoveEntity ball, 0, 0,  -2.5

	RotateEntity PortalCamera, 0, -Angle#, 180		;rotate portal camera to what player should see
    UpdateCubemap(tex,PortalCamera,Portal)
	Mouselook(camera)
	UpdateWorld
	RenderWorld
	Text 0, 0, "Portal demo - Copyright �2002 EdzUp"
	Text 0,20, "Please include my name in credits if you use this effect in your game"
	Text 0,80, "Use left and right cursor to rotate camera"
	Text 0,60,"Z Position: "+EntityZ#  (ball)


	Flip
Wend
End

Function RenderPortal( Entity )
	;Render portal texture
	HideEntity Camera								;hide camera
	ShowEntity PortalCamera							;show portal camera
	UpdateWorld
	RenderWorld
	CopyRect 0, 0, 256, 256, 0, 0, BackBuffer(), TextureBuffer( PortalTexture )
	EntityTexture Entity, PortalTexture, 0, 0		;Retexture entity with new texture
	ShowEntity Camera								;show player camera
	HideEntity PortalCamera							;hide portal camera
	Cls												;clear back buffer
End Function


 



;Tracers' Skybox code!!!!!


Function create_skybox()
    mesh = CreateMesh()

    ;front face
    brush = LoadBrush("dfront.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,-1,+1,-1,0,0
    AddVertex surface,+1,+1,-1,1,0
    AddVertex surface,+1,-1,-1,1,1
    AddVertex surface,-1,-1,-1,0,1
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush

    ;right face
    brush = LoadBrush("dright.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,+1,+1,-1,0,0
    AddVertex surface,+1,+1,+1,1,0
    AddVertex surface,+1,-1,+1,1,1
    AddVertex surface,+1,-1,-1,0,1
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush

    ;back face
    brush = LoadBrush("dback.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,+1,+1,+1,0,0
    AddVertex surface,-1,+1,+1,1,0
    AddVertex surface,-1,-1,+1,1,1
    AddVertex surface,+1,-1,+1,0,1
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush
 
    ;left face
    brush = LoadBrush("dleft.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,-1,+1,+1,0,0
    AddVertex surface,-1,+1,-1,1,0
    AddVertex surface,-1,-1,-1,1,1
    AddVertex surface,-1,-1,+1,0,1
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush

    ;top face
    brush = LoadBrush("dup.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,-1,+1,+1,0,1
    AddVertex surface,+1,+1,+1,0,0
    AddVertex surface,+1,+1,-1,1,0
    AddVertex surface,-1,+1,-1,1,1
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush
   
    ;bottom face 
    brush = LoadBrush("ddown.jpg",49)
    surface = CreateSurface(mesh,brush)
    AddVertex surface,-1,-1,-1,1,0
    AddVertex surface,+1,-1,-1,1,1
    AddVertex surface,+1,-1,+1,0,1
    AddVertex surface,-1,-1,+1,0,0
    AddTriangle surface,0,1,2
    AddTriangle surface,0,2,3
    FreeBrush brush
    
    ScaleMesh mesh,300,300,300
    FlipMesh mesh
    EntityFX mesh,1 ; make fullbright
    Return mesh
End Function
 
Function UpdateCubemap(tex,camera,entity)

	tex_sz=TextureWidth(tex)

	; Show the camera we have specifically created for updating the cubemap
	ShowEntity camera
	
	; Hide entity that will have cubemap applied to it. This is so we can get cubemap from its position, without it blocking the view
	HideEntity entity

	; Position camera where the entity is - this is where we will be rendering views from for cubemap
	PositionEntity camera,EntityX#(entity),EntityY#(entity),EntityZ#(entity)

	CameraClsMode camera,False,True
	
	; Set the camera's viewport so it is the same size as our texture - so we can fit entire screen contents into texture
	CameraViewport camera,0,0,tex_sz,tex_sz

	; Update cubemap

	; do left view	
	SetCubeFace tex,0
	RotateEntity camera,0,90,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; do forward view
	SetCubeFace tex,1
	RotateEntity camera,0,0,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; do right view	
	SetCubeFace tex,2
	RotateEntity camera,0,-90,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; do backward view
	SetCubeFace tex,3
	RotateEntity camera,0,180,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; do up view
	SetCubeFace tex,4
	RotateEntity camera,-90,0,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; do down view
	SetCubeFace tex,5
	RotateEntity camera,90,0,0
	RenderWorld
	CopyRect 0,0,tex_sz,tex_sz,0,0,BackBuffer(),TextureBuffer(tex)
	
	; Show entity again
	ShowEntity entity
	
	; Hide the cubemap camera
	HideEntity camera
	
End Function
 