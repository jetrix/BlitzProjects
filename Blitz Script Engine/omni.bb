;Omni V0.7 Beta 3 - 3D Lightmapping Engine.
;
;OpenSoure
;
;You may base your own projects off this.
;You may NOT claim credit for this base code.
;Only additions/changes YOU make.
;-----
;Originally wrote by Antony Wells 2003
;-----

;-{Engine Globals. There are functions to change these, use 'em]
Global lMapWidth#=1024,lMapHeight#=2048
Global chunkWidth#=32,chunkHeight#=32
Const shadeWidth =256,shadeHeight=256
Global lMap.lightMap 


Global lMapBlend=3
;-[Types]
Type recv ;reciever meshes(Are lit)
	Field id
	Field cW,cH
End Type
Type cast ;caster meshes (Cast shadows)
	Field id
End Type

Type light ;3d lights
	Field x#,y#,z#
	Field cpiv,r,g,b
	Field typ
End Type

;-Chunks are 'reciever' tris, processed for rendering.
Type chunk 
	Field x#[3],y#[3],z#[3]
	Field u#[3],v#[3] ;u,v coords for the lightmap
	Field vi[3]
	Field nx#,ny#,nz# ;x,y,z normal of the triangle.
	Field pTex ;which texture plane to map to. (1-3)
	Field lTex ;lightMap texture
	Field srf,mesh
	Field eux#,evx#
	Field euy#,evy#
	Field euz#,evz#,leaf.leaf
	Field ox#,oy#,oz#
	Field tBank
	Field lit,shade
	Field cw,ch
	Field mx#,my#,mz#
	;real time fx 
	Field shim#[3]
End Type

Type shade
	Field x#[2],y#[2],z#[2]
	Field sx#[2],sy#[2]
	Field msh,light.light
End Type

Type yl
	Field y[shadeHeight]
End Type
Type shadeMap
	Field img,buf
	Field x.yl[shadeWidth]
	Field light.light,cast.cast
End Type


Type leaf
	Field leaf.leaf[2]
	Field x#,y#,w#,h#
	Field on
End Type

Type lightMap
	Field texMap,texBuf
	Field sW,sH
	Field tree.leaf,scam
End Type

;shadow maps

;--- [ Engine control ]
Function lightMapSize( width#,height#)
	lMapWidth =width
	lMapHeight =height
End Function

Function chunkSize( width#,height#)
	chunkWidth =width
	chunkHeight =height
End Function
Function shadeMapSize( width,height)
	lMap\sw =width
	lMap\sh =height
End Function


Function ligtMapBlend( mode=2)
	lMapBlend =mode
End Function

;--- [ Special Fx ] ;you must retain chunks for fx to work.

Function shadowShimmer() ;disabled for now.
	ms#=0.005
	For chunk.chunk =Each chunk
		For vt=0 To 2 ;map lightmap onto mesh
			chunk\shim[vt] =chunk\shim[vt]+1
			a=chunk\shim[vt]
			leaf.leaf =chunk\leaf
			VertexTexCoords chunk\srf,chunk\vi[vt],leafU(leaf,chunk\u[vt])+Cos(a)*ms,leafV(leaf,chunk\v[vt])+Sin(a)*ms,0,1
		Next
	Next
End Function


;--- [ Scene Building ]
Function addReciever(mesh,cw=128,ch=128)
	recv.recv =New recv
	recv\id =mesh
	recv\cw=cw
	recv\ch=ch
End Function

Function addCaster(mesh)
	cast.cast =New cast
	cast\id =mesh
End Function


Const cOmni=1,cDirectional=2
Function addOmniLight(x#,y#,z#,r#=128,g#=128,b#=128)
	light.light =New light
	light\x =x
	light\y =y
	light\z =z
	light\r =r
	light\g =g
	light\b =b
	light\cPiv =CreatePivot()
	PositionEntity light\cpiv,x,y,z
End Function

Function renderLightMap(cam=0)
Local x#[5],y#[5],z#[5],v#[5]
	For recv.recv =Each recv
		EntityPickMode recv\id,2,True
		HideEntity recv\id
	Next
	For cast.cast =Each cast
		EntityAlpha cast\id,0
	Next
	CameraViewport cam,0,0,shadeWidth,shadeHeight
	mapWidth =GraphicsWidth()
	mapHeight =GraphicsHeight()

	For light.light =Each light
		PositionEntity cam,light\x,light\y,light\z
		For cast.cast =Each cast
			shadeMap.shadeMap =New shadeMap
			shadeMap\light =light
			shadeMap\cast =cast
			PointEntity cam,cast\id
			EntityAlpha cast\id,1
			RenderWorld
			EntityAlpha cast\id,0
			LockBuffer
			For px=0 To shadeWidth-1
				For py=0 To shadeHeight-1
					;pix =ReadPixelFast(px,py) 
					If ((ReadPixelFast(px,py) Shr 16) And 255)
						If shadeMap\x[px]=Null shadeMap\x[px] =New yl
						shadeMap\x[px]\y[py] =True
					EndIf
				Next
			Next
		Next
		UnlockBuffer
	Next
	
	
	
	For cast.cast =Each cast
		EntityAlpha cast\id,1
	Next
	
	Goto skipnew
	For light.light =Each light ;create shadow segs'
	PositionEntity cam,light\x,light\y,light\z
	For cast.cast =Each cast
		PointEntity cam,cast\id
		For sc =1 To CountSurfaces( cast\id)
			srf =GetSurface( cast\id,sc)
			For t=1 To CountTriangles( srf)
				shade.shade =New shade
				shade\msh =cast\id
				shade\light =light
				sa=sa+1
				For vt=0 To 2
					v1 =TriangleVertex(srf,t,vt)
					If v1=<CountVertices(srf)
						CameraProject cam,VertexX(srf,v1),VertexY(srf,v1),VertexZ(srf,v1)
						shade\sx[vt] =ProjectedX()
						shade\sy[vt] =ProjectedY() 
					Else
						Delete shade
						Goto skipS
					EndIf
					
					Next
				Next
			.skipS
			Next
		
		Next
		
	Next
	.skipnew
	
	createLightMap()
	If cam setLightCam(cam)
	For recv.recv =Each recv
		unweld(recv\id)
		HideEntity recv\id
		EntityTexture recv\id,lMap\texMap,0,1
		EntityFX recv\id,1
		sC =CountSurfaces( recv\id)
		If sc
			For s=1 To sc
				srf =GetSurface( recv\id,s)
				tris =CountTriangles( srf)
			
				For tri=0 To tris
					chunk.chunk =New chunk
					chunk\cw =recv\cw
					chunk\mesh =recv\id
					chunk\ch =recv\ch
					chunk\srf =srf
					chunk\lit =True	
					For vt=0 To 2
						vi =TriangleVertex(srf,tri,vt)
						If vi<CountVertices(srf)
							x[vt] =VertexX( srf,vi)
							y[vt] =VertexY( srf,vi)
							z[vt] =VertexZ( srf,vi)
							chunk\x[vt] =x[vt]
							chunk\y[vt] =y[vt]
							chunk\z[vt] =z[vt]
							chunk\vi[vt] =vi
						Else
							Delete chunk
							Goto skipChunk
						EndIf
					Next
					TriNorm( x[0],y[0],z[0],x[1],y[1],z[1],x[2],y[2],z[2])
					nX# =tNormX()
					nY# =tNormY()
					nZ# =tNormZ()
					chunk\nx =nx
					chunk\ny =ny
					chunk\nz =nz
					
								
					If Abs(nx)>Abs(ny) And Abs(nx)>Abs(nz)
						chunk\pTex =1
						;map onto yz-plane
					Else
						If Abs(ny)>Abs(nx) And Abs(ny)>Abs(nz)
							chunk\pTex =2
						Else
							chunk\pTex =3
						EndIf
					EndIf
					.skipChunk								
				Next
			Next
		EndIf
		
	Next
	tr =prepChunks()
	For recv.recv =Each recv
		UpdateNormals recv\id
		ShowEntity recv\id
	Next
	Return sa
End Function

Function createLightMap()
	lMap.lightMap =New lightMap
	lmap\texMap =CreateTexture(lMapWidth,lMapHeight,256)
	lmap\texBuf =TextureBuffer(lMap\texMap)
	TextureCoords lmap\texMap,1 ;set to the second u,v set.
	TextureBlend lmap\texMap,2
End Function
Function setLightCam(cam)
	lmap\scam =cam
	CameraClsMode lmap\scam,False,True
End Function

;-- [ Leaf Engine] ;packs multiple textures into 1 'larger' texture
Function newLeaf.leaf(texture) ;returns leaf object that holds the tex.
	width =TextureWidth(texture)
	height =TextureHeight(texture)
	If width<1 Or height<1 Return
	If lMap\tree =Null ;first image
		lMap\tree =New leaf
		lMap\tree\w =lMapWidth
		lMap\tree\h =lMapHeight
	EndIf
	For leaf.leaf =Each leaf
		out.leaf =insertLeaf( leaf,texture)
		If out<>Null Return out
	Next
	;Return addLeaf( lMap\tree,texture)
End Function


Function insertLeaf.leaf( leaf.leaf,texture)
width =TextureWidth(texture)
height =TextureHeight(texture)	
	
If leaf\on Return 
	
	If width<=leaf\w And height<=leaf\h ;fits
		leaf\on =True

		leaf\leaf[0] =New leaf
		leaf\leaf[1] =New leaf
		leaf\leaf[0]\x =leaf\x+width
		leaf\leaf[0]\y =leaf\y
		leaf\leaf[0]\w =leaf\w-width-1
		leaf\leaf[0]\h =height
				
		leaf\leaf[1]\x =leaf\x
		leaf\leaf[1]\y =leaf\y+height
		leaf\leaf[1]\w =leaf\w
		leaf\leaf[1]\h =leaf\h-height
		
		leaf\w =width
		leaf\h =height
		CopyRect 0,0,width,height,leaf\x,leaf\y,TextureBuffer(texture),lMap\texBuf
		Return leaf
	EndIf
End Function


Function addLeaf.leaf( leaf.leaf,texture) ;internal function
Local nleaf.leaf
	width =TextureWidth(texture)
	height =TextureHeight(texture)

If leaf\on

    nleaf =insertLeaf( leaf\leaf[0],texture)
	If nleaf<>Null Return nleaf
	nleaf =insertLeaf( leaf\leaf[1],texture)
	If nleaf<>Null Return nleaf
Else
	nleaf =insertLeaf( leaf,texture)
	If nleaf<>Null Return nleaf
EndIf
	
End Function

Function leafU#(leaf.leaf,u#) ;converts a normal u coord into a lightmap u coord
	Return (leaf\x+(leaf\w*u))/lMapWidth 
End Function

Function leafV#(leaf.leaf,v#)
	Return (leaf\y+(leaf\h*v))/lMapHeight
End Function


Function prepChunks() ;final chunk 'prep' before rendering
Local miU#,miV#
Local maU#,maV#,rU#,rV#
Local u#[3],v#[3]
Local spiv =CreatePivot(),lpiv1=CreatePivot()
Local lpiv2 =CreatePivot(),lpiv3 =CreatePivot()

For chunk.chunk =Each chunk
If chunk\lit
		;-fx
		
		;----
		For vt=0 To 2
			chunk\shim[vt] =Rnd(360)
			Select chunk\pTex
				Case 1 ;yz
					u[vt] =chunk\y[vt]
					v[vt] =chunk\z[vt]
				Case 2 ;xz
					u[vt] =chunk\x[vt]
					v[vt] =chunk\z[vt]
				Case 3 ;xy
					u[vt] =chunk\x[vt]
					v[vt] =chunk\y[vt]
				Default
					RuntimeError "Illegal projection plane"
			End Select
		Next
	
		;map u,v into valid 0,1 range.
		miU = 9999
		miV = 9999
		maU = -9999
		maV = -9999
		For i=0 To 2
			If u[i]<miU
				miU =u[i]
			EndIf
			If u[i]>maU
				maU =u[i]
			EndIf
			If v[i]<miV
				miV =v[i]
			EndIf
			If v[i]>maV
				maV =v[i]
			EndIf
		Next
		
	
		rU =maU -miU
		rV =maV -miV
		
	
		If ru=0 ru=1
		If rv=0 rv=1
		
		r=2
		mapWidth =chunk\cw
		mapHeight =chunk\ch
	
		tempMap =CreateTexture( mapWidth,mapHeight,256)
		If Not tempMap Return
		For vt=0 To 2
			chunk\u[vt] =(u[vt]-miU) /rU
			chunk\v[vt] =(v[vt]-miV) /rV
		Next
		
	
    	dist# = -(chunk\nx * chunk\x[0]+chunk\ny*chunk\y[1]+chunk\nz*chunk\z[2])
		Select chunk\pTex
			Case 3
			
				Z# = -(chunk\nx*miU + chunk\ny * miV + Dist) / chunk\nz
				uvx# = miu : UVY# = miV : UVZ# = Z
				Z# = -(chunk\NX * maU + chunk\NY * miV + Dist) / chunk\NZ
				V1X# = maU : V1Y# = miV : V1Z# = Z
				Z# = -(chunk\NX * miu + chunk\NY * maV + Dist) / chunk\NZ
				V2X# = miu : V2Y# = maV : V2Z# = Z
			Case 2
				Y# = -(chunk\NX * miu + chunk\NZ * miV + Dist) / chunk\NY
				UVX# = miu : UVY# = Y : UVZ# = miV
				Y# = -(chunk\NX * maU + chunk\NZ * miV + Dist) / chunk\NY
				V1X# = maU : V1Y# = Y : V1Z# = miV
				Y# = -(chunk\NX * miu + chunk\NZ * maV + Dist) / chunk\NY
				V2X# = miu : V2Y# = Y : V2Z# = maV
			Case 1		
				X# = -(chunk\NY * miu + chunk\NZ * miV + Dist) / chunk\NX
				UVX# = X : UVY# = miu : UVZ# = miV
				X# = -(chunk\NY * maU + chunk\NZ * miV + Dist) / chunk\NX	
				V1X# = X : V1Y# = maU : V1Z# = miV	
				X# = -(chunk\NY * miu + chunk\NZ * maV + Dist) / chunk\NX
				V2X# = X : V2Y# = miu : V2Z# = maV
		End Select
		chunk\eux = V1X - UVX : chunk\euy = V1Y - UVY : chunk\euz = V1Z - UVZ
		chunk\evx = V2X - UVX : chunk\evy = V2Y - UVY : chunk\evz = V2Z - UVZ
		chunk\ox = UVX# : chunk\oy = UVY# : chunk\oz = UVZ#

	
			
	
	
		If mapWidth<>laW Or mapHeight<>laH
			laW =mapWidth
			laH =mapHeight
			If tmpImg FreeImage tmpImg
			tmpImg =CreateImage(mapWidth,mapHeight)
			tmpBuf =ImageBuffer(tmpImg)
			SetBuffer tmpBuf
		EndIf
		LockBuffer
		For x#=0 To mapWidth-1 Step 1
			au# = Float(x)/Float(mapwidth)
			N_UEdgeX# = chunk\EuX * au#  :  N_UEdgeY# = chunk\euY * au#  :  N_UEdgeZ# = chunk\euZ * au#
		For y#=0 To mapHeight-1 Step 1
			av# = Float(y)/Float(mapHeight)		
			N_VEdgeX# = chunk\evX * av#  :  N_VEdgeY# = chunk\evY * av#  :  N_VEdgeZ# = chunk\evZ * av#
			lx# = (chunk\ox + N_UEdgeX + N_VEdgeX)
			ly# = (chunk\oy + N_UEdgeY + N_VEdgeY)
			lz# = (chunk\oz + N_UEdgeZ + N_VEdgeZ)
			PositionEntity lpiv1,lx,ly,lz
			shaded =False
			For light.light =Each light
				PositionEntity lmap\scam,light\x,light\y,light\z
	
				For shade.shadeMap =Each shadeMap
					If light =shade\light
						If lcast.cast<>shade\cast
							PointEntity lmap\scam,shade\cast\id
							lcast=shade\cast
						EndIf
						
						CameraProject lmap\scam,lx,ly,lz
						px =ProjectedX()
						If px>0 And px<shadeWidth
							If shade\x[px]<>Null
								py =ProjectedY()
								If py>0 And py<shadeHeight
									shaded =shade\x[px]\y[py]
									If shaded Exit
								EndIf
							EndIf
						EndIf
					EndIf
				Next
							
				If Not shaded
					PositionEntity spiv,light\x,light\y,light\z
					ed# =EntityDistance(spiv,lpiv1)
					av=(255-ed*5)
					If av<0 av=0
					cv=cv+av
				EndIf
			Next
			If cv>255 cv=255
			WritePixelFast x,y,cv Or (cv Shl 8) Or (cv Shl 16)
			cv=0
		Next
		Next
		UnlockBuffer
		SetBuffer TextureBuffer(tempMap)
		DrawBlock tmpImg,0,0
		SetBuffer tmpBuf 	
	;CopyRect 0,0,mapWidth,mapHeight,0,0,tmpBuf,TextureBuffer(tempMap)
		

	
		

		
		;-
		leaf.leaf =newLeaf(tempMap) 
		chunk\leaf =leaf
		FreeTexture tempMap
		For vt=0 To 2 ;map lightmap onto mesh
			VertexTexCoords chunk\srf,chunk\vi[vt],leafU(leaf,chunk\u[vt]),leafV(leaf,chunk\v[vt]),0,1
		Next
EndIf
	Next

	For recv.recv =Each recv
		ShowEntity recv\id
	Next
	CameraZoom lmap\scam,1
	SetBuffer BackBuffer()
	CameraViewport lmap\scam,0,0,GraphicsWidth(),GraphicsHeight()
	FreeImage tmpImg
End Function

Function max3#(a#,b#,c#)
	If a>b 
		If a>c Return a
		Return c
	EndIf
	If b>c Return b
	Return c
End Function
Function min3#(a#,b#,c#)
	If a<b 
		If a<c Return a
		Return c
	EndIf
	If b<c Return b
	Return c
End Function


Function texToImage(texture) ;converts a texture to an image
	out =CreateImage(TextureWidth(texture),TextureHeight(texture))
	CopyRect 0,0,TextureWidth(texture),TextureHeight(texture),0,0,TextureBuffer(texture),ImageBuffer(out)
	Return out
End Function


;-- 3rd party functions. 

Function dot(x0#,y0#,x1#,y1#,x2#,y2#)

Return (x1#-x0#)*(y2#-y1#)-(x2#-x1#)*(y1#-y0#)

End Function

Function inTri(px#,py#,x0#,y0#,x1#,y1#,x2#,y2#)

If dot(x0,y0,x1,y1,px,py)>=0

If dot(x1,y1,x2,y2,px,py)>=0

If dot(x2,y2,x0,y0,px,py)>=0

Return True

EndIf

EndIf

EndIf

End Function


Global g_TriNormalX#, g_TriNormalY#, g_TriNormalZ#

Function TriNorm(x1#, y1#, z1#, x2#, y2#, z2#, x3#, y3#, z3#)
    ux# = x1# - x2#
    uy# = y1# - y2#
    uz# = z1# - z2#
    vx# = x3# - x2#
    vy# = y3# - y2#
    vz# = z3# - z2#	
	nx# = (uy# * vz#) - (vy# * uz#)
    ny# = (uz# * vx#) - (vz# * ux#)  
    nz# = (ux# * vy#) - (vx# * uy#)
; Normalize it
    NormLen# = Sqr((nx*nx) + (ny*ny) + (nz*nz))  
    If NormLen > 0
		nx = nx/NormLen : ny = ny/NormLen: nz = nz/NormLen
	Else
		nx = 0 : ny = 0 : nz = 1
	EndIf
	g_TriNormalX = nx
	g_TriNormalY = ny
	g_TriNormalZ = nz
End Function
Dim txv#(3) 
Type TRIS
Field x0#
Field y0#
Field z0#
Field u0#
Field v0#
Field U20#
Field V20#

Field x1#
Field y1#
Field z1#
Field u1#
Field v1#
Field U21#
Field V21#

Field x2#
Field y2#
Field z2#
Field u2#
Field v2#
Field U22#
Field V22#

Field surface
End Type

Function Weld(mish)
Dim txv(3)


For nsurf = 1 To CountSurfaces(mish)
su=GetSurface(mish,nsurf)
For tq = 0 To CountTriangles(su)-1
txv(0) = TriangleVertex(su,tq,0)
txv(1) = TriangleVertex(su,tq,1)
txv(2) = TriangleVertex(su,tq,2)
vq.TRIS = New TRIS
 
vq\x0# = VertexX(su,txv(0))
vq\y0# = VertexY(su,txv(0))
vq\z0# = VertexZ(su,txv(0))
vq\u0# = VertexU(su,txv(0),0)
vq\v0# = VertexV(su,txv(0),0)
vq\u20# = VertexU(su,txv(0),1)
vq\v20# = VertexV(su,txv(0),1)

vq\x1# = VertexX(su,txv(1))
vq\y1# = VertexY(su,txv(1))
vq\z1# = VertexZ(su,txv(1))
vq\u1# = VertexU(su,txv(1),0)
vq\v1# = VertexV(su,txv(1),0)
vq\u21# = VertexU(su,txv(1),1)
vq\v21# = VertexV(su,txv(1),1)

vq\x2# = VertexX(su,txv(2))
vq\y2# = VertexY(su,txv(2))
vq\z2# = VertexZ(su,txv(2))
vq\u2# = VertexU(su,txv(2),0)
vq\v2# = VertexV(su,txv(2),0)
vq\u22# = VertexU(su,txv(2),1)
vq\v22# = VertexV(su,txv(2),1)
Next

ClearSurface su

For vq.tris = Each tris

vt1=findvert(su,vq\x0#,vq\y0#,vq\z0#,vq\u0#,vq\v0#,vq\u20#,vq\v20#)

If vt1=-1 Then
vt1=AddVertex(su,vq\x0#,vq\y0#,vq\z0#,vq\u0#,vq\v0#)
VertexTexCoords su,mycount,vq\u20#,vq\v20#,0,1
vt1 = mycount
mycount = mycount +1
EndIf

vt2=findvert(su,vq\x1#,vq\y1#,vq\z1#,vq\u1#,vq\v1#,vq\u21#,vq\v21#)
If Vt2=-1 Then
vt2=AddVertex( su,vq\x1#,vq\y1#,vq\z1#,vq\u1#,vq\v1#)
VertexTexCoords su,mycount,vq\u21#,vq\v21#,0,1
vt2 = mycount
mycount = mycount +1
EndIf

vt3=findvert(su,vq\x2#,vq\y2#,vq\z2#,vq\u2#,vq\v2#,vq\u22#,vq\v22#)

If vt3=-1 Then 
vt3=AddVertex(su,vq\x2#,vq\y2#,vq\z2#,vq\u2#,vq\v2#)
VertexTexCoords su,mycount,vq\u22#,vq\v22#,0,1
vt3 = mycount
mycount = mycount +1
EndIf

AddTriangle su,vt1,vt2,vt3

Next

Delete Each tris
mycount=0
Next
End Function

Function findvert(su,x2#,y2#,z2#,u2#,v2#,u22#,v22#)
Local thresh# =0.001

For t=0 To CountVertices(su)-1
If Abs(VertexX(su,t)-x2#)<thresh# Then 
If Abs(VertexY(su,t)-y2#)<thresh# Then 
If Abs(VertexZ(su,t)-z2#)<thresh# Then 
If Abs(VertexU(su,t,0)-u2#)<thresh# Then 
If Abs(VertexV(su,t,0)-v2#)<thresh# Then 
If Abs(VertexU(su,t,1)-u22#)<thresh# Then 
If Abs(VertexV(su,t,1)-v22#)<thresh# Then
Return t
EndIf
EndIf
EndIf
EndIf
EndIf
EndIf
EndIf
Next
Return -1
End Function

Function debugMeshUV( mesh)
	srf =GetSurface(mesh,1)

		For v=0 To CountVertices(srf)-2
			DebugLog "U1>"+VertexU(srf,v,0)
			DebugLog "U2>"+VertexU(srf,v,0)
			DebugLog "V1>"+VertexV(srf,v,0)
			DebugLog "V2>"+VertexV(srf,v,0)
			VertexTexCoords srf,v,Rnd(0.3),Rnd(0.3)
		Next


	
End Function


Function debugLeafs(doLog=True)
If doLog=4
	For leaf.leaf =Each leaf
		DebugLog "=-=-=-=-=-=-=-=-=-=-"
		If leaf\on
			DebugLog "Active leaf"
		EndIf
		DebugLog "X:"+leaf\x+" Y:"+leaf\y+" W:"+leaf\w+" H:"+leaf\h
	Next
EndIf
	;Return
	If Not MouseDown(1) Return
	For leaf.leaf =Each leaf
		Color 128,128,128
		Rect leaf\x,leaf\y,leaf\w,leaf\h
		Color 255,255,255
		Rect leaf\x,leaf\y,leaf\w,leaf\h,0
	Next
End Function


Function Unweld(mesh)
;Unweld a mesh, retaining all of its textures coords and textures
For surfcount = 1 To CountSurfaces(mesh)
surf = GetSurface(mesh,surfcount)

count = CountTriangles(surf)
bank = CreateBank((15*count)*4)
For tricount = 0 To count-1
off = (tricount*15)*4
in = TriangleVertex(surf,tricount,0)
x# = VertexX(surf,in):y#=VertexY(surf,in):z#=VertexZ(surf,in)
u# = VertexU(surf,in):v#=VertexV(surf,in)
PokeFloat(bank,off,x)
PokeFloat(bank,off+4,y)
PokeFloat(bank,off+8,z)
PokeFloat(bank,off+12,u)
PokeFloat(bank,off+16,v)

in = TriangleVertex(surf,tricount,1)
x# = VertexX(surf,in):y#=VertexY(surf,in):z#=VertexZ(surf,in)
u# = VertexU(surf,in):v#=VertexV(surf,in)
PokeFloat(bank,off+20,x)
PokeFloat(bank,off+24,y)
PokeFloat(bank,off+28,z)
PokeFloat(bank,off+32,u)
PokeFloat(bank,off+36,v)

in = TriangleVertex(surf,tricount,2)
x# = VertexX(surf,in):y#=VertexY(surf,in):z#=VertexZ(surf,in)
u# = VertexU(surf,in):v#=VertexV(surf,in)
PokeFloat(bank,off+40,x)
PokeFloat(bank,off+44,y)
PokeFloat(bank,off+48,z)
PokeFloat(bank,off+52,u)
PokeFloat(bank,off+56,v)
Next

ClearSurface(surf,True,True)

For tricount = 0 To count-1
off = (tricount*15)*4
x# = PeekFloat(bank,off)
y# = PeekFloat(bank,off+4)
z# = PeekFloat(bank,off+8)
u# = PeekFloat(bank,off+12)
v# = PeekFloat(bank,off+16)
a = AddVertex(surf,x,y,z,u,v)
x# = PeekFloat(bank,off+20)
y# = PeekFloat(bank,off+24)
z# = PeekFloat(bank,off+28)
u# = PeekFloat(bank,off+32)
v# = PeekFloat(bank,off+36)
b = AddVertex(surf,x,y,z,u,v)
x# = PeekFloat(bank,off+40)
y# = PeekFloat(bank,off+44)
z# = PeekFloat(bank,off+48)
u# = PeekFloat(bank,off+52)
v# = PeekFloat(bank,off+56)
c = AddVertex(surf,x,y,z,u,v)
AddTriangle(surf,a,b,c)
Next
FreeBank bank

Next
;UpdateNormals mesh


Return mesh
End Function

Function TNormX#()
	Return g_TriNormalX
End Function
Function TNormY#()
	Return g_TriNormalY
End Function
Function TNormZ#()
	Return g_TriNormalZ
End Function

; Load in animation sequences
;Idle=ExtractAnimSeq(gsg9,100,159)
;Run=ExtractAnimSeq(gsg9,2,37)
;Jump=ExtractAnimSeq(gsg9,38,99)
;Crouch=ExtractAnimSeq(gsg9,161,190)
;CrouchWalk=ExtractAnimSeq(gsg9,192,220)
