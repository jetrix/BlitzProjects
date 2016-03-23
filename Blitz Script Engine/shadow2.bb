
Graphics3D 640,480,16,2

SetBuffer BackBuffer()

Type Shadow_Caster
Field Caster
Field Receiver
Field Texture
Field Radius#
End Type


Type Shadow
Field Mesh
End Type


.Main
camera=CreateCamera()
PositionEntity camera,0,100,-300

light=CreateLight(2)
PositionEntity light,0,200,0
AmbientLight 100,100,100

ground=CreateCone()
; Scale the mesh. If you want to change the mesh's shape you have to scalemesh, not scaleentity. 
; The vertices won't represent their position in space if you scaleentity.
ScaleMesh ground,100,50,100
; Update the normals because we scaled the mesh (newest versions of Blitz should not require this)
UpdateNormals ground

cube = CreateCube()
ScaleEntity cube,20,20,20
RotateEntity cube,30,50,70
PositionEntity cube,0,150,0

PointEntity camera,cube

; Create texture of black square in white background
shd=CreateTexture(256,256,48) ; Flags = ClampU, ClampV
TextureBlend shd, 3
SetBuffer TextureBuffer(shd)
ClsColor 255, 255, 255
Cls
Color 0,0,0
; Must leave border of white around shadow!
Rect 1,1,254,254,1
SetBuffer BackBuffer()

Cast_Shadow(cube, ground, shd, 20.0) 

While KeyDown(1)=0

If KeyDown(200) MoveEntity ground, 0, 0, 1
If KeyDown(208) MoveEntity ground, 0, 0, -1
If KeyDown(203) MoveEntity ground, -1, 0, 0
If KeyDown(205) MoveEntity ground, 1, 0, 0

Update_Shadows() 

UpdateWorld
RenderWorld
Flip

Wend


End


