Graphics3D 640,480

SetBuffer BackBuffer()

Y = 0

AmbientLight 0,0,150

MAN = LoadAnimMesh("KK.B3D")
TurnEntity MAN, 180,180,0
ScaleEntity MAN, -.2,-.2,-.2

KICK = ExtractAnimSeq(MAN,1,40)
Animate MAN,1,.2,KICK
FlipMesh MAN

CAMERA = CreateCamera()
MoveEntity CAMERA,0,14,-25
PointEntity CAMERA,MAN

LIGHT = CreateLight(2)
PLANE = CreatePlane()

EntityColor PLANE, 0,0,150
EntityColor MAN,0,50,0
MoveEntity PLANE,0,-14,0 
MoveEntity LIGHT,0,50,0


While Not KeyHit(1)

	
	If KeyDown(205) Then TurnEntity MAN, 0, Y - 1, 0
	If KeyDown(203) Then TurnEntity MAN, 0, Y + 1, 0
	
	UpdateWorld
	RenderWorld
	
	Text 20,20, "KARATE KID KICK SEQUENCE!"
	Text 20,30, "YES, IT IS CRAP..."
	Text 20,40, "BUT HEY, MODEL CREATED IN CINEMA 4D"
	Text 20,50, "EXPORTED THE SUCKER AS .3DS INTO MILKSHAPE"
	Text 20,60, "BONED AND ANIMATED IT THEN SPIT IT OUT AS .B3D!"
	Text 20,70, "(USE LEFT AND RIGHT TO ROTATE)"
	Text 20,80, "P.S. DON'T KNOW WHY THE MESH LOOKS 'OPEN'" 
	Text 20,90, "MAYBE NO TEXTURES MAKES IT LIKE THAT?" 
	Flip

Wend

End