;BlitzScript3D Compiler &  Virtual Machine (B.S.C.V.M) by 
;Frankie Taylor

Include "bscvmStack.bb"
Include "bscvmVar.bb"
Include "bscvmLabel.bb"
Include "bscvmBSR.bb"
Include "bscvmCompiler.bb"
Include "bscvmThread.bb"
Include "bscvmCLI.bb"

.bscvm_start

AppTitle("BlitzScript3D Engine - [Esc] To Stop")

;COMPILER /////////////////////////////////////////////////
bscvmCompilerLoad("test") ;compile/recompile

;VIRTUAL MACHINE //////////////////////////////////////////
blitzScript3D=bscvmThreadExec("test") ;load & run

;##########################################################
;BlitzScript3D vs Blitz Speed Test
;##########################################################
.bscvm_main
Graphics3D(640,480,32,2)
SetBuffer(BackBuffer())

loops=1000
pause=0



;**********************************************************
;Test.bsc in BlitzScript
;**********************************************************
ClearWorld()
blitzscriptVarSet(1,CreateCamera())
blitzscriptVarSet(2,CreateLight())

Cls()
time=MilliSecs()
For loop =  1 To loops

	;======================================================
	bscvmThreadUpdate()
	;======================================================

	RenderWorld()
	
	VWait()
	Flip(False)
	If KeyHit(1) Exit
Next
timea=MilliSecs()-time



;**********************************************************
;Test.bsc Equivalent in Blitz
;**********************************************************

ClearWorld()
blitzscriptVarSet(1,CreateCamera())
blitzscriptVarSet(2,CreateLight())

Cls()
time=MilliSecs()
For loop =  1 To loops

	;======================================================
	r=Rnd(31,255)
	g=Rnd(31,255)
	b=Rnd(31,255)
	x=Rnd(-100,100)
	y=Rnd(-100,100)
	z=Rnd(0,100)
	w=Rnd(1,10)
	h=Rnd(1,10)
	l=Rnd(1,10)
	entity=CreateCube(0)
	ScaleEntity(entity,w,h,l)
	EntityShininess(entity,1) 
	EntityColor(entity,r,g,b)
	PositionEntity(entity,x,y,z)
	PositionEntity(blitzscriptVarGet(1),Cos(loop) * 25,Sin(loop) * 25,Cos(loop) * 75)
	RotateEntity(blitzscriptvarget(1),0,0,loop,0)	
	;======================================================
	
	RenderWorld()
	
	VWait()
	Flip(False)
	If KeyHit(1) Exit
Next
timeb=MilliSecs()-time

;##########################################################
;End Speed Test
;##########################################################
	
.bscvm_stop
Cls()		
Color(255,255,255)
Print "BlitzScript3D Time:"+timea
Print "BlitzBasic 3D Time:"+timeb
Flip()

;cleanup
bscvmCompilerStop()
bscvmThreadStop()
bscvmVarStop()
bscvmLabelStop()

AppTitle("BlitzScript3D Engine - [R]eload_&_Run E[X]it")

Repeat
	If KeyDown(19) Or pause=500000 Goto bscvm_start
	If KeyDown(45) Exit
	pause=pause+1
Forever	
WaitKey()
End