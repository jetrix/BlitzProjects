Include "B3D.bb"
Graphics3D 640,480,32,2

Print "Loading test1"
time1=MilliSecs()
mesh1=LoadB3D("mount.b3d",0,0,0)
result1=MilliSecs()-time1
Print "Done"
Print "Time taken: " +result1

Print "Loading test2"
time2=MilliSecs()
mesh2=LoadMesh("mount.b3d")
result2=MilliSecs()-time2
Print "Done"
Print "Time taken: "+ result2 
WaitKey()
