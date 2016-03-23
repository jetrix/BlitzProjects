Global interface=LoadImage("EDITORLAYOUT.png")
Global buttonviews=LoadImage("viewbuttons.png")
Global buttonIN34=LoadImage("34buttonIN.png")
Global buttonIN17=LoadImage("17buttonIN.png")
Global arrow=LoadImage("arrow.png")

Global pan_TL,zoom_TL,rot_TL,max_TL
Global pan_TR,zoom_TR,rot_TR,max_TR
Global pan_BL,zoom_BL,rot_BL,max_BL
Global pan_BR,zoom_BR,rot_BR,max_BR
Global TLActive=False,TRActive=False,BLActive=False,BRActive=True,buttonActive
Global zoomTL#=.1,zoomTR#=.1,zoomBL#=.1,zoomBR#=.1,inc#=.01

Global TR,TL,BR,BL,Full
Global TRcam,TLcam,BRcam,BLcam,Fullcam
Global TRwire=True,TLwire=True,BRwire=False,BLwire=True

Global pivotTL=CreatePivot()
Global pivotTR=CreatePivot()
Global pivotBL=CreatePivot()
Global pivotBR=CreatePivot()

Global front_cam=CreateCamera(pivotTL)
PositionEntity front_cam,0,0,-50
CameraProjMode front_cam,2
TL=front_cam
TLcam=F_cam

Global top_cam=CreateCamera(pivotTR)
PositionEntity top_cam,0,50,0
TurnEntity top_cam,90,0,0
CameraProjMode top_cam,2
TR=top_cam
TRcam=T_cam

Global left_cam=CreateCamera(pivotBL)
TurnEntity left_cam,0,-90,0
PositionEntity left_cam,-50,0,0
CameraProjMode left_cam,2
BL=left_cam
BLcam=L_cam

Global pers_cam=CreateCamera(pivotBR)
PositionEntity pers_cam,0,0,-10
BR=pers_cam
BRcam=P_cam

CameraViewport TL,17*16,17*3,17*22,17*19
CameraViewport TR,17*38,17*3,17*22,17*19
CameraViewport BL,17*16,17*23,17*22,17*19
CameraViewport BR,17*38,17*23,17*22,17*19