; -------------------------------------------------------------------------------------------------------------------
; This function sets up a new shadow caster/receiver pair.
;
; Caster = Mesh to cast shadows.
; Receiver = Mesh to receive shadows.
; Texture = Texture to use for shadow. (Must have UV clamping enabled for proper operation!)
; Radius# = Radius of the shadow.
; -------------------------------------------------------------------------------------------------------------------
Function Cast_Shadow(Caster, Receiver, Texture, Radius#)

ThisCaster.Shadow_Caster = New Shadow_Caster 

ThisCaster\Caster = Caster
ThisCaster\Receiver = Receiver
ThisCaster\Texture = Texture
ThisCaster\Radius# = Radius#

End Function


; -------------------------------------------------------------------------------------------------------------------
; This function deletes all old shadows and creates a shadow mesh for each shadow casting object.
;
; Note:
; Shadows only need to be repositioned and recreated each time the scene is rendered, 
; not every time the physics are updated.
; -------------------------------------------------------------------------------------------------------------------
Function Update_Shadows()

Delete_Shadows()

For ThisCaster.Shadow_Caster = Each Shadow_Caster

Shadow_Center_X# = EntityX#(ThisCaster\Caster, True)
Shadow_Center_Y# = EntityY#(ThisCaster\Caster, True)
Shadow_Center_Z# = EntityZ#(ThisCaster\Caster, True)
Shadow_Radius# = ThisCaster\Radius# 

ThisShadow.Shadow = New Shadow
ThisShadow\Mesh = Create_Shadow(ThisCaster\Receiver, Shadow_Center_X#-Shadow_Radius#, Shadow_Center_Z#+Shadow_Radius#, Shadow_Center_X#+Shadow_Radius#, Shadow_Center_Z#-Shadow_Radius#, Shadow_Center_Y#)

; full bright, use vertex colors, no fog.
EntityFX ThisShadow\Mesh, 1+2+8

; multiply blend mesh.
EntityBlend ThisShadow\Mesh, 2 

EntityTexture ThisShadow\Mesh, ThisCaster\Texture

Next 

End Function


; -------------------------------------------------------------------------------------------------------------------
; This function deletes all active shadows.
; -------------------------------------------------------------------------------------------------------------------
Function Delete_Shadows()

For ThisShadow.Shadow = Each Shadow
FreeEntity ThisShadow\Mesh
Delete ThisShadow.Shadow
Next 

End Function


; -------------------------------------------------------------------------------------------------------------------
; This function deletes all shadow casters/recievers which reference the specified entity.
;
; When you delete an entity which is casting or receiving a shadow, you must call this function so that the game
; does not crash from trying to reference an entity which does not exist.
; -------------------------------------------------------------------------------------------------------------------
Function Delete_Shadow_Caster(Entity)

For ThisCaster.Shadow_Caster = Each Shadow_Caster

If (ThisCaster\Caster = Entity) Or (ThisCaster\Receiver = Entity) 
Delete ThisCaster
EndIf 

Next 

End Function


; -------------------------------------------------------------------------------------------------------------------
; This function creates a shadow mesh and returns it's handle.
;
; Reciever = The entity to recieve the shadow.
; -------------------------------------------------------------------------------------------------------------------
Function Create_Shadow(Receiver, Shadow_X1#, Shadow_Z1#, Shadow_X2#, Shadow_Z2#, Shadow_Y#)

; Get the coordinates of the receiver in world space.
Receiver_X# = EntityX#(Receiver, True)
Receiver_Y# = EntityY#(Receiver, True)
Receiver_Z# = EntityZ#(Receiver, True)

; Get the coordinates of the lower left hand corner of the shadow in world space.
Shadow_Corner_X# = Shadow_X1#
Shadow_Corner_Z# = Shadow_Z2# 

; Convert the shadow's coordinates into the reciever's vertex space.
Shadow_X1# = Shadow_X1# - Receiver_X#
Shadow_X2# = Shadow_X2# - Receiver_X#
Shadow_Z1# = Shadow_Z1# - Receiver_Z#
Shadow_Z2# = Shadow_Z2# - Receiver_Z#
Shadow_Y# = Shadow_Y# - Receiver_Y#

; Determine how large the shadow is.
Shadow_Scale# = Sqr((Shadow_X1#-Shadow_X2#)*(Shadow_X1#-Shadow_X2#))

ShadowTris = 0

; Create the mesh for the shadow, and give it a surface to add polygons to.
MESH_Shadow = CreateMesh()
SURFACE_Shadow = CreateSurface(MESH_Shadow)

; Loop through all triangles in all surfaces of the reciever.
Surfaces = CountSurfaces(Receiver)
For LOOP_Surface = 1 To Surfaces

Surface_Handle = GetSurface(Receiver, LOOP_Surface)

Tris = CountTriangles(Surface_Handle)
For LOOP_Tris = 0 To Tris-1

Vertex_0 = TriangleVertex(Surface_Handle, LOOP_Tris, 0)
Vertex_1 = TriangleVertex(Surface_Handle, LOOP_Tris, 1)
Vertex_2 = TriangleVertex(Surface_Handle, LOOP_Tris, 2)

; Check to see if the triangle is inside the shadow's bounding rectangle.
;
; This test works by seeing if all of a triangle's points are on a specific side of each side of the
; rectangle. The test is not 100% accurate... a very few triangles will pass the test but actually be
; outside the region. But we are concerned only with making sure we find all the triangles which ARE
; in the region and cull the vast majority outside the region, so a little sloppiness in the test is
; okay if that means it's really fast.
Shadow = True

Z0# = VertexZ#(Surface_Handle, Vertex_0)
Z1# = VertexZ#(Surface_Handle, Vertex_1)
Z2# = VertexZ#(Surface_Handle, Vertex_2)

; Is polygon to north of shadow's bounding rectangle?
If (Z0# > Shadow_Z1#) And (Z1# > Shadow_Z1#) And (Z2# > Shadow_Z1#) 
Shadow = False 
Else 

; Is polygon to south of shadow's bounding rectangle?
If (Z0# < Shadow_Z2#) And (Z1# < Shadow_Z2#) And (Z2# < Shadow_Z2#) 
Shadow = False 
Else

X0# = VertexX#(Surface_Handle, Vertex_0)
X1# = VertexX#(Surface_Handle, Vertex_1)
X2# = VertexX#(Surface_Handle, Vertex_2)

; Is polygon to west of shadow's bounding rectangle?
If (X0# < Shadow_X1#) And (X1# < Shadow_X1#) And (X2# < Shadow_X1#) 
Shadow = False 
Else

; Is polygon to east of shadow's bounding rectangle?
If (X0# > Shadow_X2#) And (X1# > Shadow_X2#) And (X2# > Shadow_X2#) 
Shadow = False 
Else

Y0# = VertexY#(Surface_Handle, Vertex_0)
Y1# = VertexY#(Surface_Handle, Vertex_1)
Y2# = VertexY#(Surface_Handle, Vertex_2)

; Is shadow below the polygon?
If (Shadow_Y# < Y0#) And (Shadow_Y# < Y1#) And (Shadow_Y# < Y2#)
Shadow = False
Else

NY0# = VertexNY#(Surface_Handle, Vertex_0) 
NY1# = VertexNY#(Surface_Handle, Vertex_1) 
NY2# = VertexNY#(Surface_Handle, Vertex_2)

FaceNY# = NY0# + NY1# + NY2# 

; Is polygon facing downwards?
If (FaceNY# < 0) Then
Shadow = False
Else

If (FaceNY# = 0)
Shadow = False
EndIf 

EndIf
EndIf
EndIf
EndIf
EndIf
EndIf 

; If the triangle is inside the bounding box then add it to the mesh.
; Note that shadow texture will be flipped top to bottom because of how UV coordinates are assigned to the vertices from the world coordinates.
If Shadow = True

; Convert the coordinates of the vertices of the receiver from receiver space to world space.
X0# = X0# + Receiver_X#
X1# = X1# + Receiver_X#
X2# = X2# + Receiver_X#
Y0# = Y0# + Receiver_Y# + 0.1
Y1# = Y1# + Receiver_Y# + 0.1
Y2# = Y2# + Receiver_Y# + 0.1
Z0# = Z0# + Receiver_Z#
Z1# = Z1# + Receiver_Z#
Z2# = Z2# + Receiver_Z#

Shadow_Vertex_0 = AddVertex(SURFACE_Shadow, X0#, Y0#, Z0#, (X0#-Shadow_Corner_X#)/Shadow_Scale#, (Z0#-Shadow_Corner_Z#)/Shadow_Scale#)
Shadow_Vertex_1 = AddVertex(SURFACE_Shadow, X1#, Y1#, Z1#, (X1#-Shadow_Corner_X#)/Shadow_Scale#, (Z1#-Shadow_Corner_Z#)/Shadow_Scale#)
Shadow_Vertex_2 = AddVertex(SURFACE_Shadow, X2#, Y2#, Z2#, (X2#-Shadow_Corner_X#)/Shadow_Scale#, (Z2#-Shadow_Corner_Z#)/Shadow_Scale#)

; Adjust shadow brightness at each vertex according to how much the vertex normal points down.
;VC = (1.0-NY0#)^3*255.0
VC = (1.0-NY0#)*(1.0-NY0#)*255.0
VertexColor SURFACE_Shadow, Shadow_Vertex_0, VC, VC, VC

VC = (1.0-NY1#)*(1.0-NY1#)*255.0
VertexColor SURFACE_Shadow, Shadow_Vertex_1, VC, VC, VC

VC = (1.0-NY2#)*(1.0-NY2#)*255.0
VertexColor SURFACE_Shadow, Shadow_Vertex_2, VC, VC, VC

; Add the triangle to the shadow mesh.
AddTriangle(SURFACE_Shadow, Shadow_Vertex_0, Shadow_Vertex_1, Shadow_Vertex_2)

ShadowTris = ShadowTris + 1

EndIf

Next 

Next

Return MESH_Shadow

End Function

