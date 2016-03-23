; ID: 524
; Author: halo
; Date: 2002-12-07 15:52:04
; Title: LoadB3D()  (updated)
; Description: Load a .b3d file, with access to all the texture data, and some other options.

;LoadB3D( file$, [parent], [texturedir$], [flags] )

;file$ - file to load
;parent (optional) - mesh parent.
;texturedir$ (optional) - directory to load textures from.
;flags (optional) - none yet.

;No support for animations or bones.
;Hierarchies now supported.

Function LoadB3D(file$,parent,texturedir$="",flags=0)
f=ReadFile(file)
If Not f Return
tag$=ReadStringN(f,3)
chunksize=ReadInt(f)
endchunkpos=chunksize+FilePos(f)
texbank=CreateBank()
brushbank=CreateBank()
vertbank=CreateBank()
trisbank=CreateBank()
If tag="BB3D" Return loadchunk("BB3D",f,endchunkpos,parent,texbank,brushbank,vertbank,trisbank,texturedir$)
For n=0 To (BankSize(texbank)-1)/4
	texture=PeekInt(texbank,n*4)
	If texture FreeTexture texture
	Next
For n=0 To (BankSize(brushbank)-1)/4
	brush=PeekInt(brushbank,n*4)
	If brush FreeBrush brush
	Next
FreeBank texbank
FreeBank brushbank
FreeBank vertbank
FreeBank trisbank
End Function

