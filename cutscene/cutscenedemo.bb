Include "cutscene.bb"

Graphics3D 640,480,32,2
SetBuffer BackBuffer()

light=CreateLight()
RotateEntity light,90,0,0

scene=LoadMesh("scene.x")
PositionEntity scene,0,0,0

brush=CreateBrush()
BrushShininess brush,1
PaintMesh scene,brush

;GenerateCutscene()
camera=CreateCutscene("test")
 

While Not KeyHit(1) 
 If KeyHit(28) Then PlayCutscene(camera)
 If KeyHit(57) Then StopCutscene(camera)
 If CutsceneEnded(camera)=True Then StopCutscene(camera)
 UpdateWorld 
 RenderWorld
 Text 0,0,"Press Enter to play, Space to stop"
 Text 0,15,"Frame: "+GetCutsceneFrame(camera)
 Text 0,30,"Total Frames: "+GetCutsceneLength(camera)
 Flip
Wend

FreeCutscene(camera)
End



Function GenerateCutscene()
SeedRnd MilliSecs()
totalFrames=1800

file=WriteFile("test.cam")
WriteString file,"CAMDATA"
WriteInt file,totalFrames

For i = 1 To totalFrames Step 15
 WriteInt file,i
 WriteFloat file,Rnd(0,500)
 WriteFloat file,Rnd(0,500)
 WriteFloat file,Rnd(0,500)
 WriteFloat file,Rnd(0,360)
 WriteFloat file,Rnd(0,360)
 WriteFloat file,Rnd(0,360)
Next

CloseFile(file)
End Function