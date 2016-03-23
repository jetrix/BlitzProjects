Graphics3D 800,600,32,3

image=CreateImage(800,600)

LockBuffer ImageBuffer(image)
SetBuffer ImageBuffer(image)
For y=0 To 599
  For x=0 To 799
    WritePixelFast x,y,Rnd($ffffff)
  Next
Next
UnlockBuffer ImageBuffer(image)
SetBuffer BackBuffer()

cube1=CreateCube()
cube2=CreateCube()

redbrush=CreateBrush($ff,0,0)
bluebrush=CreateBrush(0,0,$ff)

PaintEntity cube1,redbrush
PaintEntity cube2,bluebrush

camera=CreateCamera()

MoveEntity cube1,0,0,100

ScaleEntity cube1,15,15,15
ScaleEntity cube2,1000,1000,1000
FlipMesh(cube2)

Repeat

  TurnEntity cube1,1,2,3

  CameraClsMode camera,False,True
  ShowEntity cube1
  HideEntity cube2
  RenderWorld()

  DrawImage image,0,0

  CameraClsMode camera,False,False
  HideEntity cube1
  ShowEntity cube2
  RenderWorld()

  Flip


Until KeyDown(1)
End