Function loadchunk(tag$,f,endchunkpos,parent=0,texbank,brushbank,vertbank,trisbank,texturedir$)
DebugLog tag
Select tag
	Case "BB3D"
		version=ReadInt(f)
		DebugLog "	version: "+version	
	Case "TEXS"
		name$=ReadStringN(f)
		name=findfile(name,texturedir$)
		flags=ReadInt(f)
		blend=ReadInt(f)				
		x#=ReadFloat(f)		
		y#=ReadFloat(f)			
		scalex#=ReadFloat(f)		
		scaley#=ReadFloat(f)		
		rotation#=ReadFloat(f)		
		texture=LoadTexture(name,flags)
		If texture
			If 65536 And flags TextureCoords texture,1
			TextureBlend texture,blend
			PositionTexture texture,x,y
			ScaleTexture texture,scalex,scaley
			RotateTexture texture,rotation
			EndIf
		ResizeBank texbank,BankSize(texbank)+4
		PokeInt(texbank,BankSize(texbank)-4,texture)
		DebugLog "	name: "+name
		DebugLog "	flags: "+flags
		DebugLog "	blend: "+blend
		DebugLog "	position: "+x+", "+y			
		DebugLog "	scale: "+scalex+", "+scaley	
		DebugLog "	rotation: "+rotation	
	Case "BRUS"
		textures=ReadInt(f)
		name$=ReadStringN(f)
		red=ReadFloat(f)*255	
		green=ReadFloat(f)*255		
		blue=ReadFloat(f)*255		
		alpha#=ReadFloat(f)			
		shininess#=ReadFloat(f)	
		blend=ReadInt(f)			
		fx=ReadInt(f)	
		brush=CreateBrush()
		BrushColor brush,red,green,blue
		BrushAlpha brush,alpha
		BrushShininess brush,shininess
		BrushBlend brush,blend
		BrushFX brush,fx	
		For n=1 To textures
			textureindex=ReadInt(f)
			If textureindex*4+4<=BankSize(texbank)
				texture=PeekInt(texbank,textureindex*4)
				Else
				RuntimeError "Texture does not exist."
				EndIf
			If texture
				BrushTexture brush,texture,0,n-1
				EndIf
			Next
		ResizeBank brushbank,BankSize(brushbank)+4
		PokeInt brushbank,BankSize(brushbank)-4,brush
		;DebugLog "	name: "+name
		;DebugLog "	color: "+red+", "+green+", "+blue
		;DebugLog "	alpha: "+alpha			
		;DebugLog "	shininess: "+shininess	
		;DebugLog "	blend: "+blend
		;DebugLog "	fx: "+fx	
	Case "NODE"
		lastmeshendchunkpos=endchunkpos
		name$=ReadStringN(f)
		DebugLog name
		x#=ReadFloat(f)
		y#=ReadFloat(f)
		z#=ReadFloat(f)
		width#=ReadFloat(f)
		height#=ReadFloat(f)
		depth#=ReadFloat(f)			
		w#=ReadFloat(f)
		pitch#=ReadFloat(f)
		yaw#=ReadFloat(f)
		roll#=ReadFloat(f)
		;DebugLog "	name: "+name
		;DebugLog "	position: "+x+", "+y+", "+z
		;DebugLog "	scale: "+width+", "+height+", "+depth
		;DebugLog "	rotation: "+pitch+", "+yaw+", "+roll+", "+w	
		;If FilePos(f)=endchunkpos
			mesh=CreateMesh(parent);this is more compatible than inserting a pivot.
			parent=mesh
			NameEntity mesh,name
			PositionEntity mesh,x,y,z		
			ScaleEntity mesh,width,height,depth
			RotateEntity mesh,pitch,yaw,roll;how do b3d rotations work?								
		;	EndIf
	Case "MESH"
		brush=ReadInt(f)
		;mesh=CreateMesh(parent)
		;parent=mesh
		;NameEntity mesh,name
		;PositionEntity mesh,x#,y#,z#
		;ScaleEntity mesh,width,height,depth
		;RotateEntity mesh,pitch,yaw,roll;how do b3d rotations work?
	Case "VRTS"
		flags=ReadInt(f)
		texcoordsets=ReadInt(f)
		texcoords=ReadInt(f)
		;DebugLog "	flags: "+flags
		;DebugLog "	texturecoord sets: "+texcoordsets	
		;DebugLog "	texturecoords:"+texcoords			
		ResizeBank vertbank,0
			While FilePos(f)<endchunkpos
				x#=ReadFloat(f)
				y#=ReadFloat(f)
				z#=ReadFloat(f)
				;TFormPoint x,y,z,0,parent
				;x=TFormedX()
				;y=TFormedY()
				;z=TFormedZ()
				If texcoords>0
					u0#=ReadFloat(f)
					If texcoords>1
						v0#=ReadFloat(f)
						If texcoords>2
							w0#=ReadFloat(f)
							EndIf
						EndIf
					EndIf
				If texcoordsets>1
					If texcoords>0
						u1#=ReadFloat(f)
						If texcoords>1
							v1#=ReadFloat(f)
							If texcoords>2
								w1#=ReadFloat(f)
								EndIf
							EndIf
						EndIf					
					EndIf
					;DebugLog "		position: "+x+", "+y+", "+z
					;DebugLog "		texturecoords:"+u0+", "+v0+", "+w0
					;DebugLog "		texturecoords:"+u1+", "+v1+", "+w1
					;DebugLog ""
				ResizeBank vertbank,BankSize(vertbank)+4*10
				PokeFloat vertbank,BankSize(vertbank)-4*10,x
				PokeFloat vertbank,BankSize(vertbank)-4*9,y
				PokeFloat vertbank,BankSize(vertbank)-4*8,z
				PokeFloat vertbank,BankSize(vertbank)-4*7,u0				
				PokeFloat vertbank,BankSize(vertbank)-4*6,v0
				PokeFloat vertbank,BankSize(vertbank)-4*5,w0
				PokeFloat vertbank,BankSize(vertbank)-4*4,u1				
				PokeFloat vertbank,BankSize(vertbank)-4*3,v1
				PokeFloat vertbank,BankSize(vertbank)-4*2,w1
				PokeInt vertbank,BankSize(vertbank)-4*1,0		
				Wend
		Case "TRIS"
			brush=ReadInt(f)
			;DebugLog "	brush: "+brush
			ResizeBank trisbank,0
			For n=1 To (BankSize(vertbank))/(4*10)
				PokeInt vertbank,36+(n-1)*(4*10),0
				Next
			While FilePos(f)<endchunkpos
				a=ReadInt(f)
				b=ReadInt(f)
				c=ReadInt(f)
				If a*(4*10)<BankSize(vertbank) PokeByte vertbank,36+a*(4*10),1 Else RuntimeError "Vertex does not exist."
				If b*(4*10)<BankSize(vertbank) PokeByte vertbank,36+b*(4*10),1 Else RuntimeError "Vertex does not exist."
				If c*(4*10)<BankSize(vertbank) PokeByte vertbank,36+c*(4*10),1 Else RuntimeError "Vertex does not exist."
				;DebugLog "		vertices: "+a+", "+b+", "+c
				ResizeBank trisbank,BankSize(trisbank)+12
				PokeInt trisbank,BankSize(trisbank)-12,a
				PokeInt trisbank,BankSize(trisbank)-8,b
				PokeInt trisbank,BankSize(trisbank)-4,c
				triscount=triscount+1			
				Wend
			If triscount
				surf=CreateSurface(parent)
				If brush>-1
					If brush*4+4<=BankSize(brushbank)
						PaintSurface surf,PeekInt(brushbank,brush*4)
						Else
						RuntimeError "Brush does not exist." 
						EndIf
					EndIf
				EndIf
			vertcount=0
			For n=1 To (BankSize(vertbank))/(4*10)
				If PeekInt(vertbank,36+(n-1)*(4*10))
					x#=PeekFloat(vertbank,(n-1)*(4*10))
					y#=PeekFloat(vertbank,4+(n-1)*(4*10))							
					z#=PeekFloat(vertbank,8+(n-1)*(4*10))
					u0#=PeekFloat(vertbank,12+(n-1)*(4*10))
					v0#=PeekFloat(vertbank,16+(n-1)*(4*10))
					w0#=PeekFloat(vertbank,20+(n-1)*(4*10))
					u1#=PeekFloat(vertbank,24+(n-1)*(4*10))
					v1#=PeekFloat(vertbank,28+(n-1)*(4*10))
					w1#=PeekFloat(vertbank,32+(n-1)*(4*10))
					AddVertex surf,x,y,z,u0,v0,w0
					VertexTexCoords surf,CountVertices(surf)-1,u1,v1,w1,1
					vertcount=vertcount+1
					PokeInt vertbank,36+(n-1)*(4*10),vertcount
					EndIf
				Next
			For n=1 To (BankSize(trisbank))/12
				a=PeekInt(trisbank,(n-1)*12)
				b=PeekInt(trisbank,4+(n-1)*12)
				c=PeekInt(trisbank,8+(n-1)*12)
				For i=1 To (BankSize(vertbank))/(4*10)
					If i-1=a a=PeekInt(vertbank,36+(i-1)*(4*10))-1
					If i-1=b b=PeekInt(vertbank,36+(i-1)*(4*10))-1
					If i-1=c c=PeekInt(vertbank,36+(i-1)*(4*10))-1
					Next
				AddTriangle surf,a,b,c
				Next
			UpdateNormals parent
		Default
			SeekFile f,endchunkpos	
	End Select
While FilePos(f)<endchunkpos
	tag$=ReadStringN(f,3)
	chunksize=ReadInt(f)
	childendchunkpos=chunksize+FilePos(f)
	m=loadchunk(tag,f,childendchunkpos,parent,texbank,brushbank,vertbank,trisbank,texturedir$)	
	If Not parent parent=m
	Wend
Return parent
End Function

Function ReadStringN$(f,maxlength=0)
Repeat
	ch=ReadByte(f)
	If ch=0 Return t$
	If maxlength
		If Len(t$)=maxlength Return t$+Chr(ch)
		EndIf
	t$=t$+Chr$(ch)
	Forever
End Function

Function countfiles(dirname$)
dir=ReadDir(dirname)
If Not dir RuntimeError dirname
n=-1
Repeat
n=n+1
Until NextFile(dir)=""
CloseDir dir
Return n
End Function

Function findfile$(file$,root$)
If FileType(root+file)=1 Return root+file
d=ReadDir(root)
For n=1 To countfiles(root)
	tfile$=NextFile(d)
	If FileType(root+tfile)=2
		If tfile<>"." And tfile<>".."
			test$=findfile(file,root+tfile+"\")	
			If test<>"" Return test
			EndIf
		EndIf
	Next
CloseDir d
End Function
