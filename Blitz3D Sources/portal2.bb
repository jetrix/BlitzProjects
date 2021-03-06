 
Include "mouselook.bb"
Graphics3D 800,600,16,2
SetBuffer BackBuffer()

Global camera = CreateCamera()		;standard camera


MoveEntity camera, 0, 0, -100
sky = create_skybox()
Global ball = CreateSphere()
ScaleEntity ball ,.1, .1,.1
PositionEntity ball ,0,0,-2
Global Portal = CreateCube()		;this is the portal mesh (could be a plane etc
;ScaleEntity Portal, 1,1,.01
Global Portal2 = CreateCube()	
;ScaleEntity Portal2, 1,1,.01
PositionEntity Portal2, 0, 0, -100 



Global Room = CreateCube()							;this is a test room to put portal camera into
ScaleEntity Room, -14, -14, -14
PositionEntity Room, 0, 0, -100

Global Tex = LoadTexture( "gothic3.jpg" )	;basic texture could be any image :)
EntityTexture Room, Tex

			;temp image (camera image)
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

ScaleMesh mesh1,.3,.8,0;Scale
PositionMesh mesh1,0,.05,-.01  ;Position
EntityAlpha mesh1,.2        ;Adjust mesh alpha (see through)
EntityFX mesh1,.7
UpdateNormals mesh1         ;Update mesh normals

ScaleMesh mesh2,.3,.8,0;Scale
PositionMesh mesh2,0,.02,-.01  ;Position
EntityAlpha mesh2,.1      ;Adjust mesh alpha (see through)
EntityFX mesh2,.7
UpdateNormals mesh2

ScaleMesh mesh3,.3,.8,0;Scale
PositionMesh mesh3,0,.01,-.01  ;Position
EntityAlpha mesh3,.2        ;Adjust mesh alpha (see through)
EntityFX mesh3,.7
UpdateNormals mesh3

ScaleMesh mesh4,.3,.8,0;Scale
PositionMesh mesh4,0,.03,-.01  ;Position
EntityAlpha mesh4,.1        ;Adjust mesh alpha (see through)
EntityFX mesh4,.7
UpdateNormals mesh4






AmbientLight 255,255,255			

;MAIN LOOP
While Not KeyDown(1)






	MoveMouse 800/2,600/2
	If KeyDown(203)=1 Then Angle# = Angle# -1		;left and right cursor to turn camera
	If KeyDown(205)=1 Then Angle# = Angle# +1
	If KeyDown(200)=1 Then  campos# =  campos# +.1
	If KeyDown(208)=1 Then  campos# =  campos# -.1
	If KeyDown(30)=1 Then  ballpos# =  ballpos# +.1
	If KeyDown(44)=1 Then  ballpos# =  ballpos# -.1

	RotateEntity camera,mys#,Angle#,0
	MoveEntity camera,0,0,campos#



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
		z#=Cos(ph+x*150)*.1 ;Create wave
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
		z#=Cos(ph+x*50)*1 ;Create wave
		VertexCoords surf4,k,x,y,z ;Update
	Next
	EntityTexture mesh2,tex
	EntityTexture mesh2,tex
	EntityTexture mesh2,tex



	UpdateNormals mesh1 
	UpdateNormals mesh2
	UpdateNormals mesh3
	UpdateNormals mesh4   
	   
	PositionEntity Camera, 0,0, campos#
	If EntityZ#  (ball) => -.5 Then ballpos# =-101
	PositionEntity ball, 0,0, ballpos#
	
	RotateEntity Camera, 0, Angle#, 0
	MoveEntity Camera, 0, 0,  -2.5
	MoveEntity ball, 0, 0,  -2.5
	;rotate portal camera to what player should see

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
 
 