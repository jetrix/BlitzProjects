;Control
; Camera position, angle values
Global cam_x#,cam_z#,cam_pitch#,cam_yaw#,cam_speed#=.5		; Current
Global dest_cam_x#,dest_cam_z#,dest_cam_pitch#,dest_cam_yaw#	; Destination
Global ent_x#,ent_z#,ent_pitch#,ent_yaw#,ent_speed#=.5		; Current
Global dest_ent_x#,dest_ent_z#,dest_ent_pitch#,dest_ent_yaw#	; Destination

Function mouselook(camera)
	; Mouse look
	; ----------

	; Mouse x and y speed
	mxs=MouseXSpeed()
	mys=MouseYSpeed()
	
	; Mouse shake (total mouse movement)
	mouse_shake=Abs(((mxs+mys)/2)/1000.0)

	; Destination camera angle x and y values
	dest_cam_yaw#=dest_cam_yaw#-mxs
	dest_cam_pitch#=dest_cam_pitch#+mys

	; Current camera angle x and y values
	cam_yaw=cam_yaw+((dest_cam_yaw-cam_yaw)/5)
	cam_pitch=cam_pitch+((dest_cam_pitch-cam_pitch)/5)
	
	RotateEntity camera,cam_pitch#,cam_yaw#,0
	;RotateEntity camera,mxs,mys,0
		
	; Rest mouse position to centre of screen
	MoveMouse 400,300

		; Move camera using movement values
	MoveEntity camera,x#,y#,z#
		
End Function	