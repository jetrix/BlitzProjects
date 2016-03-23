;============================
;CUTSCENE MODULE
;============================
;Cutscene Loader/Player by J377iN
;Parses .cam files and creates camera animation
;
;*.cam - Camera animation file

Function LoadCamFile(file$)
 ;Purpose: Loads .cam file
 ;Parameters: file$ - filename
 ;Return: File handle
 camFile=ReadFile(file$+".cam")
 Return camFile
End Function

Function FreeCamFile(camFile)
 ;Purpose: 
 ;Parameters:
 ;Return:
 CloseFile(camFile)
End Function 

Function CreateCutscene(camFilename$)
 ;Purpose: Creates cutscene 
 ;Parameters: camFilename$ - name of camfile
 ;Return: camera handle
 camFile=LoadCamFile(camFilename$)
 camera=CreateCamera()
 header$=ReadString(camFile)
 If header <> "CAMDATA" Then DebugLog "Invalid .CAM file"
 totalFrames=ReadInt(camFile)
 For ctr = 1 To totalFrames
  frame=ReadInt(camFile)
  posX#=ReadFloat(camFile)
  posY#=ReadFloat(camFile)
  posZ#=ReadFloat(camFile)
  rotX#=ReadFloat(camFile)
  rotY#=ReadFloat(camFile)
  rotZ#=ReadFloat(camFile)
  PositionEntity camera,posX#,posY#,posZ#
  RotateEntity camera,rotX#,rotY#,rotZ#
  SetAnimKey camera,frame
 Next
 AddAnimSeq(camera,totalFrames)
 FreeCamFile(camFile)
 Return camera
End Function 

Function PlayCutscene(camera,mode=3,speed#=0.5,seq=0,trans=10)
 ;Purpose: Initiates camera for movement  
 ;Parameters: 
 ;Return: None
 Animate camera,mode,speed#,seq,trans
End Function

Function StopCutscene(camera)
	Animate camera,0
End Function 

Function FreeCutscene(camera)
 FreeEntity camera
End Function 

Function GetCutsceneFrame(camera)
 Return AnimTime(camera)
End Function

Function GetCutsceneLength(camera)
 Return AnimLength(camera)
End Function 

Function CutsceneEnded(camera)
 If Int(AnimTime(camera))=AnimLength(camera): Return True
 Else: Return False
 EndIf 
End Function